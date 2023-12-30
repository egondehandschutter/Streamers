package examen.streamers.navigation

import android.view.SoundEffectConstants
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import examen.streamers.data.SpecialStreamers
import examen.streamers.ui.screens.HomeDestination
import examen.streamers.ui.screens.HomeScreen
import examen.streamers.ui.screens.StreamerDetailsDestination
import examen.streamers.ui.screens.StreamerDetailsScreen
import examen.streamers.ui.screens.StreamersViewModel

/**
 * Provides Navigation graph for the application.
 * The application has a home, filter and streamer details screen
 * A bidirectional transition between home and filter screen is possible,
 * as well as a bidirectional transition between home and streamer details screen.
 * Click sounds are generated in the click callbacks provided
 *
 */
@Composable
fun StreamerNavHost(
    navController: NavHostController,
    viewModel: StreamersViewModel,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToDetails = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    if (it != SpecialStreamers.startStreamer.username && it != SpecialStreamers.noStreamer.username)
                        viewModel.selectStreamer(it)
                    else
                        viewModel.clearStreamer()
                    navController.navigate(StreamerDetailsDestination.route)
                },
                viewModel = viewModel
            )
        }
        composable(route = StreamerDetailsDestination.route) {
            StreamerDetailsScreen(
                navigateBack = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    navController.navigateUp() },
                viewModel = viewModel
            )
        }
    }
}