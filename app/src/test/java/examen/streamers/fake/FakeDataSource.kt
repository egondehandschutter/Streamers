package examen.streamers.fake

import examen.streamers.model.Streamer
import examen.streamers.model.Streamers

//FakeDataSource with 2 streamers
object FakeDataSource {
    private val resultOne = Streamer(
        username = "Streamer 1",
        avatar = "",
        twitchUrl = "",
        url = "",
        isLive = true,
        isCommunityStreamer = false,

    )
    private val resultTwo = Streamer(
        username = "Streamer 2",
        avatar = "",
        twitchUrl = "",
        url = "",
        isLive = false,
        isCommunityStreamer = true,
    )
    val streamers = Streamers(streamers = listOf(resultOne, resultTwo))
}
