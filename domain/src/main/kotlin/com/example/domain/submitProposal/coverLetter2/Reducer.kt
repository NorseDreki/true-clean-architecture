package com.example.domain.submitProposal.coverLetter2

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class Reducer : ObservableTransformer<CoverLetter2.Result, CoverLetter2.ViewState> {
    override fun apply(upstream: Observable<CoverLetter2.Result>): ObservableSource<CoverLetter2.ViewState> {
        return upstream.scan(CoverLetter2.ViewState())
        { state, result ->
            when (result) {
                is CoverLetter2.Result.Valid -> state.copy(result.coverLetter, false)
                is CoverLetter2.Result.Empty -> state.copy("", false)
                is CoverLetter2.Result.LengthExceeded -> state.copy(result.coverLetter, true)
                else -> throw IllegalStateException("sdaf $state")
            }
        }
    }
}