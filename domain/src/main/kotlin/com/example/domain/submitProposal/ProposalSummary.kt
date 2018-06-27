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
                    when(it) {
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


data class ProposalSummaryViewState(
        val jobType: JobTypeViewState,
        val bid: Int,
        val earn: Int,

        val hasCoverLetter: Boolean,
        val hasQuestions: Boolean,

        val isSubmitEnabled: Boolean,
        val coverLetter: String,
        val isCoverLetterFilledCorrectly: Boolean,
        val answeredQuestions: Int,
        val totalQuestions: Int,
        val areQuestionsValid: Boolean,
        val isCoverLetterAttValid: Boolean,

        val questions: List<String>
) : UiState {
    companion object {
        fun init() = ProposalSummaryViewState(
                JobTypeViewState.init(),
                0, 0, false, false, false, "",
                false, 0, 0, false, false,
                listOf()
        )
    }
}
