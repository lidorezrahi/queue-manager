package com.example.myapplication

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

// Simplified NetworkResult for the POC
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class NetworkError(val message: String) : NetworkResult<Nothing>()
    object ServerError : NetworkResult<Nothing>()
}

class NetworkManager(val client: OkHttpClient) {

    @PublishedApi
    internal val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    // Simplified GET
    suspend inline fun <reified T> get(path: String): NetworkResult<T> {
        val request = Request.Builder()
            .url("https://api.example.com$path")
            .get()
            .build()

        return executeCall(request)
    }

    // Simplified PUT
    suspend inline fun <reified R, reified T> put(path: String, body: R): NetworkResult<T> {
        val bodyString = json.encodeToString(body)
        val requestBody = bodyString.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://api.example.com$path")
            .put(requestBody)
            .build()

        return executeCall(request)
    }

    // Helper to execute the call - Must be inline to be used by other inline functions
    @PublishedApi
    internal inline fun <reified T> executeCall(request: Request): NetworkResult<T> {
        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.string() ?: ""

                // Handle Unit return type (common for PUT/POST)
                if (T::class == Unit::class) {
                    NetworkResult.Success(Unit as T)
                } else {
                    NetworkResult.Success(json.decodeFromString<T>(body))
                }
            } else {
                NetworkResult.ServerError
            }
        } catch (e: IOException) {
            NetworkResult.NetworkError(e.message ?: "No Internet")
        }
    }
}