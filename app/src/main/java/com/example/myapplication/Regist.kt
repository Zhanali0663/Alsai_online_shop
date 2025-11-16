package com.example.myapplication

import android.content.ContentValues.TAG
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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase


class Regist : AppCompatActivity() {
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.loadingfragmentviev2)
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
        setContentView(R.layout.activity_regist)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        val name = findViewById<EditText>(R.id.RName)
        val gmail = findViewById<EditText>(R.id.Rgmail)
        val password = findViewById<EditText>(R.id.Rpassword)
        val confirm = findViewById<Button>(R.id.Rconfirm)

        val db = FirebaseDatabase.getInstance().reference




        confirm.setOnClickListener {
            val tname = name.text.toString()
            val tgmail = gmail.text.toString()
            val tpass = password.text.toString()
            if (tname == "" || tgmail == "" || tpass == ""){
                Toast.makeText(this, "Not all fields are filled in", Toast.LENGTH_SHORT).show()

            }else if ("@gmail.com" !in tgmail){
                Toast.makeText(baseContext, "Incorrect Gmail", Toast.LENGTH_SHORT,).show()
            }else if (" " in tname){
                Toast.makeText(baseContext, "Name cannot contain spaces", Toast.LENGTH_SHORT,).show()
            }
            else if (tname == "null"){
                Toast.makeText(baseContext, "Choose another name", Toast.LENGTH_SHORT,).show()}
            else{
                supportFragmentManager.beginTransaction().add(R.id.loadingfragmentviev2, LoadingFragment()).commit()
                auth.createUserWithEmailAndPassword(tgmail, tpass).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            //получяем айди пользователя
                            val uid = user?.uid ?: return@addOnCompleteListener
                            //создаем словарь с данными пользователя
                            val userData = mapOf(
                                "name" to tname,
                                "email" to tgmail,
                                "photoUrl" to "",
                                "cart" to ""
                            )
                            // Сохраняем профиль в Realtime Database


                            val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(tname).build()
                            user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        db.child("users").child(uid).setValue(userData)
                                        Toast.makeText(this, "Ползователь: ${user.displayName} добавлено", Toast.LENGTH_SHORT).show()
                                        FirebaseAuth.getInstance().signOut()
                                        finishAffinity()
                                        startActivity(Intent(this,  MainActivity::class.java))
                                    }
                            }
                        } else {
                            val exception = task.exception
                            when (exception){
                                is FirebaseAuthUserCollisionException ->{
                                    Toast.makeText(this, "This email is already registered", Toast.LENGTH_SHORT).show()
                                }
                                is FirebaseAuthWeakPasswordException -> {
                                    // Пароль слишком простой
                                    Toast.makeText(this, "The password is too simple", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    // Любая другая ошибка
                                    Toast.makeText(this, "ERROR: ${exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        supportFragmentManager.beginTransaction().remove(supportFragmentManager.findFragmentById(R.id.loadingfragmentviev2)!!).commit()
                        }
                }
            }
        }
    }
}


