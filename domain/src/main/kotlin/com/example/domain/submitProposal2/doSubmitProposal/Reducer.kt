package com.example.domain.submitProposal2.doSubmitProposal

import com.example.domain.submitProposal2.common.DialogState
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class Reducer : ObservableTransformer<DoSubmitProposal.Result, DoSubmitProposal.ViewState> {

    override fun apply(upstream: Observable<DoSubmitProposal.Result>): ObservableSource<DoSubmitProposal.ViewState> {
        return upstream.scan(DoSubmitProposal.ViewState(false, DialogState.Dismissed)) { state, result ->
            when (result) {
                is DoSubmitProposal.Result.SubmitStatus -> state.copy(isSubmitEnabled = result.enabled)
                DoSubmitProposal.Result.InProgress -> state.copy(
                        status = DialogState.Progress(null, "In Progress")
                )
                is DoSubmitProposal.Result.Success -> {

                    state.copy(
                            status = DialogState.Dismissed
                    )
                }
                is DoSubmitProposal.Result.Error -> state.copy(
                        status = DialogState.Alert(
                                "title",
                                "message",
                                "Retry",
                                "Cancel"
                        )
                )
            }
        }
    }

}
