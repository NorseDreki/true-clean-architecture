package com.example.domain.submitProposal2

import com.example.domain.UiResult
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.coverLetter.CoverLetter
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class PublishedResults : ObservableTransformer<UiResult, SubmitProposal.Result> {

    override fun apply(upstream: Observable<UiResult>): ObservableSource<SubmitProposal.Result> {
        return upstream.flatMap {
            when (it) {
                CoverLetter.Result.Empty,
                is CoverLetter.Result.Valid,
                is ClarifyingQuestions.Result.AnswerValid,
                is ClarifyingQuestions.Result.AnswerEmpty
                    /*is ProposeTerms.Result.BidValid,
                    ProposeTerms.Result.BidEmpty*/ ->//,
                    //is ProposeTerms.Result.EngagementSelected ->

                    Observable.just(SubmitProposal.Result.ProposalUpdated)

                is DoSubmitProposal.Result.Success -> {
                    println("SUCC RES")
                    Observable.just(SubmitProposal.Result.ProposalSent)
                }

                is DoSubmitProposal.Result.Error ->
                    Observable.just(SubmitProposal.Result.JobNoLongerAvailable)

                else -> Observable.empty()
            }
        }
    }
}
