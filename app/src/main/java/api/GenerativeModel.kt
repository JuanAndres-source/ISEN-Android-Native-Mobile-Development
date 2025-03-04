package com.example.tutoemeric.models

import com.google.ai.client.generativeai.GenerativeModel

class GenerativeModel(modelName: String, apiKey: String) {

    private val generativeModel = GenerativeModel(
        modelName = modelName,
        apiKey = apiKey
    )

    suspend fun generateResponse(inputText: String): String {
        return try {
            val response = generativeModel.generateContent(inputText)
            response.text ?: "Answer not found"
        } catch (e: Exception) {
            "Error: ${e.localizedMessage}"
        }
    }
}
