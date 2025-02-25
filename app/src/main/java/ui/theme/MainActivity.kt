package ui.theme

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.ui.screens.HistoryScreen
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.ui.screens.MainScreen


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreenWithNavBar()
        }
    }

    private fun MainScreen() {
        TODO("Not yet implemented")
    }
}

@Composable
fun MainScreenWithNavBar() {
    val homeTab = TabBarItem("Home", Icons.Filled.Home, Icons.Outlined.Home)
    val alertsTab = TabBarItem("Events", Icons.Filled.Notifications, Icons.Outlined.Notifications)
    val settingsTab = TabBarItem("History", Icons.Filled.Settings, Icons.Outlined.Settings)

    val tabBarItems = listOf(homeTab, alertsTab, settingsTab)
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { TabView(tabBarItems, navController) }
    ) { paddingValues ->
        NavHost(navController, startDestination = homeTab.title) {
            composable(homeTab.title) { MainScreen(paddingValues) }
            composable(alertsTab.title) { HomeScreen(paddingValues) }
            composable(settingsTab.title) { HistoryScreen(paddingValues) }
        }
    }
}

@Composable
fun TabView(tabBarItems: List<TabBarItem>, navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    NavigationBar {
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(tabBarItem.title) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = { Text(tabBarItem.title) }
            )
        }
    }
}

@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    Icon(
        imageVector = if (isSelected) selectedIcon else unselectedIcon,
        contentDescription = title
    )
}

@Composable
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge { Text(count.toString()) }
    }
}

data class EventItem(
    val title: String,
    val date: String,
    val description: String
)

@Composable
fun HomeScreen(innerPadding: PaddingValues) {
    val context = LocalContext.current
    var userInput by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("AI Response will appear here...") }

    fun handleSubmission() {
        if (userInput.isNotBlank()) {
            responseText = "This is a placeholder AI response for: $userInput"
            userInput = ""
            Toast.makeText(context, "Question Submitted", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "📅 Upcoming Events",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "1️⃣ Tech Conference - March 20, 2025\n" +
                    "2️⃣ AI Workshop - April 5, 2025\n" +
                    "3️⃣ Hackathon - May 10, 2025",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun Events() {
    // Content for events screen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Events", style = MaterialTheme.typography.headlineLarge)
    }
}

@Composable
fun HistoryScreen() {
    // Content for settings screen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("History", style = MaterialTheme.typography.headlineLarge)
    }
}

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)