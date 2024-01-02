package examen.streamers.data

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * StreamerInfo with a username, avatar, twitchUrl, url, isCommunityStreamer.
 *
 * @property username the name of the streamer.
 * @property avatar the profile picture of the streamer.
 * @property twitchUrl the twitch url of the streamer.
 * @property url the url of the streamer.
 * @property isCommunityStreamer is the streamer a isCommunityStreamer.
 * @constructor creates a streamer with a username, avatar, twitchUrl, url, isCommunityStreamer.
 */
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

// Declaration of the "special" streamers used in the application
object SpecialStreamers {
    // when there are no streamers to display
    val emptyStreamer = StreamerInfo(
        username = "",
        avatar = "",
        isCommunityStreamer = false,
        twitchUrl = "",
        url = ""
    )

    // When the application starts, used in first recompositions of the home screen
    val startStreamer = StreamerInfo(
        username = "Please wait ...",
        avatar = "",
        isCommunityStreamer = false,
        twitchUrl = "",
        url = ""
    )

    val noStreamer = StreamerInfo(
        username = "No streamers found ...",
        avatar = "",
        isCommunityStreamer = false,
        twitchUrl = "",
        url = ""
    )

}



