package fr.isen.aliagafuentesjuanandres.isensmartcompanion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ISENSmartCompanionTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current  // Get the application context
    var userInput by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("AI Response will appear here...") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo and Title
        Image(
            painter = painterResource(id = R.drawable.isen_logo), // Ensure image is in res/drawable
            contentDescription = "ISEN Logo",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Text(
            text = "ISEN",
            fontSize = 32.sp,
            color = Color.Red,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Smart Companion",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Input Field with Send Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text("Ask a question...") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    responseText = "This is a placeholder AI response."
                    Toast.makeText(context, "Question Submitted", Toast.LENGTH_SHORT).show()
                })
            )

            Button(
                onClick = {
                    responseText = "This is a placeholder AI response."
                    Toast.makeText(context, "Question Submitted", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.size(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send), // Ensure you have this icon in drawable
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }

        // Display Response
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = responseText,
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}
