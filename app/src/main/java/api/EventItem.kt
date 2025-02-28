package api

import com.google.gson.annotations.SerializedName

data class EventItem(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("date") val date: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("location") val location: String
)
