package examen.streamers.model


import kotlinx.serialization.Serializable

/**
 * Streamers with al list of streamers.
 *
 * @property streamers the list of different streamer.
 * @constructor creates streamers with a list of streamers.
 */
@Serializable
data class Streamers(
    val streamers: List<Streamer>
)