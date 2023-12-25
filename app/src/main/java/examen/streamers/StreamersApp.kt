package examen.streamers


import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import examen.streamers.navigation.StreamerNavHost
import examen.streamers.ui.screens.StreamersViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StreamersApp(navController: NavHostController = rememberNavController()) {
    val streamersViewModel: StreamersViewModel =
        viewModel(factory = StreamersViewModel.Factory)
    StreamerNavHost(navController = navController, viewModel = streamersViewModel )
}


    /**
     * App bar to display title and conditionally display the back navigation.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun StreamersTopAppBar(
        title: String,
        canNavigateBack: Boolean,
        modifier: Modifier = Modifier,
        scrollBehavior: TopAppBarScrollBehavior? = null,
        navigateUp: () -> Unit = {}
    ) {
        CenterAlignedTopAppBar(
            title = { Text(title) },
            modifier = modifier,
            scrollBehavior = scrollBehavior,
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            }
        )}

