package app.krafted.icefishing.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.krafted.icefishing.data.db.entities.*

@Database(
    entities = [ArticleBookmark::class, QuizScore::class, CatchEntry::class, ChecklistItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun quizScoreDao(): QuizScoreDao
    abstract fun catchEntryDao(): CatchEntryDao
    abstract fun checklistDao(): ChecklistDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ice_fishing.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
            }
    }
}
