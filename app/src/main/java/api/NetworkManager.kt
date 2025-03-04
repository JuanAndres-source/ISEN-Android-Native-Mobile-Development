package api.NetworkManager

import fr.isen.aliagafuentesjuanandres.isensmartcompanion.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private const val BASE_URL = "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Converts JSON to Kotlin objects
            .build()
            .create(ApiService::class.java)
    }

    suspend fun testApiCall() {
        try {
            val response = api.getEvents()
            println("API Response: $response")
        } catch (e: Exception) {
            println("API Error: ${e.message}")
        }
    }
}

