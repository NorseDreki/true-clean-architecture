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
        val questionsValid: Boolean = false
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
                                state.copy(questionsValid = true)
                            else -> state
                        }
                    }
                    .map {
                        println("----------> $it")
                        with (it) {
                            if ((!isCoverLetterRequired || coverLetterValid)
                                    && (!areQuestionsRequired || questionsValid)) {
                                SubmitAllowedResult.Enabled
                            } else {
                                SubmitAllowedResult.Disabled
                            }
                        }
                    }
                    .distinctUntilChanged()
        }
