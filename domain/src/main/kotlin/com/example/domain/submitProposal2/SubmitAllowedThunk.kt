package com.example.domain.submitProposal2

import com.example.domain.Thunk
import com.example.domain.UiResult
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.coverLetter.CoverLetter
import io.reactivex.Observable

sealed class SubmitAllowedResult : UiResult {
    object Enabled : SubmitAllowedResult()
    object Disabled : SubmitAllowedResult()
}

class SubmitAllowedThunk : Thunk<UiResult, SubmitAllowedResult> {

    data class SubmitAllowedData(
            val isCoverLetterRequired: Boolean = true,
            val areQuestionsRequired: Boolean = true,
            val coverLetterValid: Boolean = false,
            val questionsValid: Boolean = false,
            val proposeTipValid: Boolean = false
    )

    override fun apply(upstream: Observable<UiResult>) =
            upstream
                    .scan(SubmitAllowedData()) { state, result ->
                        println("----------> SUBM ALL UIRES $result")
                        when (result) {

                            is CoverLetter.Result.Valid ->
                                state.copy(coverLetterValid = true)

                            CoverLetter.Result.Empty ->
                                state.copy(coverLetterValid = false)

                            is CoverLetter.Result.LengthExceeded ->
                                state.copy(coverLetterValid = false)

                            CoverLetter.Result.NotRequired ->
                                state.copy(isCoverLetterRequired = false)

                            ClarifyingQuestions.Result.NotRequired ->
                                state.copy(areQuestionsRequired = false)

                            is ClarifyingQuestions.Result.AnsweredQuestionsCount ->
                                state.copy(questionsValid = result.answeredCount == result.totalCount)

                            else -> state
                        }
                    }
                    .map {
                        with (it) {
                            if ((!isCoverLetterRequired || coverLetterValid)
                                    && (!areQuestionsRequired || questionsValid)
                                    //negate
                                    && !proposeTipValid) {
                                SubmitAllowedResult.Enabled
                            } else {
                                SubmitAllowedResult.Disabled
                            }
                        }
                    }
                    //.skip(1)
                    .distinctUntilChanged()
                    //.startWith(SubmitAllowedResult.Disabled)

                    .doOnNext { println("! ----------> $it") }!!
}
