package com.example.domain.searchItems.searchResults

import com.example.domain.searchItems.searchResults.SearchResults.Command
import com.example.domain.searchItems.searchResults.SearchResults.Result
import com.example.domain.submitProposal2.doSubmitProposal.Api
import com.example.domain.submitProposal2.doSubmitProposal.SomeApi
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class SearchResultsProcessor : ObservableTransformer<Command, Result> {

    override fun apply(upstream: Observable<Command>) =
            upstream
                    .flatMap {
                        when (it) {
                            is Command.PerformSearch -> {
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
                .map(Result::SearchResults)
                .doOnNext {
                    /*navigator.display(
                            proposalConfirmation,
                            ProposalConfirmation.Command.DATA("some"))*/
                }
                .cast(Result::class.java)
                .onErrorReturn(Result::SearchFailed)
                .startWith(Result.InProgress)
    }
}
