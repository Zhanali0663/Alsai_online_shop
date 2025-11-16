package com.example.myapplication

import Product

import ProductAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.Nullable

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var lastpopular2: Int? = null
    private var firsttime: String? = null
    private lateinit var database: DatabaseReference
    private lateinit var database2: Query
    private lateinit var text3: TextView
    private lateinit var database3: DatabaseReference
    private var lastKey: String? = null
    private var lastKey2: String? = null
    private var lastKey3: String? = null

    private lateinit var adapter: ProductAdapter
    private lateinit var adapter2: ProductAdapter
    private lateinit var adapter3: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView
    private lateinit var loadingLayout: View
    private lateinit var productList: MutableList<Product>
    private lateinit var productList2: MutableList<Product>
    private lateinit var productList3: MutableList<Product>




    private fun loadData(parag : Int) {
        loadingLayout.visibility = View.VISIBLE
        loadingLayout.setOnTouchListener { _, _ -> true }
                                    //FIRST
        if (parag == 1) {
            // формируем запрос
            var query = database.orderByKey().limitToFirst(5)
            if (lastKey != null) {
                loadingLayout
                query = database.orderByKey().startAfter(lastKey!!).limitToFirst(5)
                //{query = database.orderByChild("popular")}
            }


            // 3. Загружаем данные
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!isAdded) return // Проверка, чтобы избежать сбоев

                    // Проходим по каждому товару и добавляем в список
                    val childrenList = snapshot.children.toList()
                    var q = childrenList.size


                    // Удаляем старый "ADD", если он уже есть в списке
                    productList.removeAll { it.name == "" }


                    var lastLoadedKey: String? = null

                    for (i in 0..q - 1) {
                        val name = childrenList[i].key ?: ""
                        val price = childrenList[i].child("price").getValue(String::class.java) ?: "0"
                        val photoUrl = childrenList[i].child("photo").getValue(String::class.java) ?: ""
                        val popular = childrenList[i].child("popular").getValue(Int::class.java) ?: 0
                        productList.add(Product(name, price + "$", photoUrl, popular))
                        lastLoadedKey = name // запоминаем последний ключ
                    }

                    lastKey = lastLoadedKey

                    if (q == 5) {
                        productList.add(
                            Product(
                                "",
                                "",
                                R.drawable.add,
                                1488
                            )
                        )
                    }
                    adapter.notifyDataSetChanged() // Обновляем отображение
                    loadingLayout.visibility = View.GONE

                }

                override fun onCancelled(error: DatabaseError) {
                    // Обработка ошибок
                }
            })
        }
                                    //SECOND
        else if (parag == 2){
            // формируем запрос
            var query = database2.limitToFirst(5)
            if (lastKey2 != null) {
                loadingLayout
                //query = database.orderByKey().startAfter(lastKey!!).limitToFirst(5)
                query = database2.startAfter(lastpopular2!!.toDouble(), lastKey2!!).limitToFirst(5)
            }


            // 3. Загружаем данные
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!isAdded) return // Проверка, чтобы избежать сбоев

                    // Проходим по каждому товару и добавляем в список
                    val childrenList = snapshot.children.toList()
                    var q = childrenList.size


                    // Удаляем старый "ADD", если он уже есть в списке
                    productList2.removeAll { it.name == "" }


                    var lastLoadedKey: String? = null
                    var lastPopularValue: Int? = null

                    for (i in 0..q - 1) {
                        val name = childrenList[i].key ?: ""
                        val price = childrenList[i].child("price").getValue(String::class.java) ?: "0"
                        val photoUrl = childrenList[i].child("photo").getValue(String::class.java) ?: ""
                        val popular = childrenList[i].child("popular").getValue(Int::class.java)
                        productList2.add(Product(name, price + "$", photoUrl, popular))
                        lastLoadedKey = name // запоминаем последний ключ
                        lastPopularValue = popular
                    }
                    lastpopular2 = lastPopularValue
                    lastKey2 = lastLoadedKey

                    if (q == 5) {
                        productList2.add(Product("", "", R.drawable.add, 1488))
                    }
                    adapter2.notifyDataSetChanged() // Обновляем отображение
                    loadingLayout.visibility = View.GONE

                }

                override fun onCancelled(error: DatabaseError) {
                    // Обработка ошибок
                }
            })
        }
                                    //THIRD
        else {
            database3.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val typeKeys = snapshot.children.mapNotNull { it.key }
                    if (typeKeys.isEmpty()) {
                        loadingLayout.visibility = View.GONE
                        return
                    }
                    var typeRand: String = ""
                    if (firsttime == null){
                        typeRand = typeKeys.random()
                        firsttime = typeRand
                    }else{typeRand = firsttime.toString()}

                    val fordb = FirebaseDatabase.getInstance().getReference("type").child(typeRand)



                    fordb.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            // формируем запрос
                            var query = fordb.orderByKey().limitToFirst(5)
                            if (lastKey3 != null) {
                                loadingLayout
                                query = fordb.orderByKey().startAfter(lastKey3!!).limitToFirst(5)
                                //{query = database.orderByChild("popular")}
                            }


                            // 3. Загружаем данные
                            query.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (!isAdded) return // Проверка, чтобы избежать сбоев

                                    // Проходим по каждому товару и добавляем в список
                                    val childrenList = snapshot.children.toList()
                                    var q = childrenList.size


                                    // Удаляем старый "ADD", если он уже есть в списке
                                    productList3.removeAll { it.name == "" }

                                    val newList = mutableListOf<Product>()
                                    var lastLoadedKey: String? = null
                                    var remaining = q
                                    for (i in 0..q - 1) {

                                        val fordbch = FirebaseDatabase.getInstance().getReference("goods/"+childrenList[i].key)
                                        fordbch.addListenerForSingleValueEvent(object : ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {

                                                val name = childrenList[i].key ?: ""
                                                val price = snapshot.child("price").getValue(String::class.java) ?: "0"
                                                val photoUrl = snapshot.child("photo").getValue(String::class.java) ?: ""
                                                val popular = snapshot.child("popular").getValue(Int::class.java) ?: 0
                                                newList.add(Product(name, price + "$", photoUrl, popular))
                                                lastLoadedKey = name // запоминаем последний ключ
                                                remaining--

                                                if (remaining == 0) {
                                                    lastKey3 = lastLoadedKey


                                                    requireActivity().runOnUiThread {
                                                        productList3.addAll(newList)
                                                        adapter.notifyDataSetChanged()
                                                    }


                                                    if (q == 5) {
                                                        productList3.add(Product("", "", R.drawable.add, 1488))
                                                    }
                                                    adapter3.notifyDataSetChanged()
                                                    loadingLayout.visibility = View.GONE
                                                    text3.text = firsttime?.capitalize()
                                                }



                                            }
                                            override fun onCancelled(error: DatabaseError) {
                                                remaining--
                                                if (remaining == 0) {
                                                    lastKey3 = lastLoadedKey
                                                    if (q == 5) {
                                                        productList3.add(Product("", "", R.drawable.add, 1488))
                                                    }
                                                    adapter3.notifyDataSetChanged()
                                                    loadingLayout.visibility = View.GONE
                                                }
                                            }

                                        })

                                    }

                                    lastKey3 = lastLoadedKey



                                    loadingLayout.visibility = View.GONE

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Обработка ошибок
                                }
                            })
                        }
                        override fun onCancelled(error: DatabaseError) {}

                    })

                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text3 = view.findViewById(R.id.FHtype)
        recyclerView3 = view.findViewById(R.id.recyccler3)
        recyclerView2 = view.findViewById(R.id.recyccler2)
        loadingLayout = view.findViewById(R.id.FHIL)
        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false) // или другой LayoutManager по вашему выбору
        recyclerView3.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // 1. Создаем пустой список для товаров
        productList = mutableListOf()
        productList2 = mutableListOf()
        productList3 = mutableListOf()
        // 2. Создаем адаптер с этим пустым списком
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

        adapter2 = ProductAdapter(productList2) { product ->
            if (product.name == ""){
                loadData(2)
            }else{
                activity?.startActivity(Intent(requireContext(), ProductOptions::class.java))
                Test.name = product.name
                Test.price = product.price
                Test.url = product.imageUrl.toString()
                Test.popular = product.popular?.toInt()!!
            }
        }

        adapter3 = ProductAdapter(productList3) { product ->
            if (product.name == ""){
                loadData(3)
            }else{
                activity?.startActivity(Intent(requireContext(), ProductOptions::class.java))
                Test.name = product.name
                Test.price = product.price
                Test.url = product.imageUrl.toString()
                Test.popular = product.popular?.toInt()!!
            }
        }


        // 3. Устанавливаем адаптер в RecyclerView
        recyclerView.adapter = adapter
        recyclerView2.adapter = adapter2
        recyclerView3.adapter = adapter3


        database = FirebaseDatabase.getInstance().getReference("goods")
        database2 = FirebaseDatabase.getInstance().getReference("goods").orderByChild("popular")
        database3 = FirebaseDatabase.getInstance().getReference("type")



        adapter.notifyDataSetChanged()
        adapter2.notifyDataSetChanged()
        adapter3.notifyDataSetChanged()
        loadingLayout.visibility = View.GONE
        loadData(1)
        loadData(2)
        loadData(3)

    }
}
