package app.krafted.icefishing.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_bookmarks")
data class ArticleBookmark(
    @PrimaryKey val articleId: Int
)
