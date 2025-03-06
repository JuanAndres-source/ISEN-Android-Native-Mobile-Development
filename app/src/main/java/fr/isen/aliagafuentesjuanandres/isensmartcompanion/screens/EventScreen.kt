package fr.isen.aliagafuentesjuanandres.isensmartcompanion.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.api.RetrofitInstance
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.model.EventModel
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.activity.EventDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Pantalla principal de eventos
 *
 * Esta pantalla muestra una lista de eventos obtenidos desde una API.
 * Permite buscar, ordenar y ver detalles de cada evento individual.
 *
 * La interfaz está compuesta por un encabezado con título y botones de acción,
 * una barra de búsqueda opcional, opciones de ordenación, y una lista
 * de tarjetas de eventos que muestran información resumida.
 *
 * @param navController Controlador de navegación para moverse entre pantallas
 */
@Composable
fun EventsScreen(navController: NavHostController) {
    // Obtiene el contexto actual para lanzar actividades
    val context = LocalContext.current

    // Variables de estado para manejar los datos y la UI
    var eventModels by remember { mutableStateOf<List<EventModel>>(emptyList()) }          // Lista completa de eventos
    var filteredEventModels by remember { mutableStateOf<List<EventModel>>(emptyList()) }  // Lista filtrada según búsqueda
    var isLoading by remember { mutableStateOf(true) }                                    // Estado de carga
    var errorMessage by remember { mutableStateOf("") }                                   // Mensaje de error si existe
    var searchQuery by remember { mutableStateOf("") }                                    // Texto de búsqueda actual
    var showSearchBar by remember { mutableStateOf(false) }                              // Controla visibilidad de barra búsqueda
    var showSortOptions by remember { mutableStateOf(false) }                           // Controla visibilidad opciones ordenación
    var refreshTrigger by remember { mutableStateOf(0) }                                // Contador para forzar recarga

    // Definición de colores personalizados para mantener consistencia visual
    val purplePrimary = Color(0xFFBB86FC)  // Color primario púrpura
    val purpleDark = Color(0xFF6200EE)     // Variante oscura del púrpura
    val darkBackground = Color(0xFF121212) // Fondo oscuro principal
    val darkSurface = Color(0xFF1E1E1E)    // Superficie oscura secundaria

    // Lista de emojis únicos para asignar a cada evento y mejorar identificación visual
    val emojiList = listOf("🔥", "🎉", "🎶", "⚽", "🏆", "📚", "🚀", "🎬", "🖼️", "🍿")

    // Efecto que se ejecuta cuando se carga la pantalla o se solicita refresco
    // Obtiene los datos de eventos desde la API
    LaunchedEffect(refreshTrigger) {
        isLoading = true
        errorMessage = ""

        RetrofitInstance.retrofitService.getEvents().enqueue(object : Callback<List<EventModel>> {
            // Maneja respuesta exitosa de la API
            override fun onResponse(call: Call<List<EventModel>>, response: Response<List<EventModel>>) {
                if (response.isSuccessful) {
                    eventModels = response.body() ?: emptyList()
                    filteredEventModels = eventModels
                } else {
                    errorMessage = "❌ Failed to fetch events"
                }
                isLoading = false
            }

            // Maneja errores de conexión o servidor
            override fun onFailure(call: Call<List<EventModel>>, t: Throwable) {
                errorMessage = "⚠️ Error: ${t.localizedMessage}"
                isLoading = false
            }
        })
    }

    // Efecto para filtrar eventos cuando cambia el texto de búsqueda o la lista de eventos
    LaunchedEffect(searchQuery, eventModels) {
        filteredEventModels = if (searchQuery.isBlank()) {
            eventModels  // Si la búsqueda está vacía, muestra todos los eventos
        } else {
            // Filtra eventos que contengan el texto en título o descripción
            eventModels.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        (it.description?.contains(searchQuery, ignoreCase = true) ?: false)
            }
        }
    }

    // Contenedor principal con fondo degradado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(darkSurface, darkBackground)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // ---- ENCABEZADO ----
            // Fila con título principal y botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🗓️ EVENTS",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                // Botón de búsqueda con forma circular y color primario
                IconButton(
                    onClick = { showSearchBar = !showSearchBar },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(purplePrimary)
                ) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botón de ordenación con forma circular y color primario
                IconButton(
                    onClick = { showSortOptions = !showSortOptions },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(purplePrimary)
                ) {
                    Icon(
                        Icons.Filled.Sort,
                        contentDescription = "Sort",
                        tint = Color.White
                    )
                }
            }

            // ---- BARRA DE BÚSQUEDA ----
            // Visible solo cuando showSearchBar es true, con animación
            AnimatedVisibility(
                visible = showSearchBar,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut()
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search events...", color = Color.White.copy(alpha = 0.6f)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = purplePrimary,
                        unfocusedBorderColor = purplePrimary.copy(alpha = 0.7f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = purplePrimary
                    ),
                    singleLine = true
                )
            }

            // ---- OPCIONES DE ORDENACIÓN ----
            // Visible solo cuando showSortOptions es true, con animación
            AnimatedVisibility(
                visible = showSortOptions,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            // Ordena alfabéticamente ascendente
                            filteredEventModels = filteredEventModels.sortedBy { it.title }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = purpleDark)
                    ) {
                        Text("Sort A-Z")
                    }

                    Button(
                        onClick = {
                            // Ordena alfabéticamente descendente
                            filteredEventModels = filteredEventModels.sortedByDescending { it.title }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = purpleDark)
                    ) {
                        Text("Sort Z-A")
                    }
                }
            }

            // Línea separadora con color primario
            Divider(
                color = purplePrimary,
                thickness = 2.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // ---- ÁREA PRINCIPAL DE CONTENIDO ----
            // Muestra carga, error o lista de eventos según estado
            Box(modifier = Modifier.weight(1f)) {
                when {
                    // Estado de carga: muestra indicador circular
                    isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = purplePrimary)
                        }
                    }
                    // Estado de error: muestra mensaje y botón para reintentar
                    errorMessage.isNotEmpty() -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )

                            Button(
                                onClick = { refreshTrigger++ },
                                colors = ButtonDefaults.buttonColors(containerColor = purplePrimary)
                            ) {
                                Text("Try Again")
                            }
                        }
                    }
                    // No hay resultados para la búsqueda actual
                    filteredEventModels.isEmpty() && searchQuery.isNotEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No events match your search",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    }
                    // Muestra lista de eventos con tarjetas personalizadas
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(filteredEventModels) { index, event ->
                                // Crea una tarjeta para cada evento, asignando emoji cíclicamente
                                EventCard(
                                    eventModel = event,
                                    emoji = emojiList[index % emojiList.size],
                                    onClick = {
                                        // Al hacer clic, abre detalles del evento en nueva actividad
                                        val gson = Gson()
                                        val intent = Intent(context, EventDetailActivity::class.java)
                                        intent.putExtra("event_json", gson.toJson(event))
                                        context.startActivity(intent)
                                    },
                                    purplePrimary = purplePrimary,
                                    purpleDark = purpleDark
                                )
                            }
                        }
                    }
                }
            }

            // ---- BOTONES DE ACCIÓN INFERIORES ----
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Botón de refresco para cargar eventos nuevamente
                Button(
                    onClick = { refreshTrigger++ },
                    colors = ButtonDefaults.buttonColors(containerColor = purpleDark),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = "Refresh"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Refresh")
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Botón para añadir nuevo evento (funcionalidad pendiente)
                Button(
                    onClick = { /* TODO: Implementar funcionalidad para añadir eventos */ },
                    colors = ButtonDefaults.buttonColors(containerColor = purplePrimary),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Event")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Event")
                    }
                }
            }
        }
    }
}

