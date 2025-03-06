package fr.isen.aliagafuentesjuanandres.isensmartcompanion.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.api.AppDatabase
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.api.Interaction
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.api.getGeminiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * MainScreen - Pantalla principal de chat con el asistente
 *
 * Esta pantalla permite al usuario interactuar con el asistente inteligente mediante
 * un chat. Es la interfaz principal de la aplicación donde ocurren las conversaciones.
 *
 * Funcionamiento:
 * - Permite al usuario enviar mensajes de texto al asistente de IA
 * - Muestra las respuestas generadas por el modelo Gemini
 * - Almacena cada interacción en la base de datos para su historial
 * - Permite borrar toda la conversación actual
 *
 * Componentes principales:
 * 1. Barra superior (TopAppBar) con título y botón para borrar la conversación
 * 2. Sección de cabecera con logo y título de ISEN YNCREA MEDITERRANEE
 * 3. Área de mensajes que muestra el historial de la conversación actual
 * 4. Campo de texto inferior para introducir nuevas consultas
 *
 * La interfaz de chat:
 * - Los mensajes del usuario aparecen a la derecha con fondo morado
 * - Las respuestas del asistente aparecen a la izquierda con fondo blanco
 * - Cada mensaje incluye una marca de tiempo
 *
 * Aspectos técnicos:
 * - Implementa Material 3 para la interfaz de usuario
 * - Usa estados Compose (mutableStateOf) para gestionar el texto de entrada y los mensajes
 * - Implementa coroutines para operaciones asíncronas (obtener respuestas API y DB)
 * - Utiliza Room Database para persistencia de datos a través de InteractionDao
 * - Aplica degradados y estilos visuales para mejorar la apariencia
 * - Cuando se envía un mensaje, muestra "Loading..." temporalmente hasta recibir la respuesta
 *
 * Esta pantalla está diseñada para proporcionar una experiencia de usuario intuitiva
 * y visualmente atractiva, centrada en la comunicación con el asistente.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current

    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    var messages by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }

    // Get DB and DAO
    val database = AppDatabase.getDatabase(context)
    val interactionDao = database.interactionDao()

    // Time formatter for chat bubble timestamps
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    // Scaffold provides a TopAppBar and a content area
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Isen Smart Companion",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                },
                actions = {
                    // "Clear conversation" button
                    IconButton(onClick = {
                        // Clear from UI
                        messages = emptyList()
                        // Clear from DB (using deleteAllInteractions instead of deleteAll)
                        CoroutineScope(Dispatchers.IO).launch {
                            interactionDao.deleteAllInteractions()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear conversation",
                            tint = Color.White
                        )
                    }
                },
                // Use topAppBarColors instead of TopAppBarDefaults.smallTopAppBarColors
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Example vertical gradient
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE7E9F0),
                            Color(0xFFC5C8D1)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
                .padding(paddingValues)
        ) {
            // ISEN YNCREA MEDITERRANEE title and logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Placeholder image for ISEN logo
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                color = Color(0xFF6200EE),
                                shape = MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ISEN",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ISEN YNCREA MEDITERRANEE title
                    Text(
                        text = "ISEN YNCREA MEDITERRANEE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color(0xFF6200EE)
                    )
                }
            }

            // A title or intro text
            Text(
                text = "Chat with your Smart Companion below!",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            // HorizontalDivider replaces the old Divider in Material 3
            HorizontalDivider(
                color = Color(0xFF6200EE),
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            // Chat messages
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                reverseLayout = false
            ) {
                items(messages) { (userText, aiResponse) ->
                    // User message bubble (on the right)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(bottom = 4.dp)
                                .background(
                                    color = Color(0xFFBB86FC),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(8.dp)
                        ) {
                            Column {
                                Text(
                                    text = "You",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = userText,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = timeFormatter.format(Date()),
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }


                        // Author : JuanAndrésAliagaFuentes



                        // AI message bubble (on the left)
                        Box(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .background(
                                    color = Color.White,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(8.dp)
                        ) {
                            Column {
                                Text(
                                    text = "AI",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF6200EE),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = aiResponse,
                                    color = Color.Black,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = timeFormatter.format(Date()),
                                    color = Color.Black.copy(alpha = 0.5f),
                                    fontSize = 12.sp,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            // Bottom input row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFEFEF))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    placeholder = { Text("Ask something...") },
                    // Updated to use colors() instead of textFieldColors
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color(0xFF6200EE)
                    )
                )

                // Send button
                IconButton(
                    onClick = {
                        val inputText = userInput.text
                        if (inputText.isNotEmpty()) {
                            // Reset input
                            userInput = TextFieldValue("")
                            // Show "Loading..." in the UI
                            messages = messages + (inputText to "Loading...")

                            // Launch a coroutine to fetch AI response
                            CoroutineScope(Dispatchers.Main).launch {
                                // Removed Elvis operator since getGeminiResponse can return null
                                val responseText = getGeminiResponse(inputText)
                                    ?: "No response available"

                                // Replace the "Loading..." with actual text
                                messages = messages.dropLast(1) + (inputText to responseText)

                                // Save to DB
                                val interaction = Interaction(
                                    question = inputText,
                                    answer = responseText,
                                    date = System.currentTimeMillis()
                                )
                                CoroutineScope(Dispatchers.IO).launch {
                                    interactionDao.insertInteraction(interaction)
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .padding(4.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Send",
                        tint = Color(0xFF6200EE)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}