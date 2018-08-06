package com.example.domain.searchItems.saveSearch

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState

class SaveSearch {

    sealed class Command : UiCommand {
        data class DATA(val query: String, val categories: String) : Command()

        data class ToggleShown(val query: String) : Command()

        object CancelPrompt : Command()

        data class UpdateSearchName(val query: String) : Command()
        //data class UpdateCaegories(val query: String) : Command()

        data class DoSaveSearch(val query: String) : Command()
        data class PromptSaveSearch(val query: String) : Command()
    }

    sealed class Result : UiResult {
        data class SearchNameSanitized(val items: String) : Result()
        
        data class SearchSaved(val items: String) : Result()
        data class SearchSaveFailed(val items: String) : Result()
        data class SearchSaveCancelled(val items: String) : Result()
    }

    data class ViewState(
            val query: String
    ) : UiState
}
