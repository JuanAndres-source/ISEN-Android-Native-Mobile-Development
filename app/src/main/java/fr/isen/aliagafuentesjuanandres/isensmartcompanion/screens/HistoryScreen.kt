package fr.isen.aliagafuentesjuanandres.isensmartcompanion.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.QuestionAnswer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.*
import androidx.compose.foundation.shape.RoundedCornerShape
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.api.InteractionDao
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.api.Interaction
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.ui.theme.PurplePrimary
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.ui.theme.PurpleLight
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.ui.theme.PurpleDark
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.ui.theme.SurfaceLight

/**
 * HistoryScreen - Pantalla de historial de conversaciones
 *
 * Esta pantalla muestra un historial completo de las interacciones previas entre el usuario y el asistente.
 *
 * Funcionamiento:
 * - Muestra una lista cronológica de todas las conversaciones guardadas
 * - Permite eliminar conversaciones individualmente
 * - Ofrece la opción de borrar todo el historial con confirmación
 * - Muestra la fecha y hora de cada interacción
 *
 * Componentes principales:
 * 1. Cabecera con título y subtítulo sobre un fondo degradado púrpura
 * 2. Contador de entradas que muestra el número total de conversaciones
 * 3. Botón para eliminar todo el historial con diálogo de confirmación
 * 4. Lista desplazable (LazyColumn) que contiene las tarjetas de conversación
 *
 * Cada tarjeta de conversación (HistoryItem) contiene:
 * - Fecha y hora de la interacción
 * - Botón para eliminar esa conversación específica
 * - Sección "You" con la pregunta del usuario
 * - Sección "Assistant" con la respuesta del asistente
 *
 * Aspectos técnicos:
 * - Utiliza Jetpack Compose para la interfaz de usuario
 * - Implementa Room Database a través del InteractionDao para gestionar el almacenamiento
 * - Usa coroutines para las operaciones de base de datos en segundo plano
 * - Aplica un diseño con tarjetas, sombras y bordes redondeados para una apariencia moderna
 * - Maneja confirmaciones mediante diálogos para acciones destructivas
 * - Implementa una paleta de colores coherente basada en tonos púrpura
 *
 * El diseño es responsive y sigue las directrices de Material Design 3 con una estética
 * personalizada usando degradados y elementos visuales distintivos.
 */
@Composable
fun HistoryScreen(interactionDao: InteractionDao) {
    val interactions by interactionDao.getAllInteractions().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(SurfaceLight, Color.White)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Title Section with curved background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(PurplePrimary, PurpleDark)
                        )
                    )
                    .padding(vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Conversation History",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Your past interactions",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Header and Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = PurpleLight.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.History,
                            contentDescription = null,
                            tint = PurplePrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Your Past Conversations",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.weight(1f))

                // Entry count
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = PurpleLight.copy(alpha = 0.15f)
                    )
                ) {
                    Text(
                        text = "${interactions.size} entries",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = PurplePrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Clear History Button
            Button(
                onClick = { showDeleteConfirmation = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9E0059) // Different accent color for deletion
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.DeleteSweep,
                    contentDescription = "Clear all history",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Clear All Conversations",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            // Interactions List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(interactions) { interaction ->
                    HistoryItem(
                        interaction = interaction,
                        dateFormatter = dateFormatter,
                        onDelete = {
                            coroutineScope.launch(Dispatchers.IO) {
                                interactionDao.deleteInteraction(interaction)
                            }
                        }
                    )
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White,
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = null,
                        tint = Color(0xFF9E0059),
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Clear History",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 22.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Text(
                    text = "Are you sure you want to delete all conversation history? This action cannot be undone.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            interactionDao.deleteAllInteractions()
                        }
                        showDeleteConfirmation = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF9E0059)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Delete All", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteConfirmation = false },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PurplePrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, PurplePrimary)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

/**
 * HistoryItem - Componente para cada elemento del historial
 *
 * Este componente representa una conversación individual en la lista de historial.
 * Muestra la pregunta del usuario y la respuesta del asistente en una tarjeta con
 * formato diferenciado para cada parte. Incluye la fecha/hora de la conversación
 * y un botón para eliminar esta entrada específica.
 *
 * @param interaction Objeto que contiene los datos de la conversación (pregunta, respuesta, fecha)
 * @param dateFormatter Formateador para mostrar la fecha/hora en formato legible
 * @param onDelete Función lambda que se ejecuta cuando se solicita eliminar esta conversación
 */
@Composable
fun HistoryItem(
    interaction: Interaction,
    dateFormatter: SimpleDateFormat,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = PurplePrimary.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with date and delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = PurpleLight.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.QuestionAnswer,
                                contentDescription = null,
                                tint = PurplePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = dateFormatter.format(Date(interaction.date)),
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Question section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PurpleLight.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "You",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurplePrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = interaction.question,
                    fontSize = 15.sp,
                    color = Color.Black.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Answer section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 4.dp)
            ) {
                Text(
                    text = "Assistant",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = interaction.answer,
                    fontSize = 15.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}