package com.example.domain.submitProposal2

val spa: SubmitProposalAnalytics? = null
val psa: ProposalSummaryAnalytics? = null

val analyticsProc =
    Observable.Transformer<UiResult, Unit> {
        it.map {
            when (it) {
                is ProposalSummaryResult.NavigationRequested -> {
                    psa!!.logReviewFromSummary(jobId, submitProposalPresenter.getSelectedPath())
                }
                is AnchorablePanelResult.Discard -> {
                    spa!!.logProposalDraftRemove()
                }
                is AnchorablePanelResult.PanelStateChanged -> {
                    spa!!.logPanelStateChanged()
                    spa!!.logScreen()
                }
                is DoSubmitProposalResult.Success -> {
                    psa!!.logSubmitProposal(jobId, submitProposalResponse.getApplicationId())
                }
                is DoSubmitProposalResult.Error -> {
                    psa!!.logMismatchFees(jobId)
                }
            }
        }
    }