/**
 * Componente para mostrar una tarjeta de evento individual
 *
 * Cada tarjeta muestra un emoji único, el título del evento,
 * una descripción breve y botones de acción. Tiene efectos
 * visuales como elevación, animación y esquinas redondeadas.
 *
 * @param eventModel Modelo de datos del evento a mostrar
 * @param emoji Emoji decorativo para identificar visualmente el evento
 * @param onClick Función que se ejecuta al hacer clic en la tarjeta
 * @param purplePrimary Color primario para estilizado
 * @param purpleDark Variante oscura del color primario
 */
@Composable
fun EventCard(
    eventModel: EventModel,
    emoji: String,
    onClick: () -> Unit,
    purplePrimary: Color,
    purpleDark: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = purplePrimary.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Fila superior con emoji y título
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Contenedor circular para el emoji
                Box(
                    modifier = Modifier
                        .background(
                            color = purpleDark,
                            shape = CircleShape
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 28.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Título del evento
                Text(
                    text = eventModel.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción del evento (limitada a 2 líneas)
            Text(
                text = eventModel.description ?: "No Description Available",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Botón para compartir (funcionalidad pendiente)
                OutlinedButton(
                    onClick = { /* TODO: Implementar funcionalidad para compartir eventos */ },
                    border = ButtonDefaults.outlinedButtonBorder,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text("Share")
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botón para ver detalles
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purpleDark
                    )
                ) {
                    Text("Details")
                }
            }
        }
    }
}

/**
 * Función de vista previa para visualizar la pantalla en el entorno de desarrollo
 *
 * Permite ver cómo se verá la pantalla sin necesidad de ejecutar la aplicación
 * en un dispositivo o emulador.
 */
@Preview(showBackground = true)
@Composable
fun PreviewEventScreen() {
    EventsScreen(navController = rememberNavController())
}