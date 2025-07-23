package com.example.jpitts_finalgirl_test_3.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jpitts_finalgirl_test_3.model.InventoryItem

@Database(entities = [InventoryItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
}
