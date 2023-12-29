package examen.streamers.fake

import examen.streamers.data.StreamerInfo
import examen.streamers.data.StreamerInfoRepository
import kotlinx.coroutines.flow.Flow

class FakeOfflineStreamerInfoRepository (
    private val streamerInfoDao: FakeStreamerInfoDao
) : StreamerInfoRepository {

    override fun getAllStreamersStream(): Flow<List<StreamerInfo>> =
        streamerInfoDao.getAllStreamers()

    override suspend fun getStreamer(username: String): StreamerInfo =
        streamerInfoDao.getStreamer(username)

    override suspend fun getUsernames(): List<String> = streamerInfoDao.getUsernames()

    override suspend fun insertStreamer(streamer: StreamerInfo) = streamerInfoDao.insert(streamer)

    override suspend fun deleteStreamer(streamer: StreamerInfo) = streamerInfoDao.delete(streamer)

    override suspend fun updateStreamer(streamer: StreamerInfo) = streamerInfoDao.update(streamer)
}