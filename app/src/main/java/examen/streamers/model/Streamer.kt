package examen.streamers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A Streamer with a username, avatar, twitchUrl, url, isLive, isCommunityStreamer.
 *
 * @property username the name of the streamer.
 * @property avatar the profile picture of the streamer.
 * @property twitchUrl the twitch url of the streamer.
 * @property url the url of the streamer.
 * @property isLive is the streamer live.
 * @property isCommunityStreamer is the streamer a isCommunityStreamer.
 * @constructor creates a streamer with a username, avatar, twitchUrl, url, isLive, isCommunityStreamer.
 */
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