package fr.isen.aliagafuentesjuanandres.isensmartcompanion.api

// Esta clase representa una interacción entre el usuario y el sistema, almacenando las preguntas y respuestas.
// La información se guarda en una base de datos utilizando Room.

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "interaction")  // Define que esta clase se utilizará para crear la tabla "interaction" en la base de datos.
data class Interaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,  // Identificador único para cada interacción, se genera automáticamente.
    @ColumnInfo(name = "question") val question: String,  // Almacena la pregunta realizada por el usuario.
    @ColumnInfo(name = "answer") val answer: String,  // Almacena la respuesta proporcionada por el sistema.
    @ColumnInfo(name = "date") val date: Long  // Almacena la fecha de la interacción en formato de timestamp (milisegundos).
)
