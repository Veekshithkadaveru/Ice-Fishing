package app.krafted.icefishing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.krafted.icefishing.data.model.Species
import app.krafted.icefishing.data.repository.SpeciesRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface FishDetailUiState {
    data object Loading : FishDetailUiState
    data class Ready(val species: Species) : FishDetailUiState
    data object NotFound : FishDetailUiState
}

class FishDetailViewModel(
    application: Application,
    private val speciesId: Int,
) : AndroidViewModel(application) {

    private val gson = Gson()
    private val repository = SpeciesRepository(application.applicationContext, gson)

    private val _state = MutableStateFlow<FishDetailUiState>(FishDetailUiState.Loading)
    val state: StateFlow<FishDetailUiState> = _state.asStateFlow()

    init {
        when {
            speciesId <= 0 -> _state.value = FishDetailUiState.NotFound
            else -> viewModelScope.launch {
                val species = repository.getSpeciesById(speciesId)
                _state.value =
                    species?.let { FishDetailUiState.Ready(it) } ?: FishDetailUiState.NotFound
            }
        }
    }

    companion object {
        fun factory(
            application: Application,
            speciesId: Int,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FishDetailViewModel(application, speciesId) as T
            }
        }
    }
}
