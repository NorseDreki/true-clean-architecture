package com.example.domain.submitProposal

import com.example.domain.UiResult
import io.reactivex.ObservableTransformer

sealed class SubmitAllowedResult : UiResult {
    object Enabled : SubmitAllowedResult()
    object Disabled : SubmitAllowedResult()
}

data class SubmitAllowedData(
        val isCoverLetterRequired: Boolean = true,
        val areQuestionsRequired: Boolean = true,
        val coverLetterValid: Boolean = false,
        //val attachmentsValid: Boolean = false,
        val questionsValid: Boolean = false,
        val proposeTermsValid: Boolean = false
)

val submitAllowedProcessor =
        ObservableTransformer<UiResult, SubmitAllowedResult> {
            it
                    .scan(SubmitAllowedData()) { state, result ->
                        when (result) {
                            CoverLetter.Result.NoCoverLetterRequired ->
                                state.copy(isCoverLetterRequired = false)

                            ClarifyingQuestions.Result.NoQuestionsRequired ->
                                state.copy(areQuestionsRequired = false)

                            is ClarifyingQuestions.Result.AllQuestionsAnswered ->
                                state.copy(questionsValid = result.answered)

                            is ProposeTerms.Result.Validation.OK ->
                                    state.copy(proposeTermsValid = true)

                            is ProposeTerms.Result.Validation.Failed ->
                                    state.copy(proposeTermsValid = false)

                            else -> state
                        }
                    }
                    .map {
                        with (it) {
                            if ((!isCoverLetterRequired || coverLetterValid)
                                    && (!areQuestionsRequired || questionsValid)
                                    //negate
                                    && !proposeTermsValid) {
                                SubmitAllowedResult.Enabled
                            } else {
                                SubmitAllowedResult.Disabled
                            }
                        }
                    }
                    .skip(1)
                    .distinctUntilChanged()
                    .doOnNext { println("----------> $it") }
        }
