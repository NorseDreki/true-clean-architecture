package com.example.domain.submitProposal2

import io.reactivex.ObservableTransformer

val navigationProcessor =
        ObservableTransformer<SubmitProposal.Command.ToNextStep, SubmitProposal.Result> {
            it.scan(NavState()) { state, command ->
                when {
                    state.index < state.pages-1 -> state.copy(state.index + 1)
                    else -> state.copy(index = 0)
                }
            }.map { SubmitProposal.Result.NavigatedTo(it.index) }
        }

data class NavState(val index: Int = 0, val pages: Int = 4)