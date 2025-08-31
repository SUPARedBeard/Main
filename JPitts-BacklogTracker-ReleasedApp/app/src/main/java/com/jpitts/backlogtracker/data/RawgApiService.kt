package com.jpitts.backlogtracker.data

import retrofit2.http.GET
import retrofit2.http.Query

interface RawgApiService {
    @GET("games")
    suspend fun searchGames(
        @Query("key") apiKey: String,
        @Query("search") query: String
    ): GameResponse
}
