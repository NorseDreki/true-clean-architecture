package com.example.domain.searchItems

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.framework.WithLoopback
import com.example.domain.framework.WithProcessors
import com.example.domain.searchItems.searchQuery.SearchQuery
import com.example.domain.searchItems.searchQuery.SearchQueryProcessor
import com.example.domain.searchItems.searchResults.SearchResults
import com.example.domain.searchItems.searchResults.SearchResultsProcessor
import com.example.domain.searchItems.searchSuggestions.SearchSuggestions
import com.example.domain.searchItems.searchSuggestions.SearchSuggestionsProcessor
import com.example.domain.submitProposal2.SubmitProposalLoopback
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class SearchItemsProcessor(
        val searchItemsProcessor: SearchItemsProcessor,
        val searchQuery: SearchQueryProcessor,
        val searchResultsProcessor: SearchResultsProcessor,
        val searchSuggestionsProcessor: SearchSuggestionsProcessor

) : ObservableTransformer<SearchItems.Command, UiResult> {


    val inner =
            ObservableTransformer<UiCommand, UiResult> {
                it
                        .compose(WithProcessors(
                                SearchItems.Command::class.java as Class<Any> to searchItemsProcessor as ObservableTransformer<Any, UiResult>,
                                SearchQuery.Command::class.java as Class<Any> to searchQuery as ObservableTransformer<Any, UiResult>,
                                SearchResults.Command::class.java as Class<Any> to searchResultsProcessor as ObservableTransformer<Any, UiResult>,
                                SearchSuggestions.Command::class.java as Class<Any> to searchSuggestionsProcessor as ObservableTransformer<Any, UiResult>
                        ))
            }


    override fun apply(upstream: Observable<SearchItems.Command>) =
        upstream
                .compose(WithLoopback(inner, SubmitProposalLoopback()))!!
}
