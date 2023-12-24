package examen.streamers.model


import kotlinx.serialization.Serializable

@Serializable
data class Streamers(
    val streamers: List<Streamer>
)