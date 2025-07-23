package com.example.jpitts_finalgirl_test_3.repository

import androidx.lifecycle.LiveData
import com.example.jpitts_finalgirl_test_3.db.InventoryDao
import com.example.jpitts_finalgirl_test_3.model.InventoryItem

class InventoryRepository(
    private val inventoryDao: InventoryDao
) {

    fun getAllItems(): LiveData<List<InventoryItem>> = inventoryDao.getAllItems()

    suspend fun insertItem(item: InventoryItem) {
        inventoryDao.insert(item)
    }

    suspend fun updateItem(item: InventoryItem) {
        inventoryDao.update(item)
    }

    suspend fun deleteItem(item: InventoryItem) {
        inventoryDao.delete(item)
    }

    fun getItemByName(name: String): LiveData<InventoryItem?> = inventoryDao.getItemByName(name)
}
