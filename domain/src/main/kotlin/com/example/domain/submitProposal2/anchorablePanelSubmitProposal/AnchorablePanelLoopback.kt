package com.example.domain.submitProposal2.anchorablePanelSubmitProposal

import com.example.domain.Loopback
import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.submitProposal2.SubmitProposal
import com.example.domain.submitProposal2.anchorablePanel.AnchorablePanelCommands
import com.example.domain.submitProposal2.anchorablePanelToolbar.AnchorablePanelToolbarCommand
import com.example.domain.submitProposal2.anchorablePanelToolbar.AnchorablePanelToolbarResult
import io.reactivex.Observable

class AnchorablePanelLoopback : Loopback<UiResult, UiCommand> {

    override fun apply(upstream: Observable<UiResult>) =
            upstream.flatMap {
                when (it) {
                    is SubmitProposal.Result.NavigatedTo -> {
                        Observable.just(AnchorablePanelToolbarCommand.ChangeTitle(""))
                    }
                    SubmitProposal.Result.ProposalSent -> {
                        Observable.just(AnchorablePanelCommands.Hide)
                    }
                    SubmitProposal.Result.JobIsPrivate -> {
                        Observable.just(AnchorablePanelCommands.Hide)
                    }
                    SubmitProposal.Result.JobNoLongerAvailable -> {
                        Observable.just(AnchorablePanelCommands.Hide)
                    }
                    AnchorablePanelToolbarResult.DiscardRequested -> {
                        //or show dialog first?
                        Observable.just(SubmitProposal.Command.RemoveProposal)
                    }
                    AnchorablePanelToolbarResult.ExpandRequested -> {
                        //or show dialog first?
                        Observable.just(AnchorablePanelCommands.Expand)
                    }
                    AnchorablePanelToolbarResult.CollapseRequested -> {
                        //or show dialog first?
                        Observable.just(AnchorablePanelCommands.Collapse)
                    }
                    else -> {
                        TODO()
                    }
                }
            }!!
}
