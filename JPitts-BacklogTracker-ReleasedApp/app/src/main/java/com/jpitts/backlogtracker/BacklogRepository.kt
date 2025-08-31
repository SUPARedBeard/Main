package com.jpitts.backlogtracker

import androidx.lifecycle.LiveData
import com.jpitts.backlogtracker.data.BacklogDao
import com.jpitts.backlogtracker.data.BacklogItem

class BacklogRepository(private val dao: BacklogDao) {

    fun getItemsByType(type: String): LiveData<List<BacklogItem>> =
        dao.getItemsByType(type)

    fun getItemById(id: Long): LiveData<BacklogItem> =
        dao.getItemById(id)

    suspend fun insert(item: BacklogItem) = dao.insert(item)
    suspend fun update(item: BacklogItem) = dao.update(item)
    suspend fun delete(item: BacklogItem) = dao.delete(item)
}