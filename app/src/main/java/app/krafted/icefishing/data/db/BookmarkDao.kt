package app.krafted.icefishing.data.db

import androidx.room.*
import app.krafted.icefishing.data.db.entities.ArticleBookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM article_bookmarks")
    fun getAll(): Flow<List<ArticleBookmark>>

    @Query("SELECT EXISTS(SELECT 1 FROM article_bookmarks WHERE articleId = :articleId)")
    fun isBookmarked(articleId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bookmark: ArticleBookmark)

    @Delete
    suspend fun delete(bookmark: ArticleBookmark)
}
