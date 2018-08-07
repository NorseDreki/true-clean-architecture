package com.example.domain.searchItems.searchSuggestions

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState

class SearchSuggestions {

    sealed class Command : UiCommand {
        data class GetSuggestions(val query: String) : Command()

        data class ToggleVisibility(val visible: Boolean) : Command()
    }

    sealed class Result : UiResult {
        data class SearchSuggestions(val items: String) : Result()
        data class SuggestionsFailed(val exception: Throwable) : Result()

        object InProgress : Result()

        object NoSuggestions: Result()

        data class VisibilityChanged(val visible: Boolean) : Result()

    }

    data class ViewState(
            val items: List<String>
    ) : UiState
}
