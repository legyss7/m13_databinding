package com.hw13

sealed class State(
    val isWaiting: Boolean = false,
    val isSearch: Boolean = false,
    open val textSearchResult: String? = "Здесь будет отображаться результат запроса"
) {
    data object Waiting : State(isWaiting = true)
    data object Ready : State()
    data object Search : State(isSearch = true)
    data object Success : State()
    data class SearchResult(
        override val textSearchResult: String?
    ) : State(
        textSearchResult = textSearchResult
    )
}