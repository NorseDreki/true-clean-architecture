package com.example.domain.submitProposal2.coverLetter

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class Reducer : ObservableTransformer<CoverLetter.Result, CoverLetter.ViewState> {

    override fun apply(upstream: Observable<CoverLetter.Result>): ObservableSource<CoverLetter.ViewState> {
        return upstream.scan(CoverLetter.ViewState())
        { state, result ->
            when (result) {
                is CoverLetter.Result.Valid -> state.copy(result.coverLetter, false)
                is CoverLetter.Result.Empty -> state.copy("", false)
                is CoverLetter.Result.LengthExceeded -> state.copy(result.coverLetter, true)
                else -> throw IllegalStateException("sdaf $state")
            }
        }
    }
}
