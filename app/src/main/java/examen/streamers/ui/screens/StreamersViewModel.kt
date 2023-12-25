package examen.streamers.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import examen.streamers.StreamersApplication
import examen.streamers.data.StreamerInfo
import examen.streamers.data.StreamerInfoRepository
import examen.streamers.data.StreamersRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class StreamerUiState(val streamerList: List<StreamerInfo> = listOf())

class StreamersViewModel(
    val streamerInfoRepository: StreamerInfoRepository,
    val streamersRepository: StreamersRepository
) : ViewModel() {
    /**
     * Holds ui state. The list of doctors is retrieved from [StreamerInfoRepository] and mapped to
     * [StreamerUiState]
     */
    val streamerUiState: StateFlow<StreamerUiState> =
        streamerInfoRepository.getAllStreamersStream().map { StreamerUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = StreamerUiState()
            )

    private suspend fun getAllStreamers() = coroutineScope {
        val streamers = async {
            try {
                streamersRepository.getStreamersInfo()
            } catch (e: IOException) {
                listOf()
            } catch (e: HttpException) {
                listOf()
            }
        }
        streamers.await().forEach { streamerInfoRepository.insertStreamer(it) }
    }

    init {
        Log.d("StreamerViewModel", "Init")
        viewModelScope.launch { getAllStreamers() }

    }

    override fun onCleared() {
        Log.d("HomeViewModel", "Cleared")
        super.onCleared()
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StreamersApplication)
                val streamerInfoRepository = application.container.streamerInfoRepository
                val streamersRepository = application.container.streamersRepository
                StreamersViewModel(streamerInfoRepository = streamerInfoRepository, streamersRepository = streamersRepository )
            }
        }
    }
}