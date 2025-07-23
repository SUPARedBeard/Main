package com.example.jpitts_finalgirl_test_3.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "endings")
data class Ending(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val isGoodEnding: Boolean = false
)
