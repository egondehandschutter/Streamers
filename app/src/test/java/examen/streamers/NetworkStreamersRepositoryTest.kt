package examen.streamers

import examen.streamers.data.NetworkStreamersRepository
import examen.streamers.data.StreamerInfo
import examen.streamers.fake.FakeDataSource
import examen.streamers.fake.FakeStreamerApiService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NetworkStreamersRepositoryTest {
    @Test
    fun networkStreamerRepository_getStreamers_verifyStreamers() =
        runTest {
            val repository = NetworkStreamersRepository(
                streamerApiService = FakeStreamerApiService()
            )
            assertEquals(FakeDataSource.streamers, repository.getStreamers())
        }

}