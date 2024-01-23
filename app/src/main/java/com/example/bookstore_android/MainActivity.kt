package com.example.bookstore_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore_android.ui.home.HomeScreen
import com.example.bookstore_android.ui.home.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize HomeViewModel
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        setContent {
            // Provide a Compose UI content
            HomeScreen(homeViewModel)
        }
    }
}
