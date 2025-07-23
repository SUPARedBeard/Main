package com.jpitts.backlogtracker.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BacklogDao {

    @Query("SELECT * FROM backlog_items WHERE type = :type ORDER BY title")
    fun getItemsByType(type: String): LiveData<List<BacklogItem>>

    @Query("SELECT * FROM backlog_items WHERE id = :id")
    fun getItemById(id: Long): LiveData<BacklogItem>

    @Insert
    suspend fun insert(item: BacklogItem)

    @Update
    suspend fun update(item: BacklogItem)

    @Delete
    suspend fun delete(item: BacklogItem)
}