package fr.isen.aliagafuentesjuanandres.isensmartcompanion.activity

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * Barra de navegación inferior con cuatro opciones: Home, Events, History y Agenda.
 * Cada opción tiene un icono y permite la navegación entre pantallas.
 *
 * @param navController Controlador de navegación para gestionar los cambios de pantalla.
 */
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    // Esta lista guarda los nombres de cada pantalla para la navegación
    val items = listOf("home", "events", "history", "agenda")

    // Esta lista sirve para guardar los iconos de cada pantalla
    val icons = listOf(
        Icons.Filled.Home,   // Icono de la pantalla principal
        Icons.Filled.Event,  // Icono de la pantalla de eventos
        Icons.Filled.History, // Icono de la pantalla de historial
        Icons.Filled.Book    // Icono de la pantalla de agenda
    )

    // Esta lista almacena las etiquetas que se mostrarán en la barra de navegación
    val labels = listOf("Home", "Events", "History", "Agenda")

    // Variable para almacenar el índice del ítem seleccionado en la barra de navegación
    var selectedItem by rememberSaveable { mutableStateOf(0) }

    // Se crea la barra de navegación inferior con fondo blanco
    NavigationBar(
        containerColor = Color.White
    ) {
        // Se genera cada ítem de la barra de navegación
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = labels[index]
                    )
                },
                label = {
                    Text(text = labels[index])
                },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index // Se actualiza el ítem seleccionado
                    navController.navigate(screen) // Se navega a la pantalla seleccionada
                },
                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF6200EE), // Color del icono cuando está seleccionado
                    unselectedIconColor = Color(0xFF6200EE).copy(alpha = 0.6f), // Color del icono cuando no está seleccionado
                    selectedTextColor = Color(0xFF6200EE), // Color del texto cuando está seleccionado
                    unselectedTextColor = Color(0xFF6200EE).copy(alpha = 0.6f), // Color del texto cuando no está seleccionado
                    indicatorColor = Color.Transparent // Se mantiene sin indicador visual de selección
                )
            )
        }
    }
}
