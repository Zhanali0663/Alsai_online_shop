package com.example.myapplication

import Product
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text

class ProductOptions : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_options)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val leftButt = findViewById<Button>(R.id.button_left)
        val rightButt = findViewById<Button>(R.id.button_right)
        val loadingLayout = findViewById<View>(R.id.POI)
        val popul = findViewById<TextView>(R.id.rating)


       fun updatePopularityCounter(change: Long) {

            val dbPopularity = FirebaseDatabase.getInstance().getReference("goods").child(Test.name).child("popular")
            dbPopularity.runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    var currentValue = currentData.getValue(Long::class.java)
                    // Увеличиваем или уменьшаем значение
                    currentData.value = currentValue?.plus(change*(-1))
                    if (change.toInt() == -1){rightButt.text = "Add to cart"}else{rightButt.text = "Remove to cart"}





                    return Transaction.success(currentData)
                }
                override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {}
            })
           loadingLayout.visibility = View.GONE
           loadingLayout.setOnTouchListener { _, _ -> false }
       }


        val image = findViewById<ImageView>(R.id.POimage)
        val t = findViewById<TextView>(R.id.textView10)
        val pr = findViewById<TextView>(R.id.textView11)
        t.text = Test.name.take(18)
        pr.text = Test.price
        //popul.text = (Test.popular.toInt() *(-1)).toString()


        val descrip = findViewById<TextView>(R.id.descrip)
        val photoSource = Test.url
        Glide.with(this@ProductOptions).load(photoSource).into(image)
        loadingLayout.visibility = View.GONE


        val dbDescrip = FirebaseDatabase.getInstance().getReference("goods").child(Test.name).child("des")
        dbDescrip.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                descrip.text = snapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        val dbInCart = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child("cart")
        dbInCart.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cart = snapshot.value.toString()
                if (Test.name in cart){
                    rightButt.text = "Remove to cart"
                    rightButt.setBackgroundColor(Color.parseColor("#ff2424"))
                }else{
                    rightButt.text = "Add to cart"
                    rightButt.setBackgroundColor(Color.parseColor("#3dfa37"))
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        rightButt.setOnClickListener {
            if (rightButt.text.toString() == "Loading..."){ Toast.makeText(this, "please wait", Toast.LENGTH_SHORT).show() }
            else if (rightButt.text.toString() == "Remove to cart") {
                loadingLayout.visibility = View.VISIBLE
                loadingLayout.setOnTouchListener { _, _ -> true }
                val dbRemoveToCart = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child("cart").child(Test.name)
                dbRemoveToCart.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        updatePopularityCounter(-1)
                        rightButt.setBackgroundColor(Color.parseColor("#3dfa37"))


                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (rightButt.text.toString() == "Add to cart"){
                loadingLayout.visibility = View.VISIBLE
                loadingLayout.setOnTouchListener { _, _ -> true }
                val dbAddToCart =  FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child("cart")
                dbAddToCart.child(Test.name).setValue(1).addOnCompleteListener {
                    if (it.isSuccessful) {
                        updatePopularityCounter(1)
                        rightButt.setBackgroundColor(Color.parseColor("#ff2424"))
                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }






    }
}