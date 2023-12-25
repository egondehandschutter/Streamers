package examen.streamers


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import examen.streamers.ui.screens.StreamersViewModel


@Composable
fun StreamersApp(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val streamersViewModel: StreamersViewModel =
        viewModel(factory = StreamersViewModel.Factory)
    val streamerUiState by streamersViewModel.streamerUiState.collectAsState()
    LazyColumn {
        items(items = streamerUiState.streamerList, key = { it.id}){ streamerInfo ->
            Text(
                text = streamerInfo.username,
                modifier = Modifier

            )
        }
    }
}
