package com.jpitts.backlogtracker.movie

import com.jpitts.backlogtracker.movie.MovieRetrofitInstance.api

class MovieRepository {
    suspend fun fetchMoviePoster(title: String): String? {
        return try {
            val response = MovieRetrofitInstance.api.searchMovies(MovieConstants.API_KEY, title)
            response.results.firstOrNull()?.posterPath
        } catch (e: Exception) {
            null
        }
    }



    suspend fun searchMovieByTitle(title: String): Movie? {
        return try {
            val response = api.searchMovies(MovieConstants.API_KEY, title)
            response.results.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
}

object MovieConstants {
    const val API_KEY = "c860a8672aff6789ebaa2d1728edc0a7"
}