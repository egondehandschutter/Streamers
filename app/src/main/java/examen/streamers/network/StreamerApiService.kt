package examen.streamers.network

import examen.streamers.model.Streamers
import retrofit2.http.GET
import retrofit2.http.Path


interface StreamerApiService {
    @GET("{apiEndpoint}")
    suspend fun getStreamers(@Path("apiEndpoint")apiEndpoint :String): Streamers

}