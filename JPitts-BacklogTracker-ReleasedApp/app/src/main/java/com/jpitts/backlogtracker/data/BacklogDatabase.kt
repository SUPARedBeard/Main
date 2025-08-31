package com.jpitts.backlogtracker.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

import androidx.sqlite.db.SupportSQLiteDatabase



@Database(entities = [BacklogItem::class], version = 5)
abstract class BacklogDatabase : RoomDatabase() {
    abstract fun backlogDao(): BacklogDao

    companion object {
        @Volatile private var INSTANCE: BacklogDatabase? = null

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE backlog_items ADD COLUMN priority TEXT NOT NULL DEFAULT 'Medium'")
            }
        }

        fun getDatabase(context: Context): BacklogDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BacklogDatabase::class.java,
                    "backlog_database"
                )
                    .addMigrations(MIGRATION_4_5)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
