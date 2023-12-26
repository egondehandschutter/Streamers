package examen.streamers.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import examen.streamers.ui.screens.HomeDestination
import examen.streamers.ui.screens.HomeScreen
import examen.streamers.ui.screens.StreamerDetailsDestination
import examen.streamers.ui.screens.StreamerDetailsScreen
import examen.streamers.ui.screens.StreamersViewModel

@Composable
fun StreamerNavHost(
    navController: NavHostController,
    viewModel: StreamersViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToDetails = {
                    viewModel.selectStreamer(it)
                    navController.navigate("${StreamerDetailsDestination.route}/${it}")
                },
                viewModel = viewModel
            )
        }
        composable(
            route = StreamerDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(StreamerDetailsDestination.itemIdArg) {
                type = NavType.StringType
            })
        ) {
            StreamerDetailsScreen(
                navigateBack = { navController.navigateUp() },
                viewModel = viewModel
            )
        }
    }
}