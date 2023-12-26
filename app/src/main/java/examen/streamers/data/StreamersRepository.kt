package examen.streamers.data


import examen.streamers.model.Streamers
import examen.streamers.network.StreamerApiService

interface StreamersRepository {
    suspend fun getStreamers(): Streamers
    suspend fun getStreamersInfo(): List<StreamerInfo>
}

class NetworkStreamersRepository(
    private val streamerApiService: StreamerApiService
) : StreamersRepository {
    private val apiEndpoint = "streamers"


    /** Fetches streamer locations from StreamerApi according to apiEndpoint */
    override suspend fun getStreamers(): Streamers = streamerApiService.getStreamers(apiEndpoint)

    override suspend fun getStreamersInfo(): List<StreamerInfo> {
        val result = getStreamers().streamers
        val streamersInfo = result.map {
            StreamerInfo(
                //id = 0,
                avatar = it.avatar,
                isCommunityStreamer = it.isCommunityStreamer,
                twitchUrl = it.twitchUrl ?: "",
                url = it.url,
                username = it.username
            )

    }
    return streamersInfo
    }

}