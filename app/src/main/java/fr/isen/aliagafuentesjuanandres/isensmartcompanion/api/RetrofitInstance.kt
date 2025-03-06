package fr.isen.aliagafuentesjuanandres.isensmartcompanion.api

import fr.isen.aliagafuentesjuanandres.isensmartcompanion.model.EventModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.http.GET

// Este es un objeto Singleton que gestiona la instancia de Retrofit
// Lo usamos para tener una única instancia en toda la aplicación
object RetrofitInstance {
    // Utilizamos "by lazy" para inicializar la instancia solo cuando se necesite
    // Esto ahorra recursos y mejora el rendimiento de la app
    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/") // URL base de Firebase
            .addConverterFactory(GsonConverterFactory.create()) // Convertidor Gson para interpretar JSON
            .build()
    }

    // Creamos la interfaz de servicio que usaremos para hacer las llamadas API
    // También inicializada de forma perezosa para optimizar recursos
    val retrofitService: RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }
}

// Interfaz que define los endpoints disponibles en nuestra API
interface RetrofitService {
    @GET("events.json") // Ruta relativa para obtener eventos desde Firebase
    fun getEvents(): Call<List<EventModel>> // Devuelve una lista de eventos del modelo EventModel
}