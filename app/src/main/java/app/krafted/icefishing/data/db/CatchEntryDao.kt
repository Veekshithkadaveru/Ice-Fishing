package app.krafted.icefishing.data.db

import androidx.room.*
import app.krafted.icefishing.data.db.entities.CatchEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface CatchEntryDao {
    @Query("SELECT * FROM catch_log ORDER BY date DESC")
    fun getAll(): Flow<List<CatchEntry>>

    @Query("SELECT * FROM catch_log WHERE fishSpecies = :species ORDER BY date DESC")
    fun getBySpecies(species: String): Flow<List<CatchEntry>>

    @Query("SELECT * FROM catch_log WHERE date BETWEEN :from AND :to ORDER BY date DESC")
    fun getByDateRange(from: Long, to: Long): Flow<List<CatchEntry>>

    @Insert
    suspend fun insert(entry: CatchEntry)

    @Delete
    suspend fun delete(entry: CatchEntry)
}
