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
    fun getAllDoctors(): Flow<List<StreamerInfo>>

    @Query("SELECT * from streamers WHERE id = :id")
    fun getDoctor(id: String): Flow<StreamerInfo>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(doctorInfo: StreamerInfo)

    @Update
    suspend fun update(doctorInfo: StreamerInfo)

    @Delete
    suspend fun delete(doctorInfo: StreamerInfo)
}