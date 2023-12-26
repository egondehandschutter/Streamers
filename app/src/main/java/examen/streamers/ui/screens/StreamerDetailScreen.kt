package examen.streamers.ui.screens


import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import examen.streamers.R
import examen.streamers.StreamersTopAppBar
import examen.streamers.data.StreamerInfo
import examen.streamers.navigation.NavigationDestination
import examen.streamers.ui.theme.StreamersTheme

object StreamerDetailsDestination : NavigationDestination {
    override val route = "streamer_details"
    override val titleRes = R.string.streamer_detail_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreamerDetailsScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StreamersViewModel
) {
    val appUiState = viewModel.appUiState.collectAsState()
    Scaffold(
        topBar = {
            StreamersTopAppBar(
                title = stringResource(StreamerDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = navigateBack,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))

            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.app_name),
                )
            }
        }, modifier = modifier
    ) { innerPadding ->
        StreamerDetailsBody(
            appUiState = appUiState.value,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }


}

@Composable
fun StreamerDetailsBody(appUiState: AppUiState, modifier: Modifier) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        val streamer = appUiState.selectedStreamer
        val context = LocalContext.current
        StreamerDetails(
            streamer = streamer, modifier = Modifier.fillMaxWidth()
        )
    }

}

@Composable
fun StreamerDetails(streamer: StreamerInfo?, modifier: Modifier) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
            ) {
                StreamerDetailsRow(
                    labelResID = R.string.streamer,
                    streamerDetail = streamer?.username ?: "",
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(
                            id = R.dimen
                                .padding_medium
                        )
                    )
                )
                StreamerDetailsRow(
                    labelResID = R.string.twitchUrl,
                    streamerDetail = streamer?.twitchUrl ?: "",
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(
                            id = R.dimen
                                .padding_medium
                        )
                    )
                )
                StreamerDetailsRow(
                    labelResID = R.string.isCommunityStreamer,
                    streamerDetail = streamer?.isCommunityStreamer.toString() ?: "",
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(
                            id = R.dimen
                                .padding_medium
                        )
                    )
                )
                StreamerDetailsRow(
                    labelResID = R.string.url,
                    streamerDetail = streamer?.url ?: "",
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(
                            id = R.dimen
                                .padding_medium
                        )
                    )
                )
            }

        }
    }

@Composable
private fun StreamerDetailsRow(
    @StringRes labelResID: Int, streamerDetail: String?, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = "${stringResource(labelResID)}:")
        Spacer(modifier = Modifier.weight(1f))
        Text(text = streamerDetail ?: "", fontWeight = FontWeight.Bold)
    }
}






