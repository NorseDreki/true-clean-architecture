package com.example.domain.searchItems.searchFilters

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState

class SearchFilters {

    sealed class Command : UiCommand {
        data class DATA(val query: String) : Command()

        data class ToggleDisplayFilters(val query: String) : Command()

        object Apply : Command()
        object Reset : Command()

        data class UpdateFilter(val query: String) : Command()
        data class PromptUpdateFilter(val query: String) : Command()
    }

    sealed class Result : UiResult {
        data class FiltersApplied(val items: String) : Result()

        data class FilterUpdated(val items: String) : Result()
    }

    data class ViewState(
            val query: String
    ) : UiState
}
