package examen.streamers.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streamers")
data class StreamerInfo(
    @PrimaryKey
    val avatar: String,
    val isCommunityStreamer: Boolean,
    val isLive: Boolean,
    val twitchUrl: String,
    val url: String,
    val username: String
)
