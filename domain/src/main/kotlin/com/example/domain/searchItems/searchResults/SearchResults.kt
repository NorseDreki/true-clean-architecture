package com.example.domain.searchItems.searchResults

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState

class SearchResults {

    sealed class Command : UiCommand {
        data class PerformSearch(val query: String) : Command()
        //data class Refresh(val query: String) : Command()
        //data class NextPage(val query: String) : Command()

        //clicks on item elements

        data class ToggleVisibility(val visible: Boolean) : Command()
    }

    sealed class Result : UiResult {
        data class SearchResults(val items: String) : Result()
        data class SearchFailed(val exception: Throwable) : Result()

        data class VisibilityChanged(val visible: Boolean) : Result()

        //empty

        object InProgress : Result()
    }

    data class ViewState(
            val items: List<String>,
            val displayedState: String,
            val exception: Exception?
    ) : UiState
}
