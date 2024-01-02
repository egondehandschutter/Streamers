package examen.streamers.ui.screens

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import examen.streamers.R
import examen.streamers.StreamersTopAppBar
import examen.streamers.data.RealTimeStreamerInfo
import examen.streamers.data.SpecialStreamers
import examen.streamers.data.StreamerInfo
import examen.streamers.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Home screen
 * Composable that displays a streamer app bar and a homebody.
 *
 */
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

    val synchronized = appUiState.synchronized
    val realTimeStreamer = viewModel.realTimeStreamerInfo

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StreamersTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->

        HomeBody(
            realTimeStreamerList = realTimeStreamer,
            streamerList = streamerUiState.streamerList,
            onItemClick = navigateToDetails,
            synchronized = synchronized,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

/**
 * Composable that displays the streamer list.
 *
 */
@VisibleForTesting
@Composable
internal fun HomeBody(
    realTimeStreamerList: List<RealTimeStreamerInfo>,
    streamerList: List<StreamerInfo>,
    onItemClick: (String) -> Unit,
    synchronized: Boolean,
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
            synchronized = synchronized,
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
        )
    }
 }

/**
 * Composable that displays a list of streamers.
 *
 */
@Composable
fun StreamerList(
    realTimeStreamerList: List<RealTimeStreamerInfo>,
    streamerList: List<StreamerInfo>,
    onItemClick: (StreamerInfo) -> Unit,
    synchronized: Boolean,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier) {
        val liveUsernames = realTimeStreamerList.filter { it.isLive } .map { it.username }
        val filteredListOne = streamerList.filter { liveUsernames.contains(it.username)}
        val filteredListTwo = streamerList.filter { !(liveUsernames.contains(it.username))}
        var sortedList = filteredListOne + filteredListTwo

        val isEmptyList = sortedList.isEmpty()
        if (isEmptyList) {
            sortedList = if (synchronized)
                listOf(SpecialStreamers.noStreamer)
            else
                listOf(SpecialStreamers.startStreamer)
        }

        items(items = sortedList, key = { it.username }) { streamer ->
            StreamerItem(
                streamer = streamer,
                realTimeStreamerList = realTimeStreamerList,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(streamer) }
                    .testTag(stringResource(R.string.testTag))
            )
        }
    }
}


/**
 * Composable that displays a list item containing a streamer icon and teh streamer information.
 *
 * @param streamer contains the data that populates the list item
 * @param modifier modifiers to set to this composable
 */
@Composable
fun StreamerItem(
    streamer: StreamerInfo,
    realTimeStreamerList: List<RealTimeStreamerInfo>,
    modifier: Modifier
) {
    val username = streamer.username
    var isStreamerLive = false
    realTimeStreamerList.forEach {
        if (it.username == username) {
            isStreamerLive = it.isLive
        }
    }

    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            StreamerIcon(
                avatar = streamer.avatar,
                modifier = Modifier
            )
            StreamerInformation(
                username = streamer.username,
                isStreamerLive = isStreamerLive,
                modifier = Modifier
            )
        }
    }
}

/**
 * Composable that displays the image in the list item.
 *
 * @param avatar contains the url for the image
 * @param modifier modifiers to set to this composable
 */
@Composable
fun StreamerIcon(
    avatar: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        modifier = modifier
            .size(dimensionResource(R.dimen.image_size))
            .padding(dimensionResource(R.dimen.padding_small))
            .clip(MaterialTheme.shapes.small),
        contentScale = ContentScale.Crop,
        model = ImageRequest.Builder(LocalContext.current)
            .data(avatar)
            .build(),
        contentDescription = "Photo of the streamer"
    )
}

/**
 * Composable that displays the username and the live icon in the list item.
 *
 * @param username contains the username of the streamer
 * @param modifier modifiers to set to this composable
 */
@Composable
fun StreamerInformation(
    username: String,
    isStreamerLive: Boolean,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = username,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
        )
        if (isStreamerLive) {
            Image(
                modifier = modifier
                    .size(dimensionResource(R.dimen.image_size))
                    .padding(dimensionResource(R.dimen.padding_small))
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop,
                painter =
                painterResource(id = R.drawable.live),
                contentDescription = stringResource(id = R.string.live_content_description),
            )
        }
    }
}
















