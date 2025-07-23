package com.jpitts.backlogtracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [BacklogItem::class], version=4)
abstract class BacklogDatabase : RoomDatabase() {
    abstract fun backlogDao(): BacklogDao

    companion object {
        @Volatile private var INSTANCE: BacklogDatabase? = null

        fun getDatabase(context: Context): BacklogDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BacklogDatabase::class.java,
                    "backlog_database"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE =it }
            }
        }
    }
}