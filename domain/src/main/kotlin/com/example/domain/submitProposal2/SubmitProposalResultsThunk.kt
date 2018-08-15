package com.example.domain.submitProposal2

import com.example.domain.Thunk
import com.example.domain.UiResult
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.coverLetter.CoverLetter
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal
import io.reactivex.Observable

class SubmitProposalResultsThunk : Thunk<UiResult, SubmitProposal.Result> {

    override fun apply(upstream: Observable<UiResult>) =
        upstream.flatMap {
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
                    Observable.just(SubmitProposal.Result.ProposalSent)
                }

                is DoSubmitProposal.Result.Error ->
                    Observable.just(SubmitProposal.Result.JobNoLongerAvailable)

                else -> Observable.empty()
            }
        }!!
}
