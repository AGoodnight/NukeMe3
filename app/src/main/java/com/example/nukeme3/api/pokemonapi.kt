package com.example.nukeme3.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import java.util.concurrent.TimeUnit

fun HttpClient(): OkHttpClient{
    return OkHttpClient()
}

fun Request(url:String): Request{
    return Request.Builder().url(url).build()
}
suspend fun getPokemon(name: String): String {
    return withContext(Dispatchers.IO) {
        // Add explicit timeouts to prevent the network call from hanging forever
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
        Log.d("requesting","Requesxting")
        val request = Request.Builder()
            .url("https://pokeapi.co/api/v2/pokemon/$name")
            .build()

        Log.d("requesting","Requesxting1")
        client.newCall(request).execute().use { response ->
            Log.d("requesting","respond ${response.isSuccessful}")
            if (!response.isSuccessful) {
                throw IOException("Server returned error code: ${response.code}")
            }

            // Safely read the body string
            val body = response.body
                ?: throw IOException("Response body was null")


            val b = body.string()
            Log.d("t",b)
            b
        }
    }
}