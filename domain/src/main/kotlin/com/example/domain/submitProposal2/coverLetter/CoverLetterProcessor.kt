package com.example.domain.submitProposal2.coverLetter

import com.example.domain.Processor
import com.example.domain.submitProposal2.coverLetter.CoverLetter.Command
import com.example.domain.submitProposal2.coverLetter.CoverLetter.Result
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class CoverLetterProcessor : Processor<Command, Result> {

    override fun apply(upstream: Observable<Command>) =
        upstream
                .debounce(3, TimeUnit.SECONDS)
                .map {
                    when (it) {
                        is Command.START -> {
                            if (it.itemOpportunity.itemDetails.isCoverLetterRequired) {
                                validate(it.itemOpportunity.proposal.coverLetter)
                            } else {
                                Result.NotRequired
                            }
                        }
                        is Command.Update -> {
                            validate(it.coverLetter)
                        }
                    }
                }!!

    private fun validate(coverLetter: String): Result {
        val validated = coverLetter.trim()
        val limit = 5000

        return when {
            validated.length > limit -> Result.LengthExceeded(validated, limit)
            validated.isNotEmpty() -> Result.Valid(validated)
            else -> Result.Empty
        }
    }
}
