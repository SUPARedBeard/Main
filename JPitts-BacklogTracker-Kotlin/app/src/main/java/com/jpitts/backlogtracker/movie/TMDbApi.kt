package com.jpitts.backlogtracker.movie

import retrofit2.http.GET
import retrofit2.http.Query

interface TMDbApi {
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): MovieResponse
}
