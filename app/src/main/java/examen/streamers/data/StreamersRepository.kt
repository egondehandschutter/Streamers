package examen.streamers.data


import examen.streamers.model.Streamers
import examen.streamers.network.StreamerApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

interface StreamersRepository {
    suspend fun getStreamers(): Streamers
    suspend fun getStreamersInfo(): List<StreamerInfo>

    suspend fun getRealTimeStreamerInfo(): List<RealTimeStreamerInfo>

    /* Flow that emits the real time parking info on a regular (60 s) basis*/
    val realTimeStreamer: Flow<List<RealTimeStreamerInfo>>
}

// Interval for refreshing real time parking information
private const val refreshIntervalMs: Long = 60000


// Custom exception used to cancel the flow in unit test
class TestException(message: String) : Exception(message)


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

    override suspend fun getRealTimeStreamerInfo(): List<RealTimeStreamerInfo> {
        val result = getStreamers().streamers
        val realTimeStreamerInfo = result.map {
            RealTimeStreamerInfo(
                username = it.username,
                isLive = it.isLive
            )

        }
        return realTimeStreamerInfo
    }

    // Actually build the flow for emiting the real time streamer information
    override val realTimeStreamer: Flow<List<RealTimeStreamerInfo>> = flow {
        while (true) {
            try {
                val realTimeStreamer = getRealTimeStreamerInfo()
                emit(realTimeStreamer) // Emits the result of the request to the flow
            }
            catch (_: IOException) {  }
            catch (_: HttpException) {  }
            catch (e: TestException) { break }
            delay(timeMillis = refreshIntervalMs) // Suspends the coroutine for some time
        }
    }

}