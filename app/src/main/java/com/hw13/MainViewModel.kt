package com.hw13

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Waiting)
    val state = _state.asStateFlow()

    private val _credential = MutableStateFlow(Credentials())
    val credentials = _credential.asStateFlow()

    private var searchJob: Job? = null
    private var updateJob: Job? = null

    init {
        viewModelScope.launch {
            _credential.debounce(300).collect {
                updateSearchText()
            }
        }
    }

    fun updateSearchText() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            delay(300) // Задержка после вызова метода
            val searchText = credentials.value.textSearch
            if (searchText.length > 2) {
                _state.value = State.Ready
                searchData() // Вызов функции searchData() после задержки
            } else {
                _state.value = State.Waiting
            }
        }
    }

    fun searchData() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _state.value = State.Search
            val searchText = credentials.value.textSearch
            val result = repository.getData(searchText)
            _state.value = State.Success
            _state.value = State.SearchResult(result)
        }
    }
}
