package examen.streamers

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import examen.huisartsengent.components.assertCurrentRouteName
import examen.streamers.ui.screens.HomeDestination
import examen.streamers.ui.screens.StreamerDetailsDestination
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StreamerScreenNavigationTest {

    /**
     * Note: To access to an empty activity, the code uses ComponentActivity instead of
     * MainActivity.
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupStreamerNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            StreamersApp(navController = navController)
        }
    }

    @Test
    fun streamerNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(HomeDestination.route)
    }

    @Test
    fun streamerNavHost_verifyBackNavigationNotShownOnHomeScreen() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }

    @Test
    fun streamerNavHost_clickStreamerOnHomeScreen_navigatesToStreamerDetailsScreen() {
        val testTag = composeTestRule.activity.getString(R.string.testTag)

        composeTestRule.waitUntil(timeoutMillis = 15000)  {
            composeTestRule.onAllNodesWithTag(testTag = testTag)
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Click on first streamer in scrollable list
        composeTestRule.onAllNodesWithTag(testTag = testTag)
            .onFirst()
            .performClick()

        navController.assertCurrentRouteName(StreamerDetailsDestination.route)
    }

    @Test
    fun streamerNavHost_clickBackOnStreamerDetailsScreen_navigatesToHomeScreen() {
        navigateToStreamerDetailsScreen()
        performNavigateUp()
        navController.assertCurrentRouteName(HomeDestination.route)
    }

    // Click first streamer in scrollable list
    private fun navigateToStreamerDetailsScreen() {
        val testTag = composeTestRule.activity.getString(R.string.testTag)

        composeTestRule.waitUntil(timeoutMillis = 15000)  {
            composeTestRule.onAllNodesWithTag(testTag = testTag)
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Click on first streamer in scrollable list
        composeTestRule.onAllNodesWithTag(testTag = testTag)
            .onFirst()
            .performClick()
    }

    // Click back in app Top Bar
    private fun performNavigateUp() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).performClick()
    }

}