package com.example.domain.submitProposal.coverLetter2

import com.example.domain.submitProposal.coverLetter2.CoverLetter2.Command
import com.example.domain.submitProposal.coverLetter2.CoverLetter2.Result
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class CoverLetterProcessor : ObservableTransformer<Command, Result> {

    override fun apply(upstream: Observable<Command>) =
        upstream//.debounce(3, TimeUnit.SECONDS)
                .map {
                    when (it) {
                        is Command.DATA -> {
                            if (it.itemOpportunity.itemDetails.isCoverLetterRequired) {
                                result(it.itemOpportunity.proposal.coverLetter)
                            } else {
                                Result.NoCoverLetterRequired
                            }
                        }
                        is Command.UpdateCoverLetter -> {
                            result(it.coverLetter)
                        }
                    }
                }

    private fun result(coverLetter: String): Result {
        //save cover letter to submit proposal storage?

        val validated = coverLetter.trim()

        val maxLength = 5000
        return when {
            validated.length > maxLength -> Result.LengthExceeded(validated, maxLength)
            validated.isNotEmpty() -> Result.Valid(validated)
            else -> Result.Empty
        }
    }
}
