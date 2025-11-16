package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class LMain : AppCompatActivity() {

    private var homeFragment: HomeFragment? = null
    private var searchFragment: SearchFragment? = null
    // Убираем profileFragment из глобальных переменных, так как он будет временным
    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lmain)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val naviga = findViewById<BottomNavigationView>(R.id.LMbottomNavi)

        // 👇 создаём HomeFragment только один раз
        if (homeFragment == null) {
            homeFragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_container, homeFragment!!, "HOME") // Добавляем тег
                .commit()
            activeFragment = homeFragment
        }

        naviga.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item1 -> {
                    // Используем существующий HomeFragment
                    switchFragment(homeFragment ?: HomeFragment().also { homeFragment = it })
                    true
                }
                R.id.item2 -> {
                    // Используем существующий SearchFragment
                    if (searchFragment == null) searchFragment = SearchFragment()
                    switchFragment(searchFragment!!)
                    true
                }
                R.id.item3 -> {
                    // 👇 Всегда создаем НОВЫЙ экземпляр ProfileFragment
                    switchFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun switchFragment(target: Fragment) {
        if (target == activeFragment && target !is ProfileFragment) return

        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)

        // Скрываем или удаляем предыдущий фрагмент
        activeFragment?.let { current ->
            if (current is ProfileFragment) {
                transaction.remove(current) // Удаляем старый ProfileFragment
            } else {
                transaction.hide(current)   // Скрываем остальные
            }
        }

        // Добавляем или показываем новый фрагмент
        if (target.isAdded) {
            transaction.show(target)
        } else {
            // Добавляем новый фрагмент. Даем уникальный тег для ProfileFragment, чтобы избежать ошибок.
            val tag = if (target is ProfileFragment) "PROFILE_${System.currentTimeMillis()}" else target::class.java.name
            transaction.add(R.id.frame_container, target, tag)
        }

        transaction.commit()
        activeFragment = target
    }
}
