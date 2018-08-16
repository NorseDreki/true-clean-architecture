package com.example.domain.submitProposal2.layouts

import com.example.domain.UiResult
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.coverLetter.CoverLetter
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal
import com.example.domain.submitProposal2.proposeTip.ProposeTip
import com.example.domain.submitProposal2.suggestedTip.SuggestedTip
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class LinearLayoutReducer(
        val proposeTipReducer: ObservableTransformer<ProposeTip.Result, ProposeTip.ViewState>,
        val suggestedTipReducer: ObservableTransformer<SuggestedTip.Result, SuggestedTip.ViewState>,
        val asReducer: ObservableTransformer<CoverLetter.Result, CoverLetter.ViewState>,
        val asReducer1: ObservableTransformer<ClarifyingQuestions.Result, ClarifyingQuestions.ViewState>,
        val asReducer2: ObservableTransformer<DoSubmitProposal.Result, DoSubmitProposal.ViewState>
) : ObservableTransformer<UiResult, LinearLayoutViewState> {

    override fun apply(upstream: Observable<UiResult>) =
        upstream
                .publish { shared ->
                    Observable.combineLatest(
                            arrayOf(
                                    shared.ofType(ProposeTip.Result::class.java).compose(proposeTipReducer).doOnNext { println("render PT: $it") },
                                    shared.ofType(SuggestedTip.Result::class.java).compose(suggestedTipReducer).doOnNext { println("render PT: $it") },
                                    shared.ofType(CoverLetter.Result::class.java).compose(asReducer).doOnNext { println("render CL: $it") },
                                    shared.ofType(ClarifyingQuestions.Result::class.java).compose(asReducer1).doOnNext { println("render CQ: $it") },
                                    shared.ofType(DoSubmitProposal.Result::class.java).compose(asReducer2).doOnNext { println("render DSP: $it") }
                            )
                    ) {
                        val pt = it[0] as com.example.domain.submitProposal2.proposeTip.ProposeTip.ViewState
                        val st = it[1] as SuggestedTip.ViewState
                        val cl = it[2] as com.example.domain.submitProposal2.coverLetter.CoverLetter.ViewState
                        val cq = it[3] as ClarifyingQuestions.ViewState
                        val dsp = it[4] as DoSubmitProposal.ViewState
                        LinearLayoutViewState(pt, st, cl, cq, dsp)
                    }
                }!!
}
