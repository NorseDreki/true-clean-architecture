package com.example.domain.submitProposal2.coverLetter

import com.example.domain.submitProposal2.coverLetter.CoverLetter.Result
import com.example.domain.submitProposal2.coverLetter.CoverLetter.ViewState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class Reducer : ObservableTransformer<Result, ViewState> {

    override fun apply(upstream: Observable<Result>) =

            upstream.scan(ViewState()) { state, result ->
                when (result) {
                    is Result.Valid -> state.copy(result.coverLetter, false)
                    is Result.Empty -> state.copy("", false)
                    is Result.LengthExceeded -> state.copy(result.coverLetter, true)
                    else -> throw IllegalStateException("sdaf $state")
                }
            }
}
