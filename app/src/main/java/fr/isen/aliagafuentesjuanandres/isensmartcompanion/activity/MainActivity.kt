package fr.isen.aliagafuentesjuanandres.isensmartcompanion.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import androidx.compose.foundation.layout.*

/**
 * MainActivity es la actividad principal de la aplicación.
 *
 * - Hereda de ComponentActivity, lo que permite usar Jetpack Compose.
 * - Se encarga de configurar la interfaz de usuario con un tema y una navegación.
 * - Implementa una barra de navegación inferior y un sistema de navegación interna.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita la funcionalidad de bordes extendidos en la UI.
        enableEdgeToEdge()

        // Define el contenido de la actividad con Jetpack Compose.
        setContent {
            ISENSmartCompanionTheme {
                // Crea y recuerda el controlador de navegación.
                val navController = rememberNavController()

                // Estructura principal de la pantalla usando Scaffold.
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) } // Agrega la barra de navegación inferior.
                ) { innerPadding ->
                    // Configura el sistema de navegación y aplica el padding necesario.
                    NavigationGraph(navController, Modifier.padding(innerPadding))
                }
            }
        }
    }
}
