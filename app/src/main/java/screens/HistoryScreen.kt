package fr.isen.aliagafuentesjuanandres.isensmartcompanion.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Sample history data
data class HistoryItem(val title: String, val description: String, val date: String)

val sampleHistory = listOf(
    HistoryItem("AI Workshop", "Attended an AI & ML hands-on session.", "Jan 10, 2025"),
    HistoryItem("Tech Expo 2024", "Explored cutting-edge technology trends.", "Nov 15, 2024"),
    HistoryItem("Startup Meetup", "Networked with entrepreneurs and investors.", "Oct 5, 2024")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: PaddingValues) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(sampleHistory) { history ->
                HistoryItemCard(history)
            }
        }
    }
}

@Composable
fun HistoryItemCard(history: HistoryItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Handle click */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = history.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = history.description)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Date: ${history.date}", fontWeight = FontWeight.Light)
        }
    }
}
