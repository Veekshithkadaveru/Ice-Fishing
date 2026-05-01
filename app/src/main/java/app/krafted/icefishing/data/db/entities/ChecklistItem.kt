package app.krafted.icefishing.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_items")
data class ChecklistItem(
    @PrimaryKey val itemId: String,
    val checked: Boolean = false
)
