package api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import ui.theme.EventItem

// Define the base URL
class NetworkManager {
    companion object {
        private const val BASE_URL = "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/"

        val api: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // Converts JSON to Kotlin objects
                .build()
                .create(ApiService::class.java)
        }
    }
}

// Define API service interface
interface ApiService {
    @GET("events.json")
    suspend fun getEvents(): List<EventItem>
}
