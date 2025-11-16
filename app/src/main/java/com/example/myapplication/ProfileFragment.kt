package com.example.myapplication

import Product
import ProductAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.launch
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileFragment : Fragment(R.layout.fragment_profile) {


    private lateinit var adapter: ProductAdapter
    private var lastKey: String? = null
    private var isFirstLoad: Boolean = false

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingLayout: View
    private lateinit var productList: MutableList<Product>


    private fun loadData(parag : Int){
        loadingLayout.visibility = View.VISIBLE
        loadingLayout.setOnTouchListener { _, _ -> true }
        // фор  мируем запрос
        var query = database.orderByKey().limitToFirst(5)
        if (lastKey != null) {
            query = database.orderByKey().startAfter(lastKey!!).limitToFirst(5)
            //{query = database.orderByChild("popular")}
        }


        // 3. Загружаем данные
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded) return // Проверка, чтобы избежать сбоев

                val childrenList = snapshot.children.toList()
                val q = childrenList.size

                // Если больше нет товаров, удаляем кнопку "ADD" и выходим
                if (q == 0) {
                    val removed = productList.removeAll { it.name == "" }
                    if (removed) {
                        adapter.notifyDataSetChanged()
                    }
                    loadingLayout.visibility = View.GONE
                    return
                }

                // Сохраняем последний ключ до запуска корутин, чтобы избежать гонки состояний
                val lastLoadedKeyFromSnapshot = childrenList.last().key

                val jobs = mutableListOf<Job>()
                // Используем ConcurrentHashMap для сохранения порядка
                val newProductsMap = java.util.concurrent.ConcurrentHashMap<String, Product>()

                for (childSnapshot in childrenList) {
                    val job = CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val fordbch = FirebaseDatabase.getInstance().getReference("goods/" + childSnapshot.key)
                            val dataSnapshot = fordbch.get().await() // Ждем результат

                            val name = childSnapshot.key ?: ""
                            val price = dataSnapshot.child("price").getValue(String::class.java) ?: "0"
                            val photoUrl = dataSnapshot.child("photo").getValue(String::class.java) ?: ""
                            val popular = dataSnapshot.child("popular").getValue(Int::class.java) ?: 0

                            newProductsMap[name] = Product(name, price + "$", photoUrl, popular)

                        } catch (e: Exception) {
                            // Обработка ошибок, если запрос не удался
                        }
                    }
                    jobs.add(job)
                }

                // Запускаем корутину, которая дождется выполнения всех запросов
                CoroutineScope(Dispatchers.Main).launch {
                    jobs.joinAll() // Ждем, пока все job'ы завершатся

                    if (!isAdded) return@launch // Проверяем, жив ли еще фрагмент

                    // --- Теперь обновляем UI один раз ---

                    // 1. Убираем старую кнопку "ADD"
                    productList.removeAll { it.name == "" }

                    // 2. Собираем товары в правильном порядке
                    val newProducts = childrenList.mapNotNull { newProductsMap[it.key] }
                    productList.addAll(newProducts)

                    // 3. Обновляем ключ для следующей загрузки
                    lastKey = lastLoadedKeyFromSnapshot

                    // 4. Если загрузилась полная пачка, добавляем новую кнопку "ADD"
                    if (q == 5) {
                        productList.add(Product("", "", R.drawable.add, 1488))
                    }

                    // 5. Уведомляем адаптер и скрываем загрузку
                    adapter.notifyDataSetChanged()
                    loadingLayout.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибок
                if (isAdded) {
                    loadingLayout.visibility = View.GONE
                }
            }
        })

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingLayout = view.findViewById(R.id.FPIL)

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


        val name = view.findViewById<TextView>(R.id.FPname)
        if (user?.displayName.toString().length < 11) {
            name?.text = user?.displayName.toString()
        }else{
            name?.text = user?.displayName.toString().substring(0,8) + "..."
        }
        val exit = view.findViewById<Button>(R.id.FPexit)
        exit?.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            activity?.startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
        }

        recyclerView = view.findViewById(R.id.FPrecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        productList = mutableListOf()

        adapter = ProductAdapter(productList) { product ->
            if (product.name == ""){
                loadData(1)
            }else{
                activity?.startActivity(Intent(requireContext(), ProductOptions::class.java))
                Test.name = product.name
                Test.price = product.price
                Test.url = product.imageUrl.toString()
                Test.popular = product.popular?.toInt()!!
            }
        }

        recyclerView.adapter = adapter
        database = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child("cart")
        adapter.notifyDataSetChanged()
        loadData(0)





    }
    override fun onResume() {
        super.onResume()

        // Проверяем, что это не первый запуск фрагмента
        if (isFirstLoad) {
            // Находим BottomNavigationView в родительском Activity (LMain)
            val naviga = activity?.findViewById<BottomNavigationView>(R.id.LMbottomNavi)
            // Программно "нажимаем" на иконку HomeFragment (item1)
            naviga?.selectedItemId = R.id.item1
        } else {
            // Отмечаем, что первый запуск прошел
            isFirstLoad = true
        }
    }



}
