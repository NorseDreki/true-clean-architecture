package com.example.domain.searchItems

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.searchItems.saveSearch.SaveSearch
import com.example.domain.searchItems.searchFilters.SearchFilters
import com.example.domain.searchItems.searchResults.SearchResults
import com.example.domain.searchItems.searchSuggestions.SearchSuggestions
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class LoopbackCommands : ObservableTransformer<UiResult, UiCommand> {

    override fun apply(upstream: Observable<UiResult>) =
            upstream.flatMap {
                when (it) {
                    is SearchFilters.Result.FiltersApplied -> {
                        Observable.just(SearchResults.Command.PerformSearch(it.items))
                    }
                    is SearchSuggestions.Result.SearchSuggestions -> {
                        Observable.just(SearchResults.Command.ToggleVisibility(false))
                    }
                    is SearchResults.Result.InProgress -> {
                        Observable.just(SearchFilters.Command.ToggleDisplayFilters(""))
                        //SaveSearch.Command.ToggleShown("true")
                    }
                    is SearchResults.Result.SearchResults -> {
                        Observable.fromArray(
                                SearchFilters.Command.DATA(""),
                                SearchFilters.Command.ToggleDisplayFilters("true"),
                                SaveSearch.Command.ToggleShown("true")
                        )
                    }
                    else -> Observable.empty()
                }
            }
                    .cast(UiCommand::class.java)!!
}
