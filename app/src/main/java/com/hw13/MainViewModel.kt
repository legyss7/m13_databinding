package com.hw13

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Waiting)
    val state = _state.asStateFlow()

    private val _credential = MutableStateFlow(Credentials())
    val credentials = _credential.asStateFlow()

    fun updateSearchText() {
        viewModelScope.launch {
            val searchText = credentials.value.textSearch
            if (searchText.length > 2) {
                _state.value = State.Ready
            } else {
                _state.value = State.Waiting
            }

        }
    }

    fun searchData() {
        viewModelScope.launch {
            _state.value = State.Search
            val searchText = credentials.value.textSearch
            val result = repository.getData(searchText)
            _state.value = State.Success
            _state.value = State.SearchResult(result)
        }
    }
}
