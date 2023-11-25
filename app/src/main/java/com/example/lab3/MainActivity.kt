package com.example.lab3

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.button_get_data)
        button.setOnClickListener { fetchDataFromApi() }
    }

    private fun fetchDataFromApi() {
        val url = URL("https://jsonplaceholder.typicode.com/posts")

        GlobalScope.launch {
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                inputStream.bufferedReader().use {
                    it.lines().forEach { line -> Log.d("API Response", line) }
                }
            }
        }
    }
}
