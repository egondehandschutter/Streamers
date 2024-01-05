package examen.streamers.fake

import examen.streamers.model.Streamers
import examen.streamers.network.StreamerApiService

//Fake api service with streamers from the fake data source
class FakeStreamerApiService: StreamerApiService {
    override suspend fun getStreamers(apiEndpoint :String): Streamers {
            return FakeDataSource.streamers
    }
}