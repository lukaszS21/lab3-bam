package com.example.lab3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("NetworkReceiver", "Network state changed")
            checkNetworkState()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.button_get_data)
        button.setOnClickListener { fetchDataFromApi() }

        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
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

    private fun checkNetworkState() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        Log.d("NetworkState", "Is connected: ${networkInfo?.isConnected}")
        Log.d("NetworkState", "Type: ${networkInfo?.type} ${ConnectivityManager.TYPE_WIFI}")
    }
}
