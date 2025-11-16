package com.example.myapplication

import Product
import ProductAdapter
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var searchInput: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter

    private val productList = mutableListOf<Product>()
    private val db = FirebaseDatabase.getInstance().getReference("goods")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        searchInput = view.findViewById(R.id.search_input)
        recyclerView = view.findViewById(R.id.search_recycler)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductAdapter(productList) { product ->
            activity?.startActivity(Intent(requireContext(), ProductOptions::class.java))
            Test.name = product.name
            Test.price = product.price
            Test.url = product.imageUrl.toString()
            Test.popular = product.popular?.toInt()!!
        }
        recyclerView.adapter = adapter

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                search(text.toString().trim())
            }
        })
    }

    private fun search(query: String) {
        if (query.isEmpty()) {
            productList.clear()
            adapter.notifyDataSetChanged()
            return
        }

        val q = query.lowercase()

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                var count = 0
                val maxResults = 4

                for (child in snapshot.children) {
                    if (count >= maxResults) break // Останавливаемся после 4 элементов

                    val name = child.key ?: ""
                    val price = child.child("price").getValue(String::class.java) ?: "0"
                    val photo = child.child("photo").getValue(String::class.java) ?: ""
                    val popular = child.child("popular").getValue(Int::class.java) ?: 0
                    val des = child.child("des").getValue(String::class.java) ?: ""

                    // Проверяем, содержится ли запрос в названии или описании
                    if (name.lowercase().contains(q) || des.lowercase().contains(q)) {
                        productList.add(Product(name, price, photo, popular))
                        count++
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибки
            }
        })
    }



}