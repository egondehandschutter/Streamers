package examen.streamers.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import examen.streamers.network.StreamerApiService
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType
import android.content.Context


interface AppContainer {
    val streamersRepository: StreamersRepository
    val streamerInfoRepository: StreamerInfoRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {
    private val baseUrl = "https://api.chess.com/pub/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: StreamerApiService by lazy {
        retrofit.create(StreamerApiService::class.java)
    }

    /**
     * DI implementation for Mars photos repository
     */
    override val streamersRepository by lazy {
        NetworkStreamersRepository(retrofitService)
    }

    override val streamerInfoRepository: StreamerInfoRepository by lazy {
        OfflineStreamerInfoRepository(StreamersDatabase.getDatabase(context).streamerInfoDao())
    }
}