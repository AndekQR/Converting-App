package com.example.convertingapp.ui.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    fun test(): List<String> {
        return listOf<String>("a","b","c")
    }
}