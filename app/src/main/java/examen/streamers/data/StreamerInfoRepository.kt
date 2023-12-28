package examen.streamers.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [StreamerInfo] from a given data source.
 */
interface StreamerInfoRepository {

    /**
     * Retrieve all the streamers from the the given data source.
     */
    fun getAllStreamersStream(): Flow<List<StreamerInfo>>

    /**
     * Retrieve a streamer from the given data source that matches with the [username].
     */
    //fun getStreamerStream(id: String): Flow<StreamerInfo?>
    suspend fun getStreamer(username: String): StreamerInfo


    // Retrieve the list of primary keys which are distinct by nature
    suspend fun getUsername(): List<String>

    /**
     * Insert streamer in the data source
     */
    suspend fun insertStreamer(streamer: StreamerInfo)

    /**
     * Delete streamer from the data source
     */
    suspend fun deleteStreamer(streamer: StreamerInfo)

    /**
     * Update streamer in the data source
     */
    suspend fun updateStreamer(streamer: StreamerInfo)
}


class OfflineStreamerInfoRepository(private val streamerInfoDao: StreamerInfoDao) : StreamerInfoRepository {
    override fun getAllStreamersStream(): Flow<List<StreamerInfo>> = streamerInfoDao.getAllStreamers()

    //override fun getStreamerStream(id: String): Flow<StreamerInfo?> = streamerInfoDao.getStreamer(id)
    override suspend fun getStreamer(username: String): StreamerInfo = streamerInfoDao.getStreamer(username)

    override suspend fun getUsername(): List<String> = streamerInfoDao.getUsernames()

    override suspend fun insertStreamer(streamer: StreamerInfo) = streamerInfoDao.insert(streamer)

    override suspend fun deleteStreamer(streamer: StreamerInfo) = streamerInfoDao.delete(streamer)

    override suspend fun updateStreamer(streamer: StreamerInfo) = streamerInfoDao.update(streamer)
}
