package app.krafted.icefishing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.icefishing.data.db.AppDatabase
import app.krafted.icefishing.data.model.ChecklistItemData
import app.krafted.icefishing.data.repository.ChecklistRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ChecklistRow(
    val id: String,
    val name: String,
    val checked: Boolean
)

data class ChecklistGroup(
    val groupId: String,
    val groupName: String,
    val items: List<ChecklistRow>
) {
    val checkedCount: Int = items.count { it.checked }
    val totalCount: Int = items.size
}

data class ChecklistUiState(
    val groups: List<ChecklistGroup> = emptyList(),
    val totalChecked: Int = 0,
    val totalItems: Int = 0
)

class ChecklistViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ChecklistRepository(
        context = application.applicationContext,
        gson = Gson(),
        checklistDao = AppDatabase.getInstance(application.applicationContext).checklistDao()
    )

    private val itemsFlow = MutableStateFlow<List<ChecklistItemData>>(emptyList())

    val state: StateFlow<ChecklistUiState> =
        combine(itemsFlow, repository.getCheckedStates()) { items, checked ->
            buildState(items, checked.associate { it.itemId to it.checked })
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChecklistUiState())

    init {
        viewModelScope.launch {
            itemsFlow.value = withContext(Dispatchers.IO) { repository.getItems() }
        }
    }

    fun setChecked(itemId: String, checked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setItemChecked(itemId, checked)
        }
    }

    fun resetAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.resetAll()
        }
    }

    private fun buildState(
        items: List<ChecklistItemData>,
        checkedMap: Map<String, Boolean>
    ): ChecklistUiState {
        if (items.isEmpty()) return ChecklistUiState()

        val groupOrder = items.map { it.groupId }.distinct()
        val grouped = items.groupBy { it.groupId }

        val groups = groupOrder.map { groupId ->
            val groupItems = grouped.getValue(groupId)
            ChecklistGroup(
                groupId = groupId,
                groupName = groupItems.first().groupName,
                items = groupItems.map { item ->
                    ChecklistRow(
                        id = item.id,
                        name = item.name,
                        checked = checkedMap[item.id] == true
                    )
                }
            )
        }

        return ChecklistUiState(
            groups = groups,
            totalChecked = groups.sumOf { it.checkedCount },
            totalItems = items.size
        )
    }
}
