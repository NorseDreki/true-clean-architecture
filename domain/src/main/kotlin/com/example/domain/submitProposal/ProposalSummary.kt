package com.example.domain.submitProposal

import com.example.domain.UiCommand
import com.example.domain.UiComponent
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.common.JobTypeViewState
import com.example.domain.models.ItemOpportunity
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/*class ProposalSummaryComponent(
        *//*val proposeTermsResults: Observable<UiResult>,
        val coverLetterResults: Observable<UiResult>,
        val pineappleQuestionsResults: Observable<UiResult>*//*
        val allResults: Observable<UiResult> //+ attachments! or split?
) : UiComponent {

    val results = PublishSubject.create<UiResult>()

    override fun processCommands(commands: Observable<UiCommand>) {
        commands.compose(psProcessor).subscribe(results)
    }

    override fun produceResults(): Observable<UiResult> {
        return results.filter { it is ProposalSummaryResult.NavigationRequested || it == ProposalSummaryResult.SubmitProposalRequested }
    }

    override fun render(): Observable<UiState> {
        return results.mergeWith(allResults).compose(psReducer)
    }

    override fun asTransformer(): Observable.Transformer<UiResult, UiState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}*/

class ProposalSummary : UiComponent<ProposalSummary.Command, ProposalSummary.Result, SubmitProposal.ViewState> {
    override fun process(commands: Observable<Command>): Observable<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(): Observable<SubmitProposal.ViewState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    sealed class Command : UiCommand {
        data class INIT(val itemOpportunity: ItemOpportunity) : Command()

        data class ToggleSubmitEnabled(val enabled: Boolean) : Command()
    }


    sealed class Result : UiResult {
        internal data class INITResult(val itemOpportunity: ItemOpportunity) : Result()
        internal data class SubmitEnabled(val enabled: Boolean) : Result()

        data class NavigationRequested(val to: Int) : Result()
        object SubmitProposalRequested : Result()
    }

    val psProcessor =
            ObservableTransformer<UiCommand, UiResult> {
                it.map {
                    when (it) {
                        is Command.INIT -> Result.INITResult(it.itemOpportunity)
                        is Command.ToggleSubmitEnabled -> Result.SubmitEnabled(it.enabled)
                        else -> throw IllegalStateException("sdf")
                    }
                }

            }
}

data class JobInfoViewState(
        val title: String,
        val jobType: JobTypeViewState
) : UiState {
    companion object {
        fun init() = JobInfoViewState("", JobTypeViewState.init())
    }
}

val psReducer =
        ObservableTransformer<UiResult, ProposalSummaryViewState> {
            it.scan(ProposalSummaryViewState.init()) { state, result ->
                val s = state as ProposalSummaryViewState
                when (result) {
                /*is CoverLetter.Result.NoCoverLetterRequired -> {
                    s.copy(hasCoverLetter = false)
                }*/
                    is SubmitProposal.Result.ProposalLoaded -> {
                        s.copy(hasCoverLetter = result.itemOpportunity.itemDetails.isCoverLetterRequired)
                    }
                    is CoverLetter.Result.Valid -> {
                        s.copy(
                                coverLetter = result.coverLetter,
                                isCoverLetterValid = true
                        )
                    }
                    is CoverLetter.Result.LengthExceeded ->
                        s.copy(
                                coverLetter = result.coverLetter,
                                isCoverLetterValid = false
                        )
                    CoverLetter.Result.Empty ->
                        s.copy(
                                coverLetter = "",
                                isCoverLetterValid = false
                        )
                    ClarifyingQuestions.Result.NoQuestionsRequired -> {
                        s.copy(
                                hasQuestions = false
                        )
                    }
                    is ClarifyingQuestions.Result.QuestionsLoaded -> {
                        s.copy(
                                hasQuestions = true,
                                questions = result.questions.map {
                                    ClarifyingQuestions.QuestionViewState(it.id, it.question, null)
                                },
                                totalQuestions = result.questions.size
                        )
                    }
                    is ClarifyingQuestions.Result.ValidAnswer -> {
                        val count = s.answeredQuestions + 1
                        s.copy(
                                questions = s.questions.map {
                                    if (it.id == result.id)
                                        ClarifyingQuestions.QuestionViewState(it.id, it.question, result.answer)
                                    else
                                        it
                                },
                                answeredQuestions = if (count > s.totalQuestions) s.totalQuestions else count,
                                areQuestionsValid = count == s.totalQuestions
                        )
                    }
                    is ClarifyingQuestions.Result.EmptyAnswer -> {
                        val count = s.answeredQuestions - 1
                        s.copy(
                                questions = s.questions.map {
                                    if (it.id == result.id)
                                        ClarifyingQuestions.QuestionViewState(it.id, it.question, null)
                                    else
                                        it
                                },
                                answeredQuestions = count,
                                areQuestionsValid = false
                        )
                    }
                    else -> {
                        s
                    }
                }
            }
                    .distinctUntilChanged()
        }


data class ProposalSummaryViewState(
        val hasCoverLetter: Boolean,
        val hasQuestions: Boolean,

        val coverLetter: String,
        val isCoverLetterValid: Boolean,

        val answeredQuestions: Int,
        val totalQuestions: Int,
        val areQuestionsValid: Boolean,

        val questions: List<ClarifyingQuestions.QuestionViewState>
) : UiState {
    companion object {
        fun init() = ProposalSummaryViewState(
                false, false, "", false,
                0, 0, false, listOf()
        )
    }
}
