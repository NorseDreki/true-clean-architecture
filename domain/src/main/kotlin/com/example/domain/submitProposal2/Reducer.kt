package com.example.domain.submitProposal2

import com.example.domain.UiResult
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.coverLetter.CoverLetter
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal
import com.example.domain.submitProposal2.proposalSummary.ProposalSummaryViewState
import com.example.domain.submitProposal2.proposalSummary.psReducer
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class Reducer(
        val asReducer: ObservableTransformer<CoverLetter.Result, CoverLetter.ViewState>,
        val asReducer1: ObservableTransformer<ClarifyingQuestions.Result, ClarifyingQuestions.ViewState>,
        val asReducer2: ObservableTransformer<DoSubmitProposal.Result, DoSubmitProposal.ViewState>
) : ObservableTransformer<UiResult, SubmitProposal.ViewState> {

    override fun apply(upstream: Observable<UiResult>): ObservableSource<SubmitProposal.ViewState> {
        return upstream
                .doOnNext { println("SP reducer $it") }
                .publish { shared ->
                    Observable.combineLatest(
                            arrayOf(
                                    shared.ofType(CoverLetter.Result::class.java).compose(asReducer).doOnNext { println("render CL: $it") },
                                    shared.ofType(ClarifyingQuestions.Result::class.java).compose(asReducer1).doOnNext { println("render CQ: $it") },

                                    shared.compose(psReducer),

                                    shared.ofType(SubmitProposal.Result.NavigatedTo::class.java)
                                            .map {
                                                println(">>>>>><<<<<<<< index ${it.index}")
                                                it.index
                                            },

                                    shared.ofType(DoSubmitProposal.Result::class.java).compose(asReducer2).doOnNext { println("render DSP: $it") }
                            )
                    ) {
                        val cl = it[0] as com.example.domain.submitProposal2.coverLetter.CoverLetter.ViewState
                        val cq = it[1] as ClarifyingQuestions.ViewState
                        val ps = it[2] as ProposalSummaryViewState
                        val index = it[3] as Int
                        val dsp = it[4] as DoSubmitProposal.ViewState
                        SubmitProposal.ViewState(cl, cq, ps, dsp, index)
                    }
                }
    }
}
