package com.example.jpitts_finalgirl_test_3.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.jpitts_finalgirl_test_3.model.InventoryItem

@Dao
interface InventoryDao {

    @Query("SELECT * FROM inventory")
    fun getAllItems(): LiveData<List<InventoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: InventoryItem)

    @Update
    suspend fun update(item: InventoryItem)

    @Delete
    suspend fun delete(item: InventoryItem)

    @Query("SELECT * FROM inventory WHERE name = :name LIMIT 1")
    fun getItemByName(name: String): LiveData<InventoryItem?>
}
