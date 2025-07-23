package com.example.jpitts_finalgirl_test_3.db

import androidx.room.*
import com.example.jpitts_finalgirl_test_3.model.Ending

@Dao
interface EndingDao {

    @Query("SELECT * FROM endings")
    suspend fun getAllEndings(): List<Ending>

    @Query("SELECT * FROM endings WHERE id = :endingId LIMIT 1")
    suspend fun getEndingById(endingId: Int): Ending?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnding(ending: Ending)

    @Update
    suspend fun updateEnding(ending: Ending)

    @Delete
    suspend fun deleteEnding(ending: Ending)

}
