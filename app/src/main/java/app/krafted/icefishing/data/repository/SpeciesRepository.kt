package app.krafted.icefishing.data.repository

import android.content.Context
import app.krafted.icefishing.data.model.Species
import com.google.gson.Gson

class SpeciesRepository(private val context: Context, private val gson: Gson) {
    private var cachedSpecies: List<Species>? = null

    suspend fun getSpecies(): List<Species> {
        if (cachedSpecies == null) {
            cachedSpecies = loadJsonAsset<List<Species>>(context, gson, "species.json")
        }
        return cachedSpecies!!
    }

    suspend fun getSpeciesById(id: Int): Species? {
        return getSpecies().find { it.id == id }
    }
}
