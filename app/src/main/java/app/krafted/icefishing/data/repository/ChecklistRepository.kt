package app.krafted.icefishing.data.repository

import android.content.Context
import app.krafted.icefishing.data.db.ChecklistDao
import app.krafted.icefishing.data.db.entities.ChecklistItem
import app.krafted.icefishing.data.model.ChecklistItemData
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class ChecklistRepository(
    private val context: Context,
    private val gson: Gson,
    private val checklistDao: ChecklistDao
) {
    private var cachedItems: List<ChecklistItemData>? = null

    suspend fun getItems(): List<ChecklistItemData> {
        if (cachedItems == null) {
            cachedItems = loadJsonAsset<List<ChecklistItemData>>(context, gson, "checklist.json")
        }
        return cachedItems!!
    }

    fun getCheckedStates(): Flow<List<ChecklistItem>> = checklistDao.getAll()

    suspend fun setItemChecked(itemId: String, checked: Boolean) {
        checklistDao.upsert(ChecklistItem(itemId = itemId, checked = checked))
    }

    suspend fun resetAll() {
        checklistDao.resetAll()
    }
}
