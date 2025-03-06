package fr.isen.aliagafuentesjuanandres.isensmartcompanion.model

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Pantalla para mostrar el historial de eventos en la aplicación
// Implementa Jetpack Compose para construir la interfaz de usuario declarativa
@Composable
fun HistoryScreen() {
    // Se usa una Column para disponer los elementos verticalmente
    Column(
        modifier = Modifier
            .fillMaxSize()        // Ocupa todo el tamaño disponible
            .wrapContentHeight(), // Ajusta la altura del contenido
        horizontalAlignment = Alignment.CenterHorizontally, // Centra los elementos horizontalmente
        verticalArrangement = Arrangement.Center // Centra los elementos verticalmente
    ) {
        // Título de la pantalla con estilo personalizado
        Text(
            text = "Events History",
            fontSize = 25.sp,         // Tamaño de fuente en 25 scaled pixels
            fontWeight = FontWeight.Bold, // Fuente en negrita para mayor énfasis
        )
    }
}
