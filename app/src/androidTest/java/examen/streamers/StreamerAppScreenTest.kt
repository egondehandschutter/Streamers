package examen.streamers

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import examen.streamers.data.RealTimeStreamerInfo
import examen.streamers.data.SpecialStreamers
import examen.streamers.data.StreamerInfo
import examen.streamers.ui.screens.AppUiState
import examen.streamers.ui.screens.HomeBody
import examen.streamers.ui.screens.StreamerDetailsBody
import org.junit.Rule
import org.junit.Test

class StreamerAppScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // Declare first streamer
    private val streamerOne = StreamerInfo(
        username = "chess24",
        avatar = "https://images.chesscomfiles.com/uploads/v1/user/302880433.dc8aca73.50x50o.5dafc7607ff6.png",
        twitchUrl = "https://twitch.tv/chess24",
        url = "https://www.chess.com/member/Chess24",
        isCommunityStreamer = false
    )

    // Declaration of second streamer
    private val streamerTwo = StreamerInfo(
        username = "DanielNaroditsky",
        avatar = "https://images.chesscomfiles.com/uploads/v1/user/1715324.840b7522.50x50o.71a0c2d59885.jpg",
        twitchUrl = "",
        url = "https://www.chess.com/member/DanielNaroditsky",
        isCommunityStreamer = false
    )

    private val streamerThree = StreamerInfo(
        username = "ChessBrah",
        avatar = "https://images.chesscomfiles.com/uploads/v1/user/2555939.974bf39b.50x50o.c90724e0b767.png",
        twitchUrl = "https://twitch.tv/chessbrah",
        url = "https://www.chess.com/member/ChessBrah",
        isCommunityStreamer = true
    )

    private val realTimeStreamerOne = RealTimeStreamerInfo(
        username = "chess24",
        isLive = false
    )

    private val realTimeStreamerTwo = RealTimeStreamerInfo(
        username = "DanielNaroditsky",
        isLive = false
    )

    private val realTimeStreamerThree = RealTimeStreamerInfo(
        username = "ChessBrah",
        isLive = true
    )

    // Declare list of streamers containing first and second streamer
    private val streamers = listOf(streamerOne, streamerTwo)

    // Declare list of streamers containing first and second streamer
    private val realTimeStreamers = listOf(realTimeStreamerOne, realTimeStreamerTwo)

    // Declare list of streamers containing first and second streamer
    private val streamersTwo = listOf(streamerOne, streamerTwo, streamerThree)

    // Declare list of streamers containing first and second streamer
    private val realTimeStreamersTwo = listOf(realTimeStreamerOne, realTimeStreamerTwo, realTimeStreamerThree)


    // Declare first App Ui State with empty filters and first streamer selected
    private val appUiStateOne = AppUiState(
        selectedStreamer = streamerOne
    )

    // Declare second App Ui State with empty filters and "empty" streamer selected
    private val appUiStateTwo = AppUiState(
        selectedStreamer = SpecialStreamers.emptyStreamer
    )

    private val appUiStateThree = AppUiState(
        selectedStreamer = streamerTwo
    )

    @Test
    fun homeScreen_verifyContent_noLive() {
        // When HomeScreen is loaded
        composeTestRule.setContent {
            HomeBody(
                realTimeStreamerList = realTimeStreamers,
                streamerList = streamers,
                onItemClick = { },
                synchronized = true,
                Modifier
            )
        }
        // Check whether both streamers are in the scrollable list
        // and are clickable
        composeTestRule.onNodeWithText(streamerTwo.username)
            .assertIsDisplayed()
            .assertHasClickAction()
        composeTestRule.onNodeWithText(streamerOne.username)
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.live_content_description))
            .assertDoesNotExist()
    }

    @Test
    fun homeScreen_verifyContent_oneLive() {
        // When HomeScreen is loaded
        composeTestRule.setContent {
            HomeBody(
                realTimeStreamerList = realTimeStreamersTwo,
                streamerList = streamersTwo,
                onItemClick = { },
                synchronized = true,
                Modifier
            )
        }
        // Check whether both streamers are in the scrollable list
        // and are clickable
        composeTestRule.onNodeWithText(streamerThree.username)
            .assertIsDisplayed()
            .assertHasClickAction()
        composeTestRule.onNodeWithText(streamerTwo.username)
            .assertIsDisplayed()
            .assertHasClickAction()
        composeTestRule.onNodeWithText(streamerOne.username)
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.live_content_description))
            .assertExists()

        val testTag = composeTestRule.activity.getString(R.string.testTag)

        composeTestRule.waitUntil(timeoutMillis = 15000)  {
            composeTestRule.onAllNodesWithTag(testTag = testTag)
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Click on first streamer in scrollable list
        composeTestRule.onAllNodesWithTag(testTag = testTag)
            .onFirst()
            .assertTextEquals(streamerThree.username)
    }

    @Test
    fun streamerDetailsScreen_verifyContent_streamerExists_withTwitchUrl() {
        // When StreamerDetailsScreen is loaded
        // and the first streamer is selected
        composeTestRule.setContent {
            StreamerDetailsBody(
                appUiState = appUiStateOne,
                Modifier
            )
        }
        // Check if twitch url intent button is displayed, enabled and clickable
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.toTwitchUrl))
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()

        // Check if url intent button is displayed, enabled and clickable
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.toUrl))
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()

        // Check if the details of the first streamer are displayed
        // but cannot be clicked
        composeTestRule.onNodeWithText(streamerOne.username)
            .assertIsDisplayed()
            .assertHasNoClickAction()
    }

    @Test
    fun streamerDetailsScreen_verifyContent_streamerIsEmpty() {   //error path
        // When StreamerDetailsScreen is loaded
        // and the "empty" streamer is selected
        composeTestRule.setContent {
            StreamerDetailsBody(
                appUiState = appUiStateTwo,
                Modifier
            )
        }
        // Check if twitchurl intent button is displayed
        // but not enabled
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.toTwitchUrl))
            .assertIsDisplayed()
            .assertIsNotEnabled()

        // Check if url intent button is displayed
        // but not enabled
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.toTwitchUrl))
            .assertIsDisplayed()
            .assertIsNotEnabled()


        // Check if empty streamer details are displayed
        // and cannot be clicked
        composeTestRule.onNodeWithText(
            text = composeTestRule.activity.getString(R.string.isCommunityStreamer),
            substring = true
        )
            .assertIsDisplayed()
            .assertHasNoClickAction()
    }

    @Test
    fun streamerDetailsScreen_verifyContent_streamerExists_withoutTwitchUrl() {
        // When StreamerDetailsScreen is loaded
        // and the first streamer is selected
        composeTestRule.setContent {
            StreamerDetailsBody(
                appUiState = appUiStateThree,
                Modifier
            )
        }
        // Check if twitch url intent button is displayed, enabled and clickable
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.toTwitchUrl))
            .assertIsDisplayed()
            .assertIsNotEnabled()
            .assertHasClickAction()

        // Check if url intent button is displayed, enabled and clickable
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.toUrl))
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()

        // Check if the details of the first streamer are displayed
        // but cannot be clicked
        composeTestRule.onNodeWithText(streamerTwo.username)
            .assertIsDisplayed()
            .assertHasNoClickAction()
    }
}