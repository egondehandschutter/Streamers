package examen.streamers

import androidx.activity.ComponentActivity
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
import examen.streamers.ui.screens.HomeBody
import examen.streamers.ui.screens.StreamerDetailsBody
import org.junit.Rule
import org.junit.Test

@Suppress("SpellCheckingInspection")
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

    // Declaration of third streamer
    private val streamerThree = StreamerInfo(
        username = "ChessBrah",
        avatar = "https://images.chesscomfiles.com/uploads/v1/user/2555939.974bf39b.50x50o.c90724e0b767.png",
        twitchUrl = "https://twitch.tv/chessbrah",
        url = "https://www.chess.com/member/ChessBrah",
        isCommunityStreamer = true
    )

    // Declare first real time streamer
    private val realTimeStreamerOne = RealTimeStreamerInfo(
        username = streamerOne.username,
        isLive = false
    )

    // Declaration of second real time streamer
    private val realTimeStreamerTwo = RealTimeStreamerInfo(
        username = streamerTwo.username,
        isLive = false
    )

    // Declaration of third real time streamer
    private val realTimeStreamerThree = RealTimeStreamerInfo(
        username = streamerThree.username,
        isLive = true
    )

    // Declare list of streamers containing first and second streamer
    private val streamersOne = listOf(streamerOne, streamerTwo)

    // Declare list of real time streamers containing first
    // and second real time streamer
    private val realTimeStreamersOne = listOf(
        realTimeStreamerOne,
        realTimeStreamerTwo
    )

    // Declare list of streamers containing first,
    // second and third streamer
    private val streamersTwo = listOf(
        streamerOne,
        streamerTwo,
        streamerThree
    )

    // Declare empty list (with streamerInfo)
    private val streamersEmpty : List<StreamerInfo> = emptyList()

    // Declare list of real time streamers containing first, second
    // and third real time streamer
    private val realTimeStreamersTwo = listOf(
        realTimeStreamerOne,
        realTimeStreamerTwo,
        realTimeStreamerThree
    )

    @Test
    fun homeScreen_verifyContent_noLiveStreamers() {
        // When HomeScreen is loaded
        composeTestRule.setContent {
            HomeBody(
                realTimeStreamerList = realTimeStreamersOne,
                streamerList = streamersOne,
                onItemClick = { },
                synchronized = true,
                realTimeSynchronized = true
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
    fun homeScreen_verifyContent_oneLiveStreamer() {
        // When HomeScreen is loaded
        composeTestRule.setContent {
            HomeBody(
                realTimeStreamerList = realTimeStreamersTwo,
                streamerList = streamersTwo,
                onItemClick = { },
                synchronized = true,
                realTimeSynchronized = true
            )
        }
        // Check whether the streamers are in the scrollable list
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

        // Check if the first streamer in the scrollable list
        // is the live streamer
        composeTestRule.onAllNodesWithTag(testTag = testTag)
            .onFirst()
            .assertTextEquals(streamerThree.username)
    }

    @Test
    fun homeScreen_verifyContent_noStreamers() { //error path
        // When HomeScreen is loaded
        composeTestRule.setContent {
            HomeBody(
                realTimeStreamerList = realTimeStreamersOne,
                streamerList = streamersEmpty,
                onItemClick = { },
                synchronized = true,
                realTimeSynchronized = true
            )
        }
        // Check if the scrolllable list shows the no streamers found message
        // which is clickable
        composeTestRule.onNodeWithText(SpecialStreamers.noStreamer.username)
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun homeScreen_verifyContent_startStreamers_synchronizedFalse_realTimeSynchronizedFalse() {
        // When HomeScreen is loaded
        composeTestRule.setContent {
            HomeBody(
                realTimeStreamerList = realTimeStreamersOne,
                streamerList = streamersOne,
                onItemClick = { },
                synchronized = false,
                realTimeSynchronized = false
            )
        }
        // Check if the scrolllable list shows the please wait message
        // which is clickable
        composeTestRule.onNodeWithText(SpecialStreamers.startStreamer.username)
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun homeScreen_verifyContent_startStreamers_synchronizedTrue_realTimeSynchronizedFalse() {
        // When HomeScreen is loaded
        composeTestRule.setContent {
            HomeBody(
                realTimeStreamerList = realTimeStreamersOne,
                streamerList = streamersTwo,
                onItemClick = { },
                synchronized = true,
                realTimeSynchronized = false
            )
        }
        // Check if the scrolllable list shows the please wait message
        // which is clickable
        composeTestRule.onNodeWithText(SpecialStreamers.startStreamer.username)
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun streamerDetailsScreen_verifyContent_streamerExists_withTwitchUrl() {
        // When StreamerDetailsScreen is loaded
        // and the first streamer is selected
        composeTestRule.setContent {
            StreamerDetailsBody(
                streamer = streamerOne
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
                streamer = SpecialStreamers.emptyStreamer
            )
        }
        // Check if twitch url intent button is displayed
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
        // and the second streamer is selected
        composeTestRule.setContent {
            StreamerDetailsBody(
                streamer = streamerTwo
            )
        }
        // Check if twitch url intent button is displayed,
        // and not enabled
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.toTwitchUrl))
            .assertIsDisplayed()
            .assertIsNotEnabled()

        // Check if url intent button is displayed, enabled and clickable
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.toUrl))
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()

        // Check if the details of the second streamer are displayed
        // but cannot be clicked
        composeTestRule.onNodeWithText(streamerTwo.username)
            .assertIsDisplayed()
            .assertHasNoClickAction()
    }
}

