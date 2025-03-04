package models


import java.io.Serializable

data class EventModel(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String): Serializable
{
    companion object {
        fun fakeEvents(): List<EventModel> {
            return listOf(
                EventModel(
                    "1e2d345a",
                    "Gala annuel de l'ISEN",
                    "Soirée prestigieuse organisée par le BDE pour célébrer les réussites de l\'année dans une ambiance festive.",
                    "10 décembre 2024",
                    "Palais Neptune, Toulon",
                    "BDE"),
                EventModel(
                    "0d1d122c",
                    "Journée de cohésion ISEN",
                    "Un moment pour accueillir les nouveaux élèves et renforcer la cohésion entre les promotions avec des activités autour de la santé, l'écologie, et la vie associative.",
                    "24 septembre 2024",
                    "Plage du Mourillon",
                    "Vie associative"),
            )
        }
    }
}