package com.jpitts.backlogtracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "backlog_items")
data class BacklogItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val title: String,
    val platform: String,
    val status: String,
    val rating: Float,
    val notes: String?,
    val completed: Boolean,
    val streamingService: String? = null,
    val coverImageUrl: String? = null
)