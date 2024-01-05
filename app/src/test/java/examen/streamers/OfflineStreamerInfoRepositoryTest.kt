package examen.streamers

import examen.streamers.data.OfflineStreamerInfoRepository
import examen.streamers.data.StreamerInfo
import examen.streamers.fake.FakeDataSource
import examen.streamers.fake.FakeStreamerInfoDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class OfflineStreamerInfoRepositoryTest {

    //testing if the streamer info from the offline repository
    // are the streamers in the fake data source mapped to streamer info
    @Test
    fun offlineStreamerInfoRepository_getAllStreamersStream_verifyStreamerStream() =
        runTest {
            // Use real OffLineStreamerInfoRepository with fake Dao injected
            // Provides effectively a fake repository
            val repository = OfflineStreamerInfoRepository(
                streamerInfoDao = FakeStreamerInfoDao()
            )
            assertEquals(
                FakeDataSource.streamers.streamers.map {
                    StreamerInfo(
                        username = it.username,
                        avatar = it.avatar,
                        twitchUrl = it.twitchUrl ?: "",
                        url = it.url,
                        isCommunityStreamer = it.isCommunityStreamer,
                    )
                },
                repository.getAllStreamersStream().first() // Collects the first (and single) emit
            )
        }

    //testing if the function getStreamer in the repository works
    //to get the correct selected streamer
    @Test
    fun offlineStreamerInfoRepository_getStreamer_verifyStreamer() =
        runTest {
            // Use real OffLineStreamerInfoRepository with fake Dao injected
            // Provides effectively a fake repository
            val repository = OfflineStreamerInfoRepository(
                streamerInfoDao = FakeStreamerInfoDao()
            )
            // Take first streamer from fake data source
            val streamer = FakeDataSource.streamers.streamers.map {
                StreamerInfo(
                    username = it.username,
                    avatar = it.avatar,
                    twitchUrl = it.twitchUrl ?: "",
                    url = it.url,
                    isCommunityStreamer = it.isCommunityStreamer,
                )
            }[0]
            assertEquals(
                streamer,
                repository.getStreamer(streamer.username)
            )
        }


    //testing if the function getUsernames in the repository works
    //to get the correct usernames
    @Test
    fun offlineStreamerInfoRepository_getUsernames_verifyUsernames() =
        runTest {
            // Use real OffLineStreamerInfoRepository with fake Dao injected
            // Provides effectively a fake repository
            val repository = OfflineStreamerInfoRepository(
                streamerInfoDao = FakeStreamerInfoDao()
            )
            // Build list of usernames from fake data source
            val usernames = FakeDataSource.streamers.streamers.map {
                it.username
            }
            assertEquals(
                usernames,
                repository.getUsernames()
            )
        }
}