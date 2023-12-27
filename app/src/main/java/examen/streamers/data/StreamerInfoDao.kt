package examen.streamers.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StreamerInfoDao {
    @Query("SELECT * from streamers")
    fun getAllStreamers(): Flow<List<StreamerInfo>>

    @Query("SELECT * from streamers WHERE username = :username")
    //fun getStreamer(username: String): Flow<StreamerInfo>
    suspend fun getStreamer(username: String): StreamerInfo

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(streamerInfo: StreamerInfo)

    @Update
    suspend fun update(streamerInfo: StreamerInfo)

    @Delete
    suspend fun delete(streamerInfo: StreamerInfo)
}