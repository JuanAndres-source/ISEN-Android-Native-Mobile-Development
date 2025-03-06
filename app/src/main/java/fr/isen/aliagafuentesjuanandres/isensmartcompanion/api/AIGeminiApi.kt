package fr.isen.aliagafuentesjuanandres.isensmartcompanion.api

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Clave API utilizada para autenticar las solicitudes al modelo de IA
val apiKey = "AIzaSyCt1VfuIyIkXjhZJW_EkPG8U2BJA1WZvPk"

/**
 * Función suspendida que envía una consulta a la IA de Google Gemini y devuelve la respuesta generada.
 * Se ejecuta en un hilo de entrada/salida (IO) para no bloquear el hilo principal.
 *
 * @param userInput El texto ingresado por el usuario para ser procesado por la IA.
 * @return La respuesta generada por la IA o un mensaje de error en caso de fallo.
 */
suspend fun getGeminiResponse(userInput: String): String {
    return withContext(Dispatchers.IO) { // Ejecutamos la operación en un hilo secundario para eficiencia
        try {
            val model = GenerativeModel(
                modelName = "gemini-1.5-flash", // Especificamos el modelo de IA a utilizar
                apiKey = apiKey // Proporcionamos la clave API para autenticar la solicitud
            )
            val response = model.generateContent(userInput) // Enviamos el texto del usuario al modelo y obtenemos la respuesta
            response.text ?: "No response from AI" // Si la respuesta es nula, devolvemos un mensaje por defecto
        } catch (e: Exception) {
            "Error: ${e.message}" // En caso de error, devolvemos el mensaje de error
        }
    }
}
