package api

import com.google.firebase.Firebase
import com.google.firebase.vertexai.GenerativeModel
import com.google.firebase.vertexai.vertexAI

class AIService {
    private val generativeModel: GenerativeModel = Firebase.vertexAI.generativeModel("gemini-2.0-flash")

    fun getModel(): GenerativeModel {
        return generativeModel
    }
}
