package examen.streamers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Streamer(
    val username: String,
    val avatar: String,
    @SerialName("twitch_url")
    val twitchUrl: String? = null,
    val url: String,
    @SerialName("is_live")
    val isLive: Boolean,
    @SerialName("is_community_streamer")
    val isCommunityStreamer: Boolean
)