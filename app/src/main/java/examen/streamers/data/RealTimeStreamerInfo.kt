package examen.streamers.data


/**
 * RealTimeStreamerInfo with a username, isLive.
 *
 * @property username the name of the streamer.
 * @property isLive is the streamer live.
 * @constructor creates a streamer with a username, isLive.
 */
data class RealTimeStreamerInfo(
    val username: String,
    val isLive: Boolean,

)