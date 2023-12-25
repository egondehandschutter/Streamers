package examen.streamers.data

import kotlinx.coroutines.flow.Flow

interface StreamerInfoRepository {

    fun getAllStreamersStream(): Flow<List<StreamerInfo>>

    /**
     * Retrieve a doctor from the given data source that matches with the [id].
     */
    fun getStreamerStream(id: String): Flow<StreamerInfo?>

    /**
     * Insert doctor in the data source
     */
    suspend fun insertStreamer(doctor: StreamerInfo)

    /**
     * Delete doctor from the data source
     */
    suspend fun deleteStreamer(doctor: StreamerInfo)

    /**
     * Update doctor in the data source
     */
    suspend fun updateStreamer(doctor: StreamerInfo)
}


class OfflineStreamerInfoRepository(private val streamerInfoDao: StreamerInfoDao) : StreamerInfoRepository {
    override fun getAllStreamersStream(): Flow<List<StreamerInfo>> = streamerInfoDao.getAllDoctors()

    override fun getStreamerStream(id: String): Flow<StreamerInfo?> = streamerInfoDao.getDoctor(id)

    override suspend fun insertStreamer(doctor: StreamerInfo) = streamerInfoDao.insert(doctor)

    override suspend fun deleteStreamer(doctor: StreamerInfo) = streamerInfoDao.delete(doctor)

    override suspend fun updateStreamer(doctor: StreamerInfo) = streamerInfoDao.update(doctor)
}
