package com.example.domain.submitProposal2.coverLetter

import com.example.domain.Reducer
import com.example.domain.submitProposal2.coverLetter.CoverLetter.Result
import com.example.domain.submitProposal2.coverLetter.CoverLetter.ViewState
import io.reactivex.Observable

class CoverLetterReducer : Reducer<Result, ViewState> {

    override fun apply(upstream: Observable<Result>) =
            upstream.scan(ViewState()) { state, result ->
                when (result) {
                    Result.NotRequired -> state.copy(
                            isVisible = false
                    )
                    is Result.Valid -> state.copy(
                            coverLetter = result.coverLetter,
                            isLengthExceeded = false
                    )
                    is Result.LengthExceeded -> state.copy(
                            coverLetter = result.coverLetter,
                            isLengthExceeded = true
                    )
                    Result.Empty -> state.copy(
                            coverLetter = "",
                            isLengthExceeded = false
                    )
                }
            }!!
}
