package com.example.domain.submitProposal2.doSubmitProposal

import com.example.domain.submitProposal2.common.DialogState
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal.Result
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal.ViewState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class Reducer : ObservableTransformer<Result, ViewState> {

    override fun apply(upstream: Observable<Result>) =
            upstream.scan(ViewState()) { state, result ->
                when (result) {
                    is Result.SubmitEnabled -> {
                        state.copy(isSubmitEnabled = result.enabled)
                    }
                    Result.InProgress -> state.copy(
                            status = DialogState.Progress(null, "In Progress")
                    )
                    is Result.Success -> {
                        state.copy(
                                //isSubmitEnabled = false,
                                status = DialogState.Dismissed
                        )
                    }
                    is Result.Error -> state.copy(
                            status = DialogState.Alert(
                                    "title",
                                    "message",
                                    "Retry",
                                    "Cancel"
                            )
                    )
                }
            }!!
}
