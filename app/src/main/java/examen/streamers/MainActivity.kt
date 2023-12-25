package examen.streamers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import examen.streamers.data.AppContainer
import examen.streamers.data.DefaultAppContainer
import examen.streamers.data.StreamerInfo
import examen.streamers.data.StreamersRepository
import examen.streamers.ui.theme.StreamersTheme
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StreamersTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StreamersApp()
                }
            }
        }
    }
}
/*@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val container: AppContainer = DefaultAppContainer()
    val streamersRepository: StreamersRepository = container.streamersRepository
    val allStreamersInfo: List<StreamerInfo>
    runBlocking {
        allStreamersInfo = streamersRepository.getStreamersInfo()
    }
    LazyColumn {
        items(allStreamersInfo) { streamerInfo ->
            Text(
                text = streamerInfo.username,
                modifier = modifier
            )
        }}
}
*/



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StreamersTheme {
        StreamersApp()
        }
    }
