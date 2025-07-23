package com.jpitts.backlogtracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jpitts.backlogtracker.data.BacklogDatabase
import com.jpitts.backlogtracker.data.BacklogItem
import com.jpitts.backlogtracker.data.Game
import com.jpitts.backlogtracker.data.RetrofitInstance
import com.jpitts.backlogtracker.data.RetrofitInstance.API_KEY
import com.jpitts.backlogtracker.movie.Movie
import com.jpitts.backlogtracker.movie.MovieRepository
import kotlinx.coroutines.launch

class BacklogViewModel(application: Application) : AndroidViewModel(application) {

    private val db = BacklogDatabase.getDatabase(application)
    private val repository = BacklogRepository(db.backlogDao())

    private val movieRepository = MovieRepository()



    private val _searchResults = MutableLiveData<List<Game>>()
    val searchResults: LiveData<List<Game>> = _searchResults

    private val _moviePosterUrl = MutableLiveData<String?>()
    val moviePosterUrl: LiveData<String?> = _moviePosterUrl

    private val _movieDetails = MutableLiveData<Movie?>()
    val movieDetails: LiveData<Movie?> = _movieDetails


    fun searchGames(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.searchGames(API_KEY, query)
                _searchResults.value = response.results
            } catch (e: Exception) {
                Log.e("BacklogViewModel", "Error fetching games: ${e.message}")
                _searchResults.value = emptyList()
            }
        }
    }

    fun fetchMoviePoster(title: String) {
        viewModelScope.launch {
            val posterPath = movieRepository.fetchMoviePoster(title)
            _moviePosterUrl.value = posterPath
        }
    }

    fun searchMovieByTitle(title: String) {
        viewModelScope.launch {
            val movie = movieRepository.searchMovieByTitle(title)
            _movieDetails.value = movie
            // Optionally update poster url for convenience
            _moviePosterUrl.value = movie?.posterPath
        }
    }


    fun getItemsByType(type: String): LiveData<List<BacklogItem>> =
        repository.getItemsByType(type)

    fun getItemById(id: Long): LiveData<BacklogItem> =
        repository.getItemById(id)

    fun insert(item: BacklogItem) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: BacklogItem) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: BacklogItem) = viewModelScope.launch {
        repository.delete(item)
    }
}
