package examen.streamers.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import examen.streamers.StreamersApplication
import examen.streamers.data.RealTimeStreamerInfo
import examen.streamers.data.SpecialStreamers
import examen.streamers.data.StreamerInfo
import examen.streamers.data.StreamerInfoRepository
import examen.streamers.data.StreamersRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * Ui State set by Room
 */
data class StreamerUiState(val streamerList: List<StreamerInfo> = listOf())

/**
 * Other Ui State
 */
data class AppUiState(
    val selectedStreamer: StreamerInfo = SpecialStreamers.noStreamer,
    val synchronized: Boolean = false, // true when online and offline data fully synchronized
    val refreshCount: Long = 0
)

/**
 * class for the the view model
 *
 * @property streamerInfoRepository for the offline repository.
 * @property streamersRepository for the online repository.
 * @constructor creates the Streamers view model with a streamerInfoRepository, streamersRepository.
 */
class StreamersViewModel(
    val streamerInfoRepository: StreamerInfoRepository,
    val streamersRepository: StreamersRepository
) : ViewModel() {

    /**
     * Holds ui state. The list of streamers is retrieved from [StreamerInfoRepository] and mapped to
     * [StreamerUiState]
     */
    val streamerUiState: StateFlow<StreamerUiState> =
        streamerInfoRepository.getAllStreamersStream().map { StreamerUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = StreamerUiState()
            )

    /**
     * Holds ui state. holds a selected streamer received from [StreamerInfoRepository],
     * if the list is synchronised and the refresh count and mapped to
     * [AppUiState]
     */
    private val _uiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    var retrofitSuccessful: Boolean = false // flagging successful retrofit
    var realTimeStreamerInfo: List<RealTimeStreamerInfo> = mutableListOf()

    /**
     * Gets streamers information from the Streamers API Retrofit service
     * and updates the room database.
     */
    private fun getAllStreamers() = viewModelScope.launch {
        val streamerList =
            try {
                streamersRepository.getStreamersInfo()
            } catch (e: IOException) {
                listOf()
            } catch (e: HttpException) {
                listOf()
            }
        val usernames = streamerInfoRepository.getUsernames().toMutableSet()
        if (streamerList.isNotEmpty()) {
            retrofitSuccessful = true
            // Check if retrofitted streamer are identical in Room database
            streamerList.forEach {
                val unchanged = when {
                    usernames.contains(it.username) -> it == streamerInfoRepository.getStreamer(it.username)
                    else -> false
                }
                if (!unchanged)
                    streamerInfoRepository.insertStreamer(it) // replace with retrofitted streamer
                usernames.remove(it.username) // Remove primary key username for each retrofitted streamer
            }
            // Remove streamers corresponding to remaining usernames in usernames set
            usernames.forEach {
                streamerInfoRepository.deleteStreamer(streamerInfoRepository.getStreamer(it))
                Log.d("StreamersViewModel", "Streamer with username $it removed from DB")
            }
        } else {
            retrofitSuccessful = false
        }
        _uiState.update { currentState ->
            currentState.copy(synchronized = true)
        }
    }

    /**
     * Gets the selected streamer information from the Streamers API Retrofit service
     * @param username username of the streamer
     */
    fun selectStreamer(username: String) = viewModelScope.launch {
        val streamer = async {
            streamerInfoRepository.getStreamer(username)
        }
        _uiState.update { currentState ->
            currentState.copy(selectedStreamer = streamer.await())
        }
    }

    /**
     * function for setting the selected streamer to the special streamer(empty streamer)
     */
    // Sets de selected streamer in state to the empty streamer (no information)
    fun clearStreamer() {
        _uiState.update { currentState ->
            currentState.copy(
                selectedStreamer = SpecialStreamers.emptyStreamer
            )
        }
    }

    /**
     * Gets streamers information from the Streamers API Retrofit service
     * and change the refresh count to trigger recomposition
     */
    // Starts the real time monitoring of the live streamers and store in the view model
    private fun startRealTimeStreamerMonitor() = viewModelScope.launch {
        // Trigger the flow and consume its elements using collect
        streamersRepository.realTimeStreamer
            .catch { }
            .collect {
                realTimeStreamerInfo = it.toList()
                _uiState.update { currentState ->
                    currentState.copy(
                        refreshCount = currentState.refreshCount + 1
                    )
                }
            }
    }

    /**
     * Call getAllStreamers(), startRealTimeStreamerMonitor() on init so we can display the streamer with the live icon immediately.
     */
    init {
        getAllStreamers()
        startRealTimeStreamerMonitor()
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