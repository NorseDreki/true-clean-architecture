package com.example.domain.submitProposal

import com.example.domain.UiResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

sealed class SubmitAllowedResult : UiResult {
    object Enabled : SubmitAllowedResult()
    object Disabled : SubmitAllowedResult()
}

data class SubmitAllowedData(
        val hasCoverLetter: Boolean = false,
        val coverLetterValid: Boolean = true,
        val attachmentsValid: Boolean = true,
        val hasQuestions: Boolean = false,
        val questionsValid: Boolean = true
)

fun <T> T.smth() where T: UiResult,  T : ClarifyingQuestions.Result.Questions {

}


val submitAllowedProcessor =
        ObservableTransformer<UiResult, SubmitAllowedResult> {
            it
                    .filter {
                        it is ClarifyingQuestions.Result.AllQuestionsAnswered
                    }
                    .cast(ClarifyingQuestions.Result.AllQuestionsAnswered::class.java)
                    /*.publish { shared ->
                        Observable.merge(
                                shared.ofType(ClarifyingQuestions.Result::class.java),
                                shared.ofType(ClarifyingQuestions.Result::class.java)
                        )

                    }*/
                    .scan(SubmitAllowedData()) { state, result ->
                        when (result) {
                        /*CoverLetter.Result.Empty -> state.copy(hasCoverLetter = true, coverLetterValid = false)
                        is UpdateCoverLetterResult.Valid -> state.copy(hasCoverLetter = true, coverLetterValid = true)
                        is UpdateCoverLetterResult.LengthExceeded -> state.copy(hasCoverLetter = true, coverLetterValid = false)
                        is PineappleQuestionsResult.EmptyAnswer -> state.copy(hasQuestions = true, questionsValid = false)
                        is PineappleQuestionsResult.Valid -> state.copy(hasQuestions = true, questionsValid = false)*/
                            is ClarifyingQuestions.Result.AllQuestionsAnswered -> state.copy(hasQuestions = true, questionsValid = false)
                            else -> state
                        }
                    }
                    .distinctUntilChanged()
                    .scan<SubmitAllowedResult>(SubmitAllowedResult.Disabled) { state, result ->
                        with(result) {
                            if ((!hasCoverLetter || coverLetterValid) && (!hasQuestions || questionsValid)) {
                                SubmitAllowedResult.Enabled
                            } else {
                                SubmitAllowedResult.Disabled
                            }
                        }
                    }
        }
