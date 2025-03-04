package fr.isen.aliagafuentesjuanandres.isensmartcompanion.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: PaddingValues) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") })
        },
        bottomBar = { BottomNavBar(navController) } // ✅ Ensure bottom navigation is included
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) { // ✅ Apply padding correctly
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.isen),
                    contentDescription = "ISEN Logo",
                    modifier = Modifier.size(200.dp) // Adjust size as needed
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ISENsmartCompanion",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f)) // Pushes content up

                ChatInputField() // ✅ Add chat input field
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: PaddingValues) {

}

@Composable
fun ChatInputField() {
    var message by remember { mutableStateOf("") } // ✅ State to store the message

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 72.dp) // ✅ Position above bottom navigation bar
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = message, // ✅ Bind TextField to state
                onValueChange = { message = it }, // ✅ Update state on input
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") }
            )
            FloatingActionButton(
                onClick = {
                    if (message.isNotBlank()) {
                        // Handle message send action (e.g., print it, send to backend)
                        println("Message Sent: $message")
                        message = "" // ✅ Clear input after sending
                    }
                },
                modifier = Modifier.padding(start = 8.dp),
                containerColor = Color.Red
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}