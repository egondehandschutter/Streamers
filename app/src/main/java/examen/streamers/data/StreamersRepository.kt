package examen.streamers.data


import examen.streamers.model.Streamers
import examen.streamers.network.StreamerApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository that fetches streamer from StreamerApi.
 */
interface StreamersRepository {
    /** Fetches streamer from StreamerApi */
    suspend fun getStreamers(): Streamers
    /** Maps Api data to persistent data */
    suspend fun getStreamersInfo(): List<StreamerInfo>
    /** Maps Api data to non persistent real time data*/
    suspend fun getRealTimeStreamerInfo(): List<RealTimeStreamerInfo>
    /** Flow with list with the RealTimeStreamerInfo, uses ApiService */
    /* Flow that emits the real time parking info on a regular (60 s) basis*/
    val realTimeStreamer: Flow<List<RealTimeStreamerInfo>>
}

// Interval for refreshing real time streamer information
private const val refreshIntervalMs: Long = 60000

// Custom exception used to cancel the flow in unit test
class TestException(message: String) : Exception(message)

/**
 * Network Implementation of Repository that fetches streamers from StreamerApi.
 */
class NetworkStreamersRepository(
    private val streamerApiService: StreamerApiService
) : StreamersRepository {
    private val apiEndpoint = "streamers"

    /** Fetches streamer from StreamerApi according to apiEndpoint*/
    override suspend fun getStreamers(): Streamers = streamerApiService.getStreamers(apiEndpoint)

    /** Convert the fetched streamer to streamer info*/
    override suspend fun getStreamersInfo(): List<StreamerInfo> {
        val result = getStreamers().streamers
        val streamersInfo = result.map {
            StreamerInfo(
                avatar = it.avatar,
                isCommunityStreamer = it.isCommunityStreamer,
                twitchUrl = it.twitchUrl ?: "",
                url = it.url,
                username = it.username
            )
        }
        return streamersInfo
    }

    /** Fetches real time streamers from RealTimeStreamerApi
     *  according to apiEndpoint */
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

    // Actually build the flow for emitting the real time streamer information
    override val realTimeStreamer: Flow<List<RealTimeStreamerInfo>> = flow {
        while (true) {
            try {
                val realTimeStreamer = getRealTimeStreamerInfo()
                emit(realTimeStreamer) // Emits the result of the request to the flow
            }
            catch (_: IOException) {
                emit(listOf())
                delay(100)
                continue
            }
            catch (_: HttpException) {
                emit(listOf())
                delay(100)
                continue
            }
            catch (e: TestException) { break }
            delay(timeMillis = refreshIntervalMs) // Suspends the coroutine for some time
        }
    }
}