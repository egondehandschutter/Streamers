package examen.streamers

import examen.streamers.data.NetworkStreamersRepository
import examen.streamers.fake.FakeDataSource
import examen.streamers.fake.FakeStreamerApiService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NetworkStreamersRepositoryTest {

    //testing if the streamers from the network repository are the ones in the fake data source
    @Test
    fun networkStreamerRepository_getStreamers_verifyStreamers() =
        runTest {
            val repository = NetworkStreamersRepository(
                streamerApiService = FakeStreamerApiService()
            )
            assertEquals(FakeDataSource.streamers, repository.getStreamers())
        }
}