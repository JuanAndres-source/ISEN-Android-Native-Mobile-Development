package fr.isen.aliagafuentesjuanandres.isensmartcompanion.activity

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.screens.AgendaEvent
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.screens.AgendaScreen
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.api.AppDatabase
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.screens.Course
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.screens.EventsScreen
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.screens.HistoryScreen
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.screens.MainScreen

/**
 * Esta función define la estructura de navegación de la aplicación, permitiendo cambiar entre diferentes pantallas.
 *
 * @param navController Controlador de navegación que gestiona los cambios de pantalla.
 * @param modifier Modificador que permite ajustes en la apariencia y comportamiento del NavHost.
 */
@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier) {
    // Se obtiene el contexto actual dentro de la función composable
    val context = LocalContext.current

    // Se obtiene la base de datos y el DAO para manejar interacciones
    val database = AppDatabase.getDatabase(context)
    val interactionDao = database.interactionDao()

    // Se define el NavHost con la pantalla inicial y las rutas de navegación
    NavHost(navController, startDestination = "home", modifier = modifier) {
        // Ruta para la pantalla principal
        composable("home") { MainScreen() }

        // Ruta para la pantalla de eventos
        composable("events") { EventsScreen(navController) }

        // Ruta para la pantalla de agenda
        composable("agenda") {
            // Lista de cursos de ejemplo
            val sampleCourses = listOf(
                Course("Native Mobile Development", "08:00 AM", "Class : 559"),
                Course("Social Human Economical Sciences", "12:00 PM", "Class : 321")
            )

            // Lista de eventos de ejemplo
            val sampleEvents = listOf(
                AgendaEvent("Isen FootBall Training", "March 13"),
                AgendaEvent("Nice Trip", "March 15")
            )

            // Se pasa la lista de cursos y eventos a la pantalla de agenda
            AgendaScreen(courses = sampleCourses, events = sampleEvents)
        }

        // Ruta para la pantalla de historial
        composable("history") {
            HistoryScreen(interactionDao)
        }
    }
}
