package app.krafted.icefishing.data.repository

import android.content.Context
import app.krafted.icefishing.data.db.BookmarkDao
import app.krafted.icefishing.data.db.entities.ArticleBookmark
import app.krafted.icefishing.data.model.Article
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ArticleRepository(
    private val context: Context,
    private val gson: Gson,
    private val bookmarkDao: BookmarkDao
) {
    private var cachedArticles: List<Article>? = null

    suspend fun getArticles(): List<Article> {
        if (cachedArticles == null) {
            cachedArticles = loadJsonAsset<List<Article>>(context, gson, "articles.json")
        }
        return cachedArticles ?: emptyList()
    }

    suspend fun getArticleById(id: Int): Article? {
        return getArticles().find { it.id == id }
    }

    fun isBookmarked(articleId: Int): Flow<Boolean> = bookmarkDao.isBookmarked(articleId)

    fun getAllBookmarks(): Flow<List<ArticleBookmark>> = bookmarkDao.getAll()

    suspend fun toggleBookmark(articleId: Int) {
        val isCurrentlyBookmarked = bookmarkDao.isBookmarked(articleId).first()
        if (isCurrentlyBookmarked) {
            bookmarkDao.delete(ArticleBookmark(articleId))
        } else {
            bookmarkDao.insert(ArticleBookmark(articleId))
        }
    }
}
