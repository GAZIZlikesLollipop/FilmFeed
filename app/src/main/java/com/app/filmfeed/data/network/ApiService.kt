package com.app.filmfeed.data.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(): HttpClient {
    return HttpClient(Android) { // Здесь указываем Android-движок
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
        install(Logging) { // <-- Теперь 'Logging' должен быть найден
            logger = object : Logger { // <-- Теперь 'logger' должен быть найден
                override fun log(message: String) { // <-- 'log' теперь переопределяет метод Ktor.Logger
                    Log.d("KtorClient", message) // Используем Android Logcat
                }
            }
            level = LogLevel.ALL // <-- 'level' теперь должен быть найден
        }
        // Здесь могут быть и другие общие плагины, например, таймауты, кэширование
         install(HttpTimeout) {
            requestTimeoutMillis = 15_000
         }
    }
}

class ApiService(private val httpClient: HttpClient){
    private val BASE_URL = "http://192.168.1.9:8080"
//    private val BASE_URL = "http://10.0.2.2:8080"
//    private val BASE_URL = "http://192.168.1.15:8080"
    suspend fun getMovies(): List<Movie> {
        return httpClient.get("$BASE_URL/movies").body()
    }
}