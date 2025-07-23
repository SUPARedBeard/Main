package com.example.jpitts_finalgirl_test_3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jpitts_finalgirl_test_3.model.InventoryItem
import com.example.jpitts_finalgirl_test_3.model.StoryChoice
import com.example.jpitts_finalgirl_test_3.model.StoryPage
import com.example.jpitts_finalgirl_test_3.repository.StoryRepository

class StoryViewModel(
    private val repository: StoryRepository
) : ViewModel() {

    val currentPage = MutableLiveData<StoryPage>()
    val inventory = MutableLiveData<List<InventoryItem>>()
    val shouldNavigateToEnding = MutableLiveData<Boolean>()

    init {
        currentPage.value = repository.getPage(1)
    }

    fun chooseOption(nextPageId: Int) {
        val previousChoices = currentPage.value?.choices ?: emptyList()
        val selectedChoice = previousChoices.find { it.nextPageId == nextPageId }

        val currentItems = inventory.value?.toMutableList() ?: mutableListOf()



        selectedChoice?.itemToAdd?.let { currentItems.add(it) }
        selectedChoice?.itemToRemove?.let { currentItems.remove(it) }

        val nextPage = repository.getPage(nextPageId)
        if (nextPage == null) {
            shouldNavigateToEnding.value = true
            return
        }

        inventory.value = currentItems
        currentPage.value = repository.getPage(nextPageId)


    }

    fun addItem(item: InventoryItem) {
        val currentList = inventory.value?.toMutableList() ?: mutableListOf()
        currentList.add(item)
        inventory.value = currentList
    }

    fun resetStory() {
        currentPage.value = repository.getPage(1)  //start from beginning
        inventory.value = emptyList()
        shouldNavigateToEnding.value = false
    }
}

