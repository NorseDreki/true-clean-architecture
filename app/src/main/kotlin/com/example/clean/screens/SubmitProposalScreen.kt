package com.example.clean.screens

import com.example.clean.*
import com.example.domain.UiState
import com.example.domain.submitProposal.ClarifyingQuestions
import com.example.domain.submitProposal.CoverLetter
import com.example.domain.submitProposal.SubmitProposal
import me.tatarka.bindingcollectionadapter2.ItemBinding

data class SubmitProposalScreen(
        override val state: ViewStateEvents,
        override val events: SubmitProposalScreenEvents
) : Screen {

    override val layout: Int
        get() = R.layout.submit_proposal_view_content



    companion object {
        var screen: SubmitProposalScreen? = null
        lateinit var events: SubmitProposalScreenEvents

        var questions: List<MainActivity.QuestionViewStateEvents>? = listOf()

        fun fromState(submitProposal: SubmitProposal, state: SubmitProposal.ViewState): SubmitProposalScreen {
            if (screen == null) {

                val cqe = ClarifyingQuestionsEvents(
                        submitProposal.clarifyingQuestions,
                        ItemBinding.of(BR.v, R.layout.clarifying_question_item)
                )
                events = SubmitProposalScreenEvents(
                        CoverLetterScreenEvents(submitProposal.coverLetter),
                        cqe
                )

                val items =
                        (state.clarifyingQuestions as ClarifyingQuestions.ViewState).items

                when {
                    questions == null && items.isNotEmpty() -> {
                        questions = items
                                .map {
                                    val qvs = MainActivity.QuestionViewStateEvents(onChanged = ObservableProperty(), wrapped = it)
                                    qvs.onChanged.observe().subscribe {
                                        println("!!!!! changed $it for $qvs")
                                        cqe.onChanged.onNext(
                                                qvs
                                        )
                                    }
                                    println("mapping $qvs")

                                    qvs
                                }
                    }
                    questions == null && items.isNotEmpty() -> {
                        questions = items
                                .zip(questions!!)
                                .map {
                                    println("mapping")
                                    MainActivity.QuestionViewStateEvents(it.second.onChanged, it.first)
                                }
                    }

                }

                val newState =
                        ViewStateEvents(
                                state.coverLetter,
                                MainActivity.QuestionsViewStateEvents(questions!!)
                        )

                screen = SubmitProposalScreen(newState, events)

            } else {

                val newState =
                        ViewStateEvents(
                                state.coverLetter,
                                MainActivity.QuestionsViewStateEvents(questions!!)
                        )

                screen = screen!!.copy(newState)
            }

            return screen!!
        }

        fun wrapCQ() {

        }
    }
}


data class ViewStateEvents(
        val coverLetter: CoverLetter.ViewState,
        val clarifyingQuestions: MainActivity.QuestionsViewStateEvents
) : UiState