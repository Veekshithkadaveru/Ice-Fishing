package app.krafted.icefishing.data.repository

import android.content.Context
import app.krafted.icefishing.data.model.Tip
import com.google.gson.Gson

class TipRepository(private val context: Context, private val gson: Gson) {
    private var cachedTips: List<Tip>? = null

    suspend fun getTips(): List<Tip> {
        if (cachedTips == null) {
            cachedTips = loadJsonAsset<List<Tip>>(context, gson, "tips.json")
        }
        return cachedTips ?: emptyList()
    }

    suspend fun getTipsByCategory(categoryId: String): List<Tip> {
        return getTips().filter { it.categoryId == categoryId }
    }
    
    suspend fun getCategories(): List<String> {
        return getTips().map { it.categoryId }.distinct()
    }
}
