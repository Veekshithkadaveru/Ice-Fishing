package app.krafted.icefishing.data.db

import androidx.room.*
import app.krafted.icefishing.data.db.entities.ChecklistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {
    @Query("SELECT * FROM checklist_items")
    fun getAll(): Flow<List<ChecklistItem>>

    @Upsert
    suspend fun upsert(item: ChecklistItem)

    @Query("UPDATE checklist_items SET checked = 0")
    suspend fun resetAll()
}
