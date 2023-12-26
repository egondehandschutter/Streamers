package examen.streamers.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streamers")
data class StreamerInfo(
    @PrimaryKey//(autoGenerate = true)
    //val id : Int,
    val username: String,
    val avatar: String,
    val isCommunityStreamer: Boolean,
    val twitchUrl: String,
    val url: String

)
