package examen.streamers.navigation

import android.view.SoundEffectConstants
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
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
 * The application has a home and streamer details screen,
 * a bidirectional transition between home and streamer details screen.
 * Click sounds are generated in the click callbacks provided.
 * @property navController the nav host  controller.
 * @property viewModel the streamers view model.
 * @constructor creates the streamer nav host.
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
        composable(route = HomeDestination.route,
            enterTransition = {
                return@composable fadeIn(tween(1000))
            }, exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(700))
            }
            ) {
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
        composable(route = StreamerDetailsDestination.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                )
            },
            exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                )
            }
            ) {
            StreamerDetailsScreen(
                navigateBack = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    navController.navigateUp() },
                viewModel = viewModel
            )
        }
    }
}