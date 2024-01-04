package examen.streamers

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import examen.streamers.data.StreamerInfo
import examen.streamers.data.StreamerInfoDao
import examen.streamers.data.StreamersDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@Suppress("SpellCheckingInspection")
class StreamerInfoDaoTest {
    private lateinit var streamerInfoDao: StreamerInfoDao
    private lateinit var streamersDatabase: StreamersDatabase
    // Declaration of first streamer
    private val streamerInfoOne = StreamerInfo(
        username = "chess24",
        avatar = "https://images.chesscomfiles.com/uploads/v1/user/302880433.dc8aca73.50x50o.5dafc7607ff6.png",
        twitchUrl = "https://twitch.tv/chess24",
        url = "https://www.chess.com/member/Chess24",
        isCommunityStreamer = true
    )
    // Declaration of second streamer
    private val streamerInfoTwo = StreamerInfo(
        username = "DanielNaroditsky",
        avatar = "https://images.chesscomfiles.com/uploads/v1/user/1715324.840b7522.50x50o.71a0c2d59885.jpg",
        twitchUrl = "https://twitch.tv/gmnaroditsky",
        url = "https://www.chess.com/member/DanielNaroditsky",
        isCommunityStreamer = false
    )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        streamersDatabase = Room.inMemoryDatabaseBuilder(context, StreamersDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        streamerInfoDao = streamersDatabase.streamerInfoDao()
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        streamersDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsStreamerIntoDB() = runBlocking {
        addOneStreamerToDb()
        val allStreamers = streamerInfoDao.getAllStreamers().first() // Collect first Room emit
        assertEquals(allStreamers[0], streamerInfoOne)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllStreamers_returnsAllStreamersFromDB() = runBlocking {
        addTwoStreamersToDb()
        val allStreamers = streamerInfoDao.getAllStreamers().first() // Collect first Room emit
        // Do not make assumptions about list order
        Assert.assertTrue(allStreamers.containsAll(listOf(streamerInfoOne, streamerInfoTwo)))
    }

    @Test
    @Throws(Exception::class)
    fun daoGetStreamer_returnsStreamerFromDB() = runBlocking {
        addOneStreamerToDb()
        val streamerInfo = streamerInfoDao.getStreamer( username = streamerInfoOne.username)
        assertEquals(streamerInfo, streamerInfoOne)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetUsernames_returnsKeysFromDB() = runBlocking {
        addTwoStreamersToDb()
        val streamerUsernames = streamerInfoDao.getUsernames()
        // Do not make assumptions about list order
        Assert.assertTrue(streamerUsernames.containsAll(listOf(streamerInfoOne.username, streamerInfoTwo.username)))
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteStreamers_deletesAllStreamersFromDB() = runBlocking {
        addTwoStreamersToDb()
        streamerInfoDao.delete(streamerInfoOne)
        streamerInfoDao.delete(streamerInfoTwo)
        val allStreamers = streamerInfoDao.getAllStreamers().first() // Collect first Room emit
        Assert.assertTrue(allStreamers.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateStreamers_updatesStreamersInDB() = runBlocking {
        // Make sure the id primary key property is the same as for the first streamer
        val newStreamerInfoOne = streamerInfoOne.copy(
            url = "https://www.chess.com/member/new1",
            isCommunityStreamer = false
        )
        // Make sure the id primary key property is the same as for the second streamer
        val newStreamerInfoTwo = streamerInfoTwo.copy(
            url = "https://www.chess.com/member/new2",
            isCommunityStreamer = true
        )
        addTwoStreamersToDb()
        streamerInfoDao.update(newStreamerInfoOne)
        streamerInfoDao.update(newStreamerInfoTwo)
        val allStreamers = streamerInfoDao.getAllStreamers().first() // Collect first Room emit
        // Do not make assumptions about list order
        Assert.assertTrue(allStreamers.containsAll(listOf(newStreamerInfoOne, newStreamerInfoTwo)))
    }


    // Add first streamer to database
    private suspend fun addOneStreamerToDb() {
        streamerInfoDao.insert(streamerInfoOne)
    }

    // Add first and second streamer to database
    private suspend fun addTwoStreamersToDb() {
        streamerInfoDao.insert(streamerInfoOne)
        streamerInfoDao.insert(streamerInfoTwo)
    }

}