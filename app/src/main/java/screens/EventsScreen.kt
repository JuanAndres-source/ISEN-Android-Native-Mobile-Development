package fr.isen.aliagafuentesjuanandres.isensmartcompanion.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.EventModel
import api.NetworkManager.NetworkManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(innerPadding: PaddingValues) {
    val coroutineScope = rememberCoroutineScope()
    var events by remember { mutableStateOf<List<EventModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch events
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                events = NetworkManager.api.getEvents()
                isLoading = false
            } catch (e: Exception) {
                Log.e("EventsScreen", "Error fetching events: ${e.message}")
                errorMessage = "Failed to load events"
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Upcoming Events") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            } else if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            } else {
                LazyColumn {
                    items(events) { event ->
                        EventCard(event)
                    }
                }
            }
        }
    }
}

@Composable
fun EventCard(event: EventModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(event.title, fontSize = MaterialTheme.typography.titleLarge.fontSize, fontWeight = MaterialTheme.typography.titleLarge.fontWeight)
            Text(event.date, fontSize = MaterialTheme.typography.bodyMedium.fontSize, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(event.description, fontSize = MaterialTheme.typography.bodySmall.fontSize)
        }
    }
}
