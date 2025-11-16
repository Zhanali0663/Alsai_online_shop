package com.example.myapplication

import android.app.Application
import com.bumptech.glide.Glide
import kotlin.concurrent.thread

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Запускаем очистку дискового кеша в фоновом потоке.
        // Это нужно, чтобы не замедлять запуск приложения.
        thread {
            Glide.get(this).clearDiskCache()
        }
    }
}