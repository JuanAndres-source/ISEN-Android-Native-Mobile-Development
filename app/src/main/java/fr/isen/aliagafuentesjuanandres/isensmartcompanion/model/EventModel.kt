package fr.isen.aliagafuentesjuanandres.isensmartcompanion.model

// Esta clase define el modelo de datos para los eventos en nuestra aplicación
// Utilizamos una data class de Kotlin que nos proporciona automáticamente
// funciones como equals(), hashCode(), toString() y copy()
data class EventModel(
    val id: String,         // Identificador único para cada evento
    val title: String,      // Título descriptivo del evento
    val description: String, // Descripción detallada del evento
    val date: String,       // Fecha cuando ocurrirá el evento (en formato string)
    val location: String,   // Ubicación física donde se realizará el evento
    val category: String    // Categoría a la que pertenece el evento para facilitar su clasificación
)