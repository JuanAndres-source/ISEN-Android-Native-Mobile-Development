package com.example.tutoemeric.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class GeminiViewModel(modelName: String, apiKey: String) : ViewModel() {

    private val generativeModel = GenerativeModel(
        modelName = modelName,
        apiKey = apiKey
    )

    fun generateTextResponse(inputText: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = generativeModel.generateContent(inputText)
                onResult(response.text ?: "Answer not found")
            } catch (e: Exception) {
                onResult("Error: ${e.localizedMessage}")
            }
        }
    }
}
