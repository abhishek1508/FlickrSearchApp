package com.example.flickrsearch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickrsearch.repository.FlickrSearchRepository
import com.example.flickrsearch.state.FlickrImageState
import com.example.flickrsearch.state.InputFieldState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the state and logic related to the Flickr image search feature.
 *
 * This [ViewModel] interacts with the [FlickrSearchRepository] to fetch search results based on the user's query
 * and manage various states of the search process.
 *
 * @param repository The repository used to fetch data from the Flickr API.
 */
@OptIn(FlowPreview::class)
class FlickrSearchViewModel(private val repository: FlickrSearchRepository): ViewModel() {

    private var _inputFieldState: MutableStateFlow<InputFieldState> = MutableStateFlow(InputFieldState.Idle)
    val inputFieldState: StateFlow<InputFieldState> = _inputFieldState.asStateFlow()

    private var _flickrImageState: MutableStateFlow<FlickrImageState> = MutableStateFlow(FlickrImageState.Idle)
    val flickrImageState: StateFlow<FlickrImageState> = _flickrImageState.asStateFlow()

    private var _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        viewModelScope.launch {
            searchQuery.debounce(timeoutMillis = 500).collectLatest { query ->
                if (query.isEmpty()) {
                    _flickrImageState.update { FlickrImageState.Idle }
                    return@collectLatest
                }
                repository.fetchImages(query = query).fold(
                    onSuccess = { result ->
                        if (result.items.isEmpty()) {
                            _flickrImageState.update { FlickrImageState.EmptyResults }
                        } else {
                            _flickrImageState.update { FlickrImageState.Content(result) }
                        }
                    }, onFailure = { error ->
                        _flickrImageState.update { FlickrImageState.Error(error.message) }
                    }
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _flickrImageState.update { FlickrImageState.Loading }
        _searchQuery.update { query }
        onSearchInputFocused()
    }

    fun onSearchInputFieldClicked() {
        onSearchInputFocused()
    }

    fun onSearchInputFieldCleared() {
        _searchQuery.update { "" }
        _inputFieldState.update { InputFieldState.Idle }
    }

    private fun onSearchInputFocused() {
        if (searchQuery.value.isNotEmpty()) {
            _inputFieldState.update { InputFieldState.HasInput }
        } else {
            _inputFieldState.update { InputFieldState.FocusedNoInput }
        }
    }
}