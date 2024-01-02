package examen.streamers.network

import examen.streamers.model.Streamers
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service object for creating api calls
 */
interface StreamerApiService {
    /**
     * function for creating retrofit api calls
     * @property apiEndpoint is the endpoint for the base url.
     * @constructor creates the retrofit connection
     */
    @GET("{apiEndpoint}")
    suspend fun getStreamers(@Path("apiEndpoint")apiEndpoint :String): Streamers

}