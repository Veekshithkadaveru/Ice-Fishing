package app.krafted.icefishing.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <reified T> loadJsonAsset(context: Context, gson: Gson, fileName: String): T =
    withContext(Dispatchers.IO) {
        context.assets.open(fileName).bufferedReader().use { reader ->
            val type = object : TypeToken<T>() {}.type
            gson.fromJson(reader, type)
        }
    }
