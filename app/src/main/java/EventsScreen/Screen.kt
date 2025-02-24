package fr.isen.aliagafuentesjuanandres.isensmartcompanion.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Sample event data
data class Event(val title: String, val description: String, val date: String)

val sampleEvents = listOf(
    Event("Tech Conference 2025", "A conference about emerging technologies.", "March 15, 2025"),
    Event("Android Dev Summit", "Annual Android development event.", "April 10, 2025"),
    Event("AI & ML Workshop", "A hands-on workshop on AI and ML.", "May 20, 2025"),
    Event("Startup Pitch Night", "Showcase your startup ideas.", "June 5, 2025")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Events") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(sampleEvents) { event ->
                EventItem(event)
            }
        }
    }
}

@Composable
fun EventItem(event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Handle click event */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = event.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = event.description)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Date: ${event.date}", fontWeight = FontWeight.Light)
        }
    }
}
