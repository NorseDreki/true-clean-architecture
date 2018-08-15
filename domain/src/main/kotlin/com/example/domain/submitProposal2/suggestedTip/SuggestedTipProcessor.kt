package com.example.domain.submitProposal2.suggestedTip

import com.example.domain.Processor
import com.example.domain.submitProposal2.suggestedTip.SuggestedTip.Command
import com.example.domain.submitProposal2.suggestedTip.SuggestedTip.Command.*
import com.example.domain.submitProposal2.suggestedTip.SuggestedTip.Result
import com.example.domain.submitProposal2.suggestedTip.SuggestedTip.Result.*
import io.reactivex.Observable

class SuggestedTipProcessor(
        val userSuggestion: UserSuggestion
) : Processor<Command, Result> {

    override fun apply(upstream: Observable<Command>) =
            upstream
                    .flatMap {
                        when (it) {
                            is START -> {
                                val result = if (userSuggestion.hasSuggestionAvailable()) {
                                    SuggestionLoaded(userSuggestion.getSuggestionForUser())
                                } else {
                                    SuggestionNotAvailable
                                }

                                Observable.just(result)

                            }
                            AcceptSugestion -> {
                                Observable.just(SuggestionAccepted)
                            }
                            LearnMore -> {
                                //navigation side effect

                                Observable.empty()
                            }
                        }
                    }!!
}
