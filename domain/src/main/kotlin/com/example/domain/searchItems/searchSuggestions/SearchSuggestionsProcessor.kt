package com.example.domain.searchItems.searchSuggestions

import com.example.domain.searchItems.searchSuggestions.SearchSuggestions.Command
import com.example.domain.searchItems.searchSuggestions.SearchSuggestions.Result
import com.example.domain.submitProposal2.doSubmitProposal.Api
import com.example.domain.submitProposal2.doSubmitProposal.SomeApi
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class SearchSuggestionsProcessor : ObservableTransformer<Command, Result> {

    override fun apply(upstream: Observable<Command>) =
            upstream
                    .flatMap {
                        when (it) {
                            is Command.GetSuggestions -> {
                                doSubmit()
                            }
                            is Command.ToggleVisibility -> {
                                Observable.just(Result.VisibilityChanged(it.visible))
                            }
                        }
                    }

    private fun doSubmit(): Observable<Result> {
        val api: Api? = SomeApi()

        return api!!.submitProposal("123", "dsf")
                //.delay(3, TimeUnit.SECONDS)
                .map(Result::SearchSuggestions)
                .doOnNext {
                    /*navigator.display(
                            proposalConfirmation,
                            ProposalConfirmation.Command.DATA("some"))*/
                }
                .cast(Result::class.java)
                .onErrorReturn(Result::SuggestionsFailed)
                .startWith(Result.InProgress)
    }
}
