package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class LoadingFragment : Fragment(R.layout.fragmend_loading) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnTouchListener { _, _ -> true }
    }
}