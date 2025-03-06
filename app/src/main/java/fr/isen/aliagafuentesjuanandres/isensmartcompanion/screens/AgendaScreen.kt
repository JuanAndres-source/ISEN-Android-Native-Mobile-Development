package fr.isen.aliagafuentesjuanandres.isensmartcompanion.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Divider
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.shadow

// Este es un conjunto de colores que usamos en toda la aplicación
// Definimos una paleta de púrpura con sus variantes
val PurplePrimary = Color(0xFF6200EE)  // Color principal púrpura
val PurpleLight = Color(0xFFBB86FC)    // Versión más clara del púrpura
val PurpleDark = Color(0xFF3700B3)     // Versión más oscura del púrpura
val SurfaceLight = Color(0xFFF8F6FD)   // Color de fondo muy claro con toque púrpura

// Modelo para representar un curso académico
data class Course(
    val courseName: String,    // Nombre de la asignatura o clase
    val courseTime: String,    // Horario en que se imparte
    val courseRoom: String     // Aula o sala donde se realiza
)

// Modelo para representar un evento en la agenda
data class AgendaEvent(
    val eventName: String,     // Nombre descriptivo del evento
    val eventDate: String      // Fecha en que ocurre el evento
)

// Componente principal que muestra la pantalla de agenda con cursos y eventos
@Composable
fun AgendaScreen(courses: List<Course>, events: List<AgendaEvent>) {
    // Estado para manejar la lista de cursos que podemos modificar
    val coursesList = remember { mutableStateListOf(*courses.toTypedArray()) }

    // Estados para controlar el diálogo de añadir curso
    var showDialog by remember { mutableStateOf(false) }
    var courseName by remember { mutableStateOf("") }
    var courseRoom by remember { mutableStateOf("") }
    var courseTime by remember { mutableStateOf("") }

    // Contenedor principal con degradado de fondo
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
            // Sección de título con fondo curvo y degradado
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
                        text = "Academic Planner",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Manage your academic schedule",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sección de cursos con encabezado personalizado
            SectionHeader(
                title = "Today's Classes",
                icon = Icons.Rounded.School,
                iconColor = PurplePrimary
            )

            // Lista desplazable de cursos
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(coursesList) { course ->
                    CourseItem(course = course, onDelete = { coursesList.remove(course) })
                }
            }

            // Botón para añadir un nuevo curso
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurplePrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add course",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Add New Class",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            // Sección de eventos próximos
            SectionHeader(
                title = "Upcoming Events",
                icon = Icons.Rounded.CalendarMonth,
                iconColor = PurplePrimary
            )

            // Lista desplazable de eventos
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(events) { event ->
                    EventItem(event = event)
                }
            }
        }
    }

    // Diálogo para añadir un nuevo curso (se muestra solo cuando showDialog es true)
    if (showDialog) {
        AddCourseDialog(
            onDismiss = {
                showDialog = false
                courseName = ""
                courseRoom = ""
                courseTime = ""
            },
            onConfirm = {
                if (courseName.isNotBlank() && courseRoom.isNotBlank() && courseTime.isNotBlank()) {
                    coursesList.add(Course(courseName, courseTime, courseRoom))
                    courseName = ""
                    courseRoom = ""
                    courseTime = ""
                    showDialog = false
                }
            },
            courseName = courseName,
            onCourseNameChange = { courseName = it },
            courseRoom = courseRoom,
            onCourseRoomChange = { courseRoom = it },
            courseTime = courseTime,
            onCourseTimeChange = { courseTime = it }
        )
    }
}

// Componente para los encabezados de sección con icono y botón "Ver todo"
@Composable
private fun SectionHeader(title: String, icon: ImageVector, iconColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Icono con fondo redondeado
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
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Título de la sección
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botón "Ver todo"
        TextButton(onClick = { }) {
            Text(
                text = "See all",
                color = PurplePrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Componente para mostrar cada curso en forma de tarjeta
@Composable
fun CourseItem(course: Course, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = PurplePrimary.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador vertical de color para destacar visualmente
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(PurplePrimary)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Información del curso
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = course.courseName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Icono y texto para el horario
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = course.courseTime,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Etiqueta para el aula con estilo diferenciado
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = PurpleLight.copy(alpha = 0.15f)
                        )
                    ) {
                        Text(
                            text = course.courseRoom,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = PurplePrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Botón para eliminar el curso
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = Color.Gray
                )
            }
        }
    }
}

// Componente para mostrar cada evento en forma de tarjeta
@Composable
fun EventItem(event: AgendaEvent) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = PurplePrimary.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador visual para la fecha del evento
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PurpleLight.copy(alpha = 0.2f)
                ),
                modifier = Modifier.size(56.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Separamos la fecha para mostrar día y mes
                    val dateParts = event.eventDate.split(" ")
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (dateParts.size >= 2) {
                            Text(
                                text = dateParts[0],
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = PurpleDark
                            )
                            Text(
                                text = dateParts[1],
                                fontSize = 12.sp,
                                color = PurplePrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información del evento
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.eventName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = event.eventDate,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// Diálogo para añadir un nuevo curso
@Composable
private fun AddCourseDialog(
    onDismiss: () -> Unit,            // Función para cancelar
    onConfirm: () -> Unit,            // Función para confirmar
    courseName: String,               // Nombre del curso actual
    onCourseNameChange: (String) -> Unit,  // Función para actualizar el nombre
    courseRoom: String,               // Aula actual
    onCourseRoomChange: (String) -> Unit,  // Función para actualizar el aula
    courseTime: String,               // Horario actual
    onCourseTimeChange: (String) -> Unit   // Función para actualizar el horario
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icono en la parte superior del diálogo
                Icon(
                    imageVector = Icons.Rounded.School,
                    contentDescription = null,
                    tint = PurplePrimary,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(bottom = 8.dp)
                )
                // Título del diálogo
                Text(
                    text = "Add New Class",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 22.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            // Formulario con campos para ingresar datos del curso
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campo para el nombre del curso
                OutlinedTextField(
                    value = courseName,
                    onValueChange = onCourseNameChange,
                    label = { Text("Class Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = PurplePrimary,
                        focusedLabelColor = PurplePrimary
                    )
                )

                // Campo para el aula
                OutlinedTextField(
                    value = courseRoom,
                    onValueChange = onCourseRoomChange,
                    label = { Text("Classroom") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = PurplePrimary,
                        focusedLabelColor = PurplePrimary
                    )
                )

                // Campo para el horario
                OutlinedTextField(
                    value = courseTime,
                    onValueChange = onCourseTimeChange,
                    label = { Text("Schedule") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = PurplePrimary,
                        focusedLabelColor = PurplePrimary
                    )
                )
            }
        },
        // Botón para guardar los datos
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurplePrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save", fontWeight = FontWeight.Bold)
            }
        },
        // Botón para cancelar
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
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