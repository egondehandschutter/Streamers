package examen.streamers.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import examen.streamers.R
import examen.streamers.StreamersTopAppBar
import examen.streamers.data.RealTimeStreamerInfo
import examen.streamers.data.StreamerInfo
import examen.streamers.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StreamersViewModel
) {
    val streamerUiState by viewModel.streamerUiState.collectAsState()

    val appUiState by viewModel.appUiState.collectAsState()
    val variabele = appUiState.refreshCount
    //Log.d("t",variabele.toString())

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val realTimeStreamer = viewModel.realTimeStreamerInfo

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StreamersTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },

    ) { innerPadding ->

        HomeBody(
            realTimeStreamerList = realTimeStreamer,
            streamerList = streamerUiState.streamerList,
            onItemClick = navigateToDetails,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )

}


}

@Composable
fun HomeBody(
    realTimeStreamerList: List<RealTimeStreamerInfo>,
    streamerList: List<StreamerInfo>,
    onItemClick: (String) -> Unit,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        StreamerList(
            realTimeStreamerList = realTimeStreamerList,
            streamerList = streamerList,
            onItemClick = { onItemClick(it.username) },
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
        )
    }
    }

@Composable
fun StreamerList(
    realTimeStreamerList: List<RealTimeStreamerInfo>,
    streamerList: List<StreamerInfo>,
    onItemClick: (StreamerInfo) -> Unit,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier) {

        items(items = streamerList, key = { it.username }) { streamer ->
            StreamerItem(
                streamer = streamer,
                realTimeStreamerList = realTimeStreamerList,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(streamer) }
                    )
        }
    }
}



@Composable
fun StreamerItem(
    streamer: StreamerInfo,
    realTimeStreamerList: List<RealTimeStreamerInfo>,
    modifier: Modifier
) {
    val username = streamer.username
    var isStreamerLive = false
    realTimeStreamerList.forEach{
        if (it.username == username){
            isStreamerLive = it.isLive
        }
    }
    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_large))
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Text(
                text = streamer.username,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = streamer.twitchUrl,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = isStreamerLive.toString(),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }


}







