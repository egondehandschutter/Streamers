package examen.streamers.data

import kotlinx.coroutines.flow.Flow

interface StreamerInfoRepository {

    fun getAllStreamersStream(): Flow<List<StreamerInfo>>

    /**
     * Retrieve a streamer from the given data source that matches with the [id].
     */
    //fun getStreamerStream(id: String): Flow<StreamerInfo?>
    suspend fun getStreamer(id: String): StreamerInfo?

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
    override suspend fun getStreamer(id: String): StreamerInfo? = streamerInfoDao.getStreamer(id)

    override suspend fun insertStreamer(streamer: StreamerInfo) = streamerInfoDao.insert(streamer)

    override suspend fun deleteStreamer(streamer: StreamerInfo) = streamerInfoDao.delete(streamer)

    override suspend fun updateStreamer(streamer: StreamerInfo) = streamerInfoDao.update(streamer)
}
