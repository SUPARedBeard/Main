package com.example.jpitts_finalgirl_test_3.model

data class StoryPage(
    val id: Int,
    val text: String,
    val choices: List<StoryChoice>
)

data class StoryChoice(
    val text: String,
    val nextPageId: Int,
    val itemToAdd: InventoryItem? = null,
    val itemToRemove: InventoryItem? = null
)
