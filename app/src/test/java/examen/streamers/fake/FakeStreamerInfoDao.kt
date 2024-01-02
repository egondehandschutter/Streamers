package examen.streamers.fake

import examen.streamers.data.StreamerInfo
import examen.streamers.data.StreamerInfoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeStreamerInfoDao: StreamerInfoDao{
    private val streamerInfoList = FakeDataSource.streamers.streamers.map {
        StreamerInfo(
            username = it.username,
            avatar = it.avatar,
            twitchUrl = it.twitchUrl ?: "",
            url = it.url,
            isCommunityStreamer = it.isCommunityStreamer,
        )
    }

    // Build a flow and emit once
    override fun getAllStreamers(): Flow<List<StreamerInfo>> = flow {
        emit(streamerInfoList)
    }

    override suspend fun getStreamer(username: String): StreamerInfo {
        return streamerInfoList.filter { it.username == username }[0]
    }

    override suspend fun getUsernames(): List<String> {
        return streamerInfoList.map { it.username }
    }
    // provide empty implementation for the convenience functions
    override suspend fun insert(streamerInfo: StreamerInfo) = Unit

    override suspend fun delete(streamerInfo: StreamerInfo) = Unit

    override suspend fun update(streamerInfo: StreamerInfo) = Unit

}