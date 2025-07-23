package com.example.jpitts_finalgirl_test_3.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory")
data class InventoryItem(
    @PrimaryKey val name: String,
    val description: String
)
