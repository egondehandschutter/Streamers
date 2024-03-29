package examen.streamers.fake

import examen.streamers.data.RealTimeStreamerInfo
import examen.streamers.data.StreamerInfo
import examen.streamers.data.StreamersRepository
import examen.streamers.model.Streamers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//Fake network repository using the fake api service
class FakeNetworkStreamersRepository (
    private val streamerApiService : FakeStreamerApiService
    )  : StreamersRepository {
    override suspend fun getStreamers(): Streamers = streamerApiService.getStreamers(
        apiEndpoint = "dummy",
    )
    override suspend fun getStreamersInfo(): List<StreamerInfo> = getStreamers().streamers.map{
        StreamerInfo(
            username = it.username,
            avatar = it.avatar,
            twitchUrl = it.twitchUrl ?: "",
            url = it.url,
            isCommunityStreamer = it.isCommunityStreamer,
        )
    }

    override suspend fun getRealTimeStreamerInfo(): List<RealTimeStreamerInfo> =  getStreamers().streamers.map {
        RealTimeStreamerInfo(
            username = it.username,
            isLive = it.isLive
        )
    }

    override val realTimeStreamer: Flow<List<RealTimeStreamerInfo>> = flow {
        emit(
            getStreamers().streamers.map {
                RealTimeStreamerInfo(
                    username = it.username,
                    isLive = it.isLive
                )
            }
        )
    }
}