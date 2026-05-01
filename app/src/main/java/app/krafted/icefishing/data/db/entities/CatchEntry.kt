package app.krafted.icefishing.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catch_log")
data class CatchEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val fishSpecies: String,
    val count: Int,
    val depth: Int,
    val baitUsed: String,
    val waterTemp: Int?,
    val notes: String,
    val sessionId: String
)
