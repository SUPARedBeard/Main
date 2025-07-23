package com.example.jpitts_finalgirl_test_3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jpitts_finalgirl_test_3.model.InventoryItem
import com.example.jpitts_finalgirl_test_3.repository.InventoryRepository
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val repository: InventoryRepository
) : ViewModel() {

    val allItems: LiveData<List<InventoryItem>> = repository.getAllItems()

    fun insertItem(item: InventoryItem) {
        viewModelScope.launch {
            repository.insertItem(item)
        }
    }

    fun updateItem(item: InventoryItem) {
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }

    fun deleteItem(item: InventoryItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    fun getItemByName(name: String): LiveData<InventoryItem?> {
        return repository.getItemByName(name)
    }
}
