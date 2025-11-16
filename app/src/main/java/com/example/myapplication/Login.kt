package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase


class Login : AppCompatActivity() {
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.loadingfragmentviev)
        if (fragment is LoadingFragment) {
            // ничего не делаем — блокируем выход
        } else {
            super.onBackPressed()
        }
    }

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth


        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl

            // Check if user's email is verified
            val emailVerified = it.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = it.uid
        }


        val gmail = findViewById<EditText>(R.id.LGmail)
        val password = findViewById<EditText>(R.id.LPass)
        val button = findViewById<Button>(R.id.LButton)
        val db = FirebaseDatabase.getInstance().reference
        button.setOnClickListener {
            val tgmail = gmail.text.toString()
            val tpass = password.text.toString()

            if (tgmail == "" || tpass == ""){
                Toast.makeText(this, "Not all fields are filled in", Toast.LENGTH_SHORT).show()
            }else if ("@gmail.com" !in tgmail){
                Toast.makeText(baseContext, "Incorrect Gmail", Toast.LENGTH_SHORT,).show()
            }
            else {
                supportFragmentManager.beginTransaction().add(R.id.loadingfragmentviev, LoadingFragment()).commit()

                auth.signInWithEmailAndPassword(tgmail, tpass).addOnCompleteListener(this) { task ->
                    supportFragmentManager.beginTransaction().remove(supportFragmentManager.findFragmentById(R.id.loadingfragmentviev)!!).commit()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        //получяем айди пользователя
                        val uid = user?.uid ?: return@addOnCompleteListener
                        db.child("users").child(uid).get().addOnSuccessListener { snapshot ->
                            if (snapshot.exists()) {
                                Toast.makeText(this, "Hello ${user?.displayName}", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finishAffinity()
                            }else{
                                FirebaseAuth.getInstance().signOut()
                                Toast.makeText(this, "This account has been banned, create a new one", Toast.LENGTH_SHORT).show()
                            }
                        }


                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }

            }



        }
    }
}
