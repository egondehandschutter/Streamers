package examen.streamers

import examen.huisartsengent.components.TestDispatcherRule
import examen.streamers.data.NetworkStreamersRepository
import examen.streamers.data.OfflineStreamerInfoRepository
import examen.streamers.data.RealTimeStreamerInfo
import examen.streamers.data.SpecialStreamers
import examen.streamers.data.StreamerInfo
import examen.streamers.fake.FakeDataSource
import examen.streamers.fake.FakeNetworkStreamersRepository
import examen.streamers.fake.FakeStreamerApiService
import examen.streamers.fake.FakeStreamerInfoDao
import examen.streamers.ui.screens.StreamersViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class StreamersViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun streamersViewModel_getAllStreamers_verifyViewModelInitSuccess() = runTest {
        // Both fake repositories are build by using the real repositories and injecting them
        // with fake dao en fake api service respectively
        val streamersViewModel = StreamersViewModel(
            streamerInfoRepository = OfflineStreamerInfoRepository(streamerInfoDao = FakeStreamerInfoDao()),
            streamersRepository = FakeNetworkStreamersRepository(
                streamerApiService = FakeStreamerApiService(),
            )
        )
        val selectedStreamer = streamersViewModel.appUiState.value.selectedStreamer
        val streamerList = streamersViewModel.streamerUiState.first().streamerList // first (and single) emit is collected
        val realTimeStreamerInfo = streamersViewModel.realTimeStreamerInfo

        // Check if retrofit was successful without exception thrown
        Assert.assertTrue(streamersViewModel.retrofitSuccessful)

        // Check if the initial selected streamer is "special"
        assertEquals(selectedStreamer, SpecialStreamers.noStreamer)
        // Check if the streamer list corresponds to the fake data source
        assertEquals(
            streamerList,
            FakeDataSource.streamers.streamers.map {
                StreamerInfo(
                    username = it.username,
                    avatar = it.avatar,
                    twitchUrl = it.twitchUrl?: "",
                    url = it.url,
                    isCommunityStreamer = it.isCommunityStreamer
                )
            }
        )

        assertEquals(
            realTimeStreamerInfo,
            FakeDataSource.streamers.streamers.map {
                RealTimeStreamerInfo(
                    username = it.username,
                    isLive = it.isLive
                )
            }
        )
    }


    @Test
    fun streamersViewModel_selectStreamer_verifyAppUiState() = runTest {
        // Both fake repositories are build by using the real repositories and injecting them
        // with fake dao en fake api service respectively
        val streamersViewModel = StreamersViewModel(
            streamerInfoRepository = OfflineStreamerInfoRepository(streamerInfoDao = FakeStreamerInfoDao()),
            streamersRepository = FakeNetworkStreamersRepository(
                streamerApiService = FakeStreamerApiService(),
            )
        )
        // Take primary key of first streamer in fake data source
        val username = FakeDataSource.streamers.streamers.map { it.username }[0]
        // Take first streamer in fake data source
        val streamer = FakeDataSource.streamers.streamers.map {
            StreamerInfo(
                username = it.username,
                avatar = it.avatar,
                twitchUrl = it.twitchUrl?: "",
                url = it.url,
                isCommunityStreamer = it.isCommunityStreamer
            )
        }[0]
        streamersViewModel.selectStreamer(username)
        assertEquals(streamersViewModel.appUiState.value.selectedStreamer, streamer)
    }

    @Test
    fun streamersViewModel_clearStreamer_verifyAppUiState() = runTest {
        val streamersViewModel = StreamersViewModel(
            streamerInfoRepository = OfflineStreamerInfoRepository(streamerInfoDao = FakeStreamerInfoDao()),
            streamersRepository = FakeNetworkStreamersRepository(
                streamerApiService = FakeStreamerApiService(),
            )
        )
        streamersViewModel.clearStreamer()
        assertEquals(streamersViewModel.appUiState.value.selectedStreamer, SpecialStreamers.emptyStreamer)
    }
}