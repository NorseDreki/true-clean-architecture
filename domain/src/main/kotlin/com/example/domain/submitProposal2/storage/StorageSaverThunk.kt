package com.example.domain.submitProposal2.storage

import com.example.domain.Thunk
import com.example.domain.UiResult
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import io.reactivex.Observable

class StorageSaverThunk : Thunk<UiResult, UiResult> {

    override fun apply(upstream: Observable<UiResult>) =
            upstream.flatMap {
                when(it) {
                    is ClarifyingQuestions.Result.AnswerValid -> {
                        try {
//                            repo!!.updateCoverLetter("234", "324")
                            Observable.empty<UiResult>()
                        } catch (e: Exception) {
                            Observable.error<UiResult>(e)
                        }
                    }
                    else -> Observable.empty()
                }
            }!!
}
