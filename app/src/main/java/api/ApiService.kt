package fr.isen.aliagafuentesjuanandres.isensmartcompanion.api
import models.EventModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("events.json")
    suspend fun getEvents(): List<EventModel> // Change Call<> to suspend function
}