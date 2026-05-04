package app.krafted.icefishing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.icefishing.data.model.Species
import app.krafted.icefishing.data.repository.SpeciesRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface FishGuideUiState {
    data object Loading : FishGuideUiState
    data class Ready(val species: List<Species>) : FishGuideUiState
}

class FishGuideViewModel(application: Application) : AndroidViewModel(application) {

    private val gson = Gson()
    private val repository = SpeciesRepository(application.applicationContext, gson)

    private val _state = MutableStateFlow<FishGuideUiState>(FishGuideUiState.Loading)
    val state: StateFlow<FishGuideUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = FishGuideUiState.Ready(repository.getSpecies())
        }
    }
}
