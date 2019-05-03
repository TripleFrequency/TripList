package me.michaelhaas.triplist.service.http.model

data class TripDetailsContract(
    val id: Int,
    val location: String,
    val description: String,
    val photoUrls: List<String>,
    val activities: List<ActivityContract>
)