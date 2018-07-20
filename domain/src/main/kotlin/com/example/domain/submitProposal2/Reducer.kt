package com.example.domain.submitProposal2

import com.example.domain.framework.asEmbedded
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.coverLetter.CoverLetter
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal
import com.example.domain.submitProposal2.proposalSummary.ProposalSummaryViewState
import com.example.domain.submitProposal2.proposalSummary.psReducer
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class Reducer(
        val coverLetter: com.example.domain.submitProposal2.coverLetter.CoverLetter,
        val clarifyingQuestions: ClarifyingQuestions,
        val doSubmitProposal: DoSubmitProposal
) : ObservableTransformer<SubmitProposal.Result, SubmitProposal.ViewState> {
    override fun apply(upstream: Observable<SubmitProposal.Result>): ObservableSource<SubmitProposal.ViewState> {
        return upstream.publish {shared ->
            Observable.combineLatest(
                    arrayOf(
                            shared.ofType(CoverLetter.Result::class.java).compose(coverLetter.asEmbedded().asReducer).doOnNext { println("render CL: $it") },
                            shared.ofType(ClarifyingQuestions.Result::class.java).compose(clarifyingQuestions.asEmbedded().asReducer).doOnNext { println("render CQ: $it") },

                            shared.compose(psReducer),

                            shared.ofType(SubmitProposal.Result.NavigatedTo::class.java)
                                    .map {
                                        println(">>>>>><<<<<<<< index ${it.index}")
                                        it.index
                                    },

                            shared.ofType(DoSubmitProposal.Result::class.java).compose(doSubmitProposal.asEmbedded().asReducer).doOnNext { println("render DSP: $it") }
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
