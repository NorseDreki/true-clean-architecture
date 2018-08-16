package com.example.domain.submitProposal2.anchorablePanel

import com.example.domain.Processor
import io.reactivex.Observable

class AnchorablePanelProcessor : Processor<AnchorablePanelCommands, AnchorablePanelResult> {

    override fun apply(upstream: Observable<AnchorablePanelCommands>) =
            upstream.map {
                when (it) {
                    AnchorablePanelCommands.Hide -> AnchorablePanelResult.PanelStateChanged(PanelState.HIDDEN)
                    AnchorablePanelCommands.Expand -> AnchorablePanelResult.PanelStateChanged(PanelState.EXPANDED)
                    AnchorablePanelCommands.Collapse -> AnchorablePanelResult.PanelStateChanged(PanelState.COLLAPSED)
                    is AnchorablePanelCommands.ChangeTitle -> AnchorablePanelResult.TitleChanged(it.title)
                //on nav command -- also expand panel
                // also change title
                    else -> throw IllegalStateException("sdf")
                }
            }!!
}
