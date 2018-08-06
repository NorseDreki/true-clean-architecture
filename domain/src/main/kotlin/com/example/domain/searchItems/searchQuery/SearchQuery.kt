package com.example.domain.searchItems.searchQuery

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState

class SearchQuery {

    sealed class Command : UiCommand {
        data class UpdateSearchQuery(val query: String) : Command()
    }

    sealed class Result : UiResult {
        data class SanitizedQuery(val items: String) : Result()
    }

    data class ViewState(
            val query: String
    ) : UiState
}
