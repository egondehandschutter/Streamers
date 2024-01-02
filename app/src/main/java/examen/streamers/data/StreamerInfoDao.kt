package examen.streamers.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Streamer database
 */
@Dao
interface StreamerInfoDao {
    /**
     * gets all the streamer info.
     * @return list with all the StreamerInfo.
     */
    // Retrieve streamers as a flow
    @Query("SELECT * from streamers")
    fun getAllStreamers(): Flow<List<StreamerInfo>>

    /**
     * gets the streamer info with the username.
     * @param username username of a streamer.
     * @return The correct streamer info.
     */
    // Retrieve one streamer based on his/her primary key
    @Query("SELECT * from streamers WHERE username = :username")
    //fun getStreamer(username: String): Flow<StreamerInfo>
    suspend fun getStreamer(username: String): StreamerInfo

    /**
     * gets all the usernames from every streamer.
     * @return list with all the StreamerInfo.
     */
    // Retrieve the list of primary keys (usernames) which are distinct by nature
    @Query("SELECT username from streamers")
    suspend fun getUsernames(): List<String>


    /**
     * Insert a [streamerInfo] to the database.
     */
    // Room convenience function for insertion
    // Specify the conflict strategy as REPLACE, when the user tries to add an
    // existing streamer into the database. Room solves the conflict by replacement.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(streamerInfo: StreamerInfo)


    /**
     * update a [streamerInfo] to the database.
     */
    // Room convenience function for updates
    @Update
    suspend fun update(streamerInfo: StreamerInfo)

    /**
     * delete a [streamerInfo] to the database.
     */
    // Room convenience function for deletion
    @Delete
    suspend fun delete(streamerInfo: StreamerInfo)
}