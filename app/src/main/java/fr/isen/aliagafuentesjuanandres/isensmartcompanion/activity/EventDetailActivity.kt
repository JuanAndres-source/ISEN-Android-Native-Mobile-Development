package fr.isen.aliagafuentesjuanandres.isensmartcompanion.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit
import androidx.navigation.compose.*
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.api.AlertSettings
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.model.EventModel
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.api.NotificationPreferences
import android.util.Log

/**
 * Actividad que muestra los detalles de un evento específico.
 *
 * Esta actividad recibe un objeto EventModel serializado como JSON a través del intent
 * que la inicia. Muestra toda la información del evento y permite al usuario realizar
 * acciones como añadir el evento al calendario, buscar direcciones, compartir o
 * programar notificaciones.
 */
class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Extraemos el JSON del evento del intent que inició esta actividad
        val jsonEvent = intent.getStringExtra("event_json")

        // Añadimos log para depuración
        Log.d("EventDetail", "Received JSON: $jsonEvent")

        try {
            // Deserializamos el JSON a un objeto EventModel
            // El try-catch protege contra errores de formato en el JSON
            val eventModel = Gson().fromJson(jsonEvent, EventModel::class.java)

            // Configuramos el contenido de la pantalla usando Jetpack Compose
            setContent {
                EventDetailScreen(eventModel = eventModel, onBackPressed = { finish() })
            }
        } catch (e: Exception) {
            // Manejo de error si el JSON no puede ser parseado correctamente
            Log.e("EventDetail", "Error parsing JSON: ${e.message}", e)
            Toast.makeText(this, "Error loading event details", Toast.LENGTH_LONG).show()
            finish() // Cerramos la actividad si no podemos mostrar los datos
        }
    }
}

/**
 * Composable principal que estructura toda la pantalla de detalles del evento.
 *
 * @param eventModel El modelo de datos que contiene toda la información del evento a mostrar
 * @param onBackPressed Callback que se ejecuta cuando el usuario pulsa el botón de volver
 */
@Composable
fun EventDetailScreen(eventModel: EventModel, onBackPressed: () -> Unit) {
    // Variables de estado para controlar componentes interactivos
    var isReminderSet by remember { mutableStateOf(false) } // Estado de la notificación
    var showShareOptions by remember { mutableStateOf(false) } // Visibilidad del diálogo de compartir
    var expandedDescription by remember { mutableStateOf(false) } // Estado de expansión de la descripción
    val context = LocalContext.current
    val notificationPreferences = NotificationPreferences(context)

    // Definición de colores del tema púrpura
    val primaryColor = Color(0xFF6200EE)
    val secondaryColor = Color(0xFF3700B3)
    val surfaceColor = Color(0xFF121212)
    val cardBackgroundColor = Color(0x33BB86FC)
    val descriptionCardColor = Color(0x33BB86FC)
    val dividerColor = Color(0xFFBB86FC)

    // Cargamos el estado de la notificación al iniciar el composable
    LaunchedEffect(eventModel.id) {
        isReminderSet = notificationPreferences.getReminder(eventModel.id)
    }

    // Diseño principal - Fondo con degradado y columna para el contenido
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(surfaceColor, Color.Black),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Sección de cabecera con botón de volver, compartir y toggle de notificación
            HeaderSection(
                isReminderSet = isReminderSet,
                onBackPressed = onBackPressed,
                onReminderToggle = { isToggled ->
                    // Actualizamos el estado visual y guardamos la preferencia
                    isReminderSet = isToggled
                    notificationPreferences.saveReminder(eventModel.id, isToggled)

                    // Mostramos mensaje de confirmación al usuario
                    val message = if (isToggled) "Notification enabled!" else "Notification disabled!"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                    // Si se activa la notificación, programamos el recordatorio
                    if (isToggled) {
                        scheduleNotification(context, eventModel.id)
                    }
                },
                onShareClick = { showShareOptions = true },
                primaryColor = primaryColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Sección de título - Destaca el nombre del evento
            TitleSection(title = eventModel.title, dividerColor = dividerColor)

            Spacer(modifier = Modifier.height(15.dp))

            // Insignia de estado del evento - Indica si está próximo o ya pasó
            EventStatusBadge(eventModel.date)

            Spacer(modifier = Modifier.height(15.dp))

            // Tarjeta de categoría - Muestra la categoría del evento
            CategoryCard(category = eventModel.category, backgroundColor = cardBackgroundColor)

            Spacer(modifier = Modifier.height(20.dp))

            // Sección de información - Muestra fecha y ubicación con iconos
            EventInfoSection(date = eventModel.date, location = eventModel.location, primaryColor = primaryColor)

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta de descripción expandible - Permite mostrar u ocultar el texto completo
            ExpandableDescriptionCard(
                description = eventModel.description,
                backgroundColor = descriptionCardColor,
                isExpanded = expandedDescription,
                onExpandToggle = { expandedDescription = !expandedDescription }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botones de acción - Añadir al calendario y buscar direcciones
            ActionButtons(
                onAddToCalendar = {
                    addEventToCalendar(context, eventModel)
                },
                onFindDirections = {
                    openMapsForLocation(context, eventModel.location)
                },
                primaryColor = primaryColor
            )
        }

        // Diálogo de opciones para compartir - Aparece animado cuando showShareOptions es true
        AnimatedVisibility(
            visible = showShareOptions,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            ShareOptionsDialog(
                eventModel = eventModel,
                onDismiss = { showShareOptions = false },
                context = context
            )
        }
    }
}

/**
 * Sección de cabecera que contiene el botón de volver, el botón de compartir
 * y el control para activar/desactivar notificaciones.
 */
@Composable
fun HeaderSection(
    isReminderSet: Boolean,
    onBackPressed: () -> Unit,
    onReminderToggle: (Boolean) -> Unit,
    onShareClick: () -> Unit,
    primaryColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón de volver - Navega a la pantalla anterior
        Button(
            onClick = onBackPressed,
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back to events",
                tint = Color.White
            )
        }

        Row {
            // Botón de compartir - Muestra el diálogo de opciones para compartir
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share event",
                    modifier = Modifier.size(28.dp),
                    tint = Color.White
                )
            }

            // Toggle de notificación - Cambia entre activado/desactivado
            IconButton(onClick = { onReminderToggle(!isReminderSet) }) {
                Icon(
                    imageVector = if (isReminderSet) Icons.Filled.Notifications else Icons.Filled.NotificationsOff,
                    contentDescription = if (isReminderSet) "Disable notification" else "Enable notification",
                    modifier = Modifier.size(28.dp),
                    tint = if (isReminderSet) Color(0xFFBB86FC) else Color.White
                )
            }
        }
    }
}

/**
 * Sección de título que muestra el nombre del evento de manera destacada
 * y añade un separador debajo.
 */
@Composable
fun TitleSection(title: String, dividerColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0x44000000))
    ) {
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            textAlign = TextAlign.Center
        )
    }

    // Separador visual para delimitar secciones
    Divider(
        color = dividerColor,
        thickness = 2.dp,
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
    )
}

/**
 * Insignia que muestra si el evento es próximo o ya pasó.
 * En una implementación real, compararía la fecha del evento con la actual.
 */
@Composable
fun EventStatusBadge(eventDate: String) {
    // Implementación simple - en una app real se compararían fechas
    val isUpcoming = true

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    ) {
        Card(
            modifier = Modifier.wrapContentSize(),
            colors = CardDefaults.cardColors(
                containerColor = if (isUpcoming) Color(0xFF4CAF50) else Color(0xFFFF9800)
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = if (isUpcoming) "Upcoming Event" else "Past Event",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }
}

/**
 * Tarjeta que muestra la categoría del evento.
 */
@Composable
fun CategoryCard(category: String, backgroundColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Text(
            text = "Category: $category",
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Sección que muestra la fecha y ubicación del evento con iconos.
 */
@Composable
fun EventInfoSection(date: String, location: String, primaryColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x44BB86FC), shape = MaterialTheme.shapes.medium)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Fecha con icono de calendario
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "Date",
                modifier = Modifier.size(24.dp),
                tint = primaryColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = date,
                fontSize = 16.sp,
                color = Color.White
            )
        }

        // Ubicación con icono de localización
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = "Location",
                modifier = Modifier.size(24.dp),
                tint = primaryColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = location,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

/**
 * Tarjeta de descripción expandible que permite mostrar u ocultar el texto completo.
 * Al hacer clic, alterna entre mostrar solo unas líneas o la descripción completa.
 */
@Composable
fun ExpandableDescriptionCard(
    description: String,
    backgroundColor: Color,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onExpandToggle),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Description",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Icono que cambia según el estado expandido/contraído
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Show less" else "Show more",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Texto de descripción que se trunca o expande según el estado
            Text(
                text = description,
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                color = Color.White,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = if (isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Sección de botones de acción: añadir al calendario y buscar direcciones.
 */
@Composable
fun ActionButtons(
    onAddToCalendar: () -> Unit,
    onFindDirections: () -> Unit,
    primaryColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Botón para añadir evento al calendario
        Button(
            onClick = onAddToCalendar,
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Icon(
                imageVector = Icons.Default.AddAlert,
                contentDescription = "Add to calendar",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add to Calendar")
        }

        // Botón para buscar direcciones hacia la ubicación del evento
        Button(
            onClick = onFindDirections,
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsWalk,
                contentDescription = "Find directions",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Directions")
        }
    }
}

/**
 * Diálogo modal que muestra opciones para compartir el evento.
 * Permite compartir por email, SMS u otras aplicaciones.
 */
@Composable
fun ShareOptionsDialog(
    eventModel: EventModel,
    onDismiss: () -> Unit,
    context: Context
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x88000000))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clickable { /* Previene propagación de clics */ },
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Share Event",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Opción para compartir por email
                ShareOption(
                    icon = Icons.Default.Email,
                    text = "Email",
                    onClick = {
                        shareViaEmail(context, eventModel)
                        onDismiss()
                    }
                )

                Divider(color = Color(0x1FFFFFFF), thickness = 1.dp)

                // Opción para compartir por SMS
                ShareOption(
                    icon = Icons.Default.Message,
                    text = "Text Message",
                    onClick = {
                        shareViaSMS(context, eventModel)
                        onDismiss()
                    }
                )

                Divider(color = Color(0x1FFFFFFF), thickness = 1.dp)

                // Opción para compartir mediante otras aplicaciones
                ShareOption(
                    icon = Icons.Default.Share,
                    text = "Other Apps",
                    onClick = {
                        shareEvent(context, eventModel)
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para cancelar y cerrar el diálogo
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

/**
 * Componente reutilizable que representa una opción de compartir en el diálogo.
 */
@Composable
fun ShareOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFBB86FC)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

/**
 * Función de utilidad para añadir el evento al calendario del dispositivo.
 * Crea un intent específico para el calendario y lo lanza si hay una app compatible.
 */
fun addEventToCalendar(context: Context, eventModel: EventModel) {
    val intent = Intent(Intent.ACTION_INSERT)
        .setData(android.provider.CalendarContract.Events.CONTENT_URI)
        .putExtra(android.provider.CalendarContract.Events.TITLE, eventModel.title)
        .putExtra(android.provider.CalendarContract.Events.DESCRIPTION, eventModel.description)
        .putExtra(android.provider.CalendarContract.Events.EVENT_LOCATION, eventModel.location)

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "No calendar app found", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Función de utilidad para abrir un mapa con la ubicación del evento.
 * Intenta primero con Google Maps, y si no está disponible usa el navegador.
 */
fun openMapsForLocation(context: Context, location: String) {
    val encodedLocation = Uri.encode(location)
    val gmmIntentUri = Uri.parse("geo:0,0?q=$encodedLocation")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")

    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        val browserIntent = Intent(Intent.ACTION_VIEW,
            Uri.parse("https://www.google.com/maps/search/?api=1&query=$encodedLocation"))
        context.startActivity(browserIntent)
    }
}

/**
 * Función de utilidad para compartir el evento por email.
 * Prepara un correo con los detalles del evento.
 */
fun shareViaEmail(context: Context, eventModel: EventModel) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Solo apps de email deberían manejar esto
        putExtra(Intent.EXTRA_SUBJECT, "Event: ${eventModel.title}")
        putExtra(Intent.EXTRA_TEXT, """
            Event: ${eventModel.title}
            Date: ${eventModel.date}
            Location: ${eventModel.location}
            Category: ${eventModel.category}
            
            ${eventModel.description}
        """.trimIndent())
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Función de utilidad para compartir el evento por SMS.
 * Prepara un mensaje de texto con los detalles básicos del evento.
 */
fun shareViaSMS(context: Context, eventModel: EventModel) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("smsto:") // Solo apps de SMS deberían manejar esto
        putExtra("sms_body", """
            Check out this event!
            ${eventModel.title}
            Date: ${eventModel.date}
            Location: ${eventModel.location}
        """.trimIndent())
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "No messaging app found", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Función de utilidad para compartir el evento mediante cualquier aplicación compatible.
 * Muestra un selector de apps para compartir texto plano.
 */
fun shareEvent(context: Context, eventModel: EventModel) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, """
            Event: ${eventModel.title}
            Date: ${eventModel.date}
            Location: ${eventModel.location}
            Category: ${eventModel.category}
            
            ${eventModel.description}
        """.trimIndent())
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share event via"))
}

/**
 * Función que programa una notificación para el evento utilizando WorkManager.
 * Crea una tarea de ejecución única con un retraso de 10 segundos (en una app real,
 * este tiempo se calcularía en base a la fecha del evento).
 */
fun scheduleNotification(context: Context, eventId: String) {
    val inputData = workDataOf("eventId" to eventId)

    val notificationWorkRequest: WorkRequest =
        OneTimeWorkRequestBuilder<AlertSettings>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .setInputData(inputData)
            .build()

    WorkManager.getInstance(context).enqueue(notificationWorkRequest)
}