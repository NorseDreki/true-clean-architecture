package com.example.domain.submitProposal2.suggestedTip

import com.example.domain.submitProposal2.suggestedTip.SuggestedTip.Command
import com.example.domain.submitProposal2.suggestedTip.SuggestedTip.Result
import io.reactivex.Observable
import io.reactivex.ObservableTransformer


class Processor(val userSuggestion: UserSuggestion) : ObservableTransformer<Command, Result> {

    override fun apply(upstream: Observable<Command>) =
            upstream
                    .flatMap {
                        when (it) {
                            is Command.DATA -> {
                                val result = if (userSuggestion.hasSuggestionAvailable()) {
                                    Result.SuggestionLoaded(userSuggestion.getSuggestionForUser())
                                } else {
                                    Result.SuggestionNotAvailable
                                }

                                Observable.just(result)

                            }
                            Command.AcceptSugestion -> {
                                Observable.just(Result.SuggestionAccepted)
                            }
                            is Command.LearnMore -> {
                                //navigation side effect

                                Observable.empty()
                            }
                        }
                    }!!
}
