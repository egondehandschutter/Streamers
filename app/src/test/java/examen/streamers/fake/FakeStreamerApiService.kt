package examen.streamers.fake

import examen.streamers.model.Streamers
import examen.streamers.network.StreamerApiService

class FakeStreamerApiService: StreamerApiService {
    override suspend fun getStreamers(apiEndpoint :String): Streamers {
            return FakeDataSource.streamers
    }
}