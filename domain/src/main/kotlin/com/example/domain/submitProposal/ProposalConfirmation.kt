package com.example.domain.submitProposal

import com.example.domain.UiState
import io.reactivex.ObservableTransformer

val pcReducer =
        ObservableTransformer<SubmitProposal.Result.ProposalLoaded, ProposalConfirmationViewState> {
            it.map { ProposalConfirmationViewState(it.itemOpportunity.itemDetails.id) }
        }

class ProposalConfirmationViewState(
        title: String
) : UiState
