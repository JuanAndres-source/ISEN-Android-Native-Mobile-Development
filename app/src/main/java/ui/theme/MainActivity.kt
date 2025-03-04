package ui.theme

import android.os.Bundle
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import fr.isen.aliagafuentesjuanandres.isensmartcompanion.screens.TabBarItem
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

fun composable(title: Any, function: @Composable () -> Unit) {

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
    val id: String,
    val title: String,
    val date: String,
    val description: String,
    val location: String,
    val category: String
)

@Composable
fun HomeScreen(innerPadding: PaddingValues) {
    val events = listOf(
        EventItem(
            "0d1d122c", "Journée de cohésion ISEN", "24 septembre 2024",
            "Un moment pour accueillir les nouveaux élèves et renforcer la cohésion entre les promotions avec des activités autour de la santé, l'écologie, et la vie associative.",
            "Plage du Mourillon", "Vie associative"
        ),
        EventItem(
            "1e2d345a", "Gala annuel de l'ISEN", "10 décembre 2024",
            "Soirée prestigieuse organisée par le BDE pour célébrer les réussites de l'année dans une ambiance festive.",
            "Palais Neptune, Toulon", "BDE"
        ),
        EventItem(
            "2f3g456b", "Tournoi de futsal ISEN", "15 octobre 2024",
            "Compétition sportive organisée par le BDS pour les amateurs de football en salle.",
            "Complexe sportif de Toulon", "BDS"
        ),
        EventItem(
            "3h4i567c", "Conférence Digital Native Forum", "19 janvier 2024",
            "Rencontre entre étudiants et professionnels pour explorer les opportunités de carrière et les innovations technologiques.",
            "Campus Toulon", "Professionnel"
        ),
        EventItem(
            "4j5k678d", "Soirée d'intégration", "30 septembre 2024",
            "Une soirée conviviale pour intégrer les nouveaux élèves et renforcer les liens entre promotions.",
            "Bar Le Kraken", "BDE"
        ),
        EventItem(
            "5l6m789e", "Challenge Make IT Agri", "1er novembre 2024",
            "Concours inter-écoles pour innover dans l'agriculture durable avec des solutions numériques.",
            "En ligne", "Concours"
        ),
        EventItem(
            "6n7o891f", "Portes ouvertes ISEN", "25 janvier 2024",
            "Journée pour découvrir les formations et rencontrer les équipes pédagogiques.",
            "Campus Toulon", "Institutionnel"
        ),
        EventItem(
            "7p8q902g", "Soirée Halloween ISEN", "31 octobre 2024",
            "Une soirée déguisée organisée par le BDE avec animations et concours de costumes.",
            "Salle des fêtes, Toulon", "BDE"
        ),
        EventItem(
            "8r9s013h", "Tournoi de badminton", "22 février 2024",
            "Événement sportif organisé par le BDS pour les passionnés de raquettes.",
            "Gymnase municipal", "BDS"
        ),
        EventItem(
            "9t0u124i", "Hackathon ISEN", "10 mars 2024",
            "Compétition de développement informatique sur 48 heures avec des prix pour les meilleures équipes.",
            "Campus Toulon", "Technologique"
        ),
        EventItem(
            "10v1w235j", "Atelier de préparation à l'étranger", "14 novembre 2024",
            "Conférences et stands pour faciliter la mobilité internationale des étudiants.",
            "Campus Toulon", "International"
        ),
        EventItem(
            "11x2y346k", "Journée du développement durable", "20 avril 2024",
            "Sensibilisation aux enjeux écologiques et initiatives durables à l'ISEN.",
            "Campus Toulon", "Vie associative"
        )
    )

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
            text = "📅 Événements à venir",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn {
            items(events) { event ->
                EventCard(event)
            }

        }
    }



    data class TabBarItem(
        val title: String,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector,
        val badgeAmount: Int? = null
    )
}

@Composable
fun EventCard(event: EventItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.DarkGray
            )
            Text(
                text = "📆 ${event.date}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Text(
                text = "📍 ${event.location}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
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
/*
 fun fetch

 */