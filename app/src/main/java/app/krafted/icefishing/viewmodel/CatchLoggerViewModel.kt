package app.krafted.icefishing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.icefishing.data.db.AppDatabase
import app.krafted.icefishing.data.db.entities.CatchEntry
import app.krafted.icefishing.data.repository.SpeciesRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

data class CatchSummary(
    val totalFish: Int,
    val sessions: Int,
    val topSpecies: String?,
    val topDepth: Int?
)

data class CatchLoggerFilters(
    val species: String? = null,
    val dateRange: DateRange = DateRange.ALL
)

enum class DateRange {
    ALL, LAST_7_DAYS, LAST_30_DAYS
}

data class CatchLoggerUiState(
    val entries: List<CatchEntry> = emptyList(),
    val summary: CatchSummary = CatchSummary(0, 0, null, null),
    val filters: CatchLoggerFilters = CatchLoggerFilters(),
    val speciesOptions: List<String> = emptyList()
)

class CatchLoggerViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application.applicationContext).catchEntryDao()
    private val speciesRepository = SpeciesRepository(application.applicationContext, Gson())

    private val _filters = MutableStateFlow(CatchLoggerFilters())
    val filters: StateFlow<CatchLoggerFilters> = _filters.asStateFlow()

    private val _speciesOptions = MutableStateFlow<List<String>>(emptyList())

    val state: StateFlow<CatchLoggerUiState> =
        combine(dao.getAll(), _filters, _speciesOptions) { all, filters, options ->
            val filtered = applyFilters(all, filters)
            CatchLoggerUiState(
                entries = filtered,
                summary = computeSummary(filtered),
                filters = filters,
                speciesOptions = options
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CatchLoggerUiState())

    init {
        viewModelScope.launch {
            val species = withContext(Dispatchers.IO) { speciesRepository.getSpecies() }
            _speciesOptions.value = species.map { it.name }
        }
    }

    fun setSpeciesFilter(species: String?) {
        _filters.value = _filters.value.copy(species = species)
    }

    fun setDateRange(range: DateRange) {
        _filters.value = _filters.value.copy(dateRange = range)
    }

    fun addEntry(entry: CatchEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(entry)
        }
    }

    fun deleteEntry(entry: CatchEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(entry)
        }
    }

    private fun applyFilters(
        all: List<CatchEntry>,
        filters: CatchLoggerFilters
    ): List<CatchEntry> {
        val now = System.currentTimeMillis()
        val cutoff = when (filters.dateRange) {
            DateRange.ALL -> Long.MIN_VALUE
            DateRange.LAST_7_DAYS -> now - 7L * 24 * 60 * 60 * 1000
            DateRange.LAST_30_DAYS -> now - 30L * 24 * 60 * 60 * 1000
        }
        return all.filter { entry ->
            (filters.species == null || entry.fishSpecies == filters.species) &&
                    entry.date >= cutoff
        }
    }

    private fun computeSummary(entries: List<CatchEntry>): CatchSummary {
        if (entries.isEmpty()) return CatchSummary(0, 0, null, null)
        val totalFish = entries.sumOf { it.count }
        val sessions = entries.map { it.sessionId }.distinct().size
        val topSpecies = entries
            .groupBy { it.fishSpecies }
            .mapValues { (_, list) -> list.sumOf { it.count } }
            .maxByOrNull { it.value }
            ?.key
        val topDepth = entries
            .groupBy { it.depth }
            .mapValues { (_, list) -> list.sumOf { it.count } }
            .maxByOrNull { it.value }
            ?.key
        return CatchSummary(totalFish, sessions, topSpecies, topDepth)
    }
}

fun newSessionId(): String = "session_${System.currentTimeMillis()}"

fun startOfToday(): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}
