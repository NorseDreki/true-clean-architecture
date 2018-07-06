package com.example.clean.screens

import com.example.clean.ClarifyingQuestionsEvents
import com.example.clean.R
import com.example.domain.submitProposal.ClarifyingQuestions
import com.example.domain.submitProposal.SubmitProposal

data class SubmitProposalScreen(
        override val state: SubmitProposal.ViewState,//ViewStateEvents,
        override val events: SubmitProposalScreenEvents
) : Screen {

    override val layout: Int
        get() = R.layout.submit_proposal_view_content



    companion object {
        var screen: SubmitProposalScreen? = null
        lateinit var events: SubmitProposalScreenEvents

        //var questions: List<MainActivity.QuestionViewStateEvents>? = listOf()

        private lateinit var cqe: ClarifyingQuestionsEvents

        fun fromState(submitProposal: SubmitProposal, state: SubmitProposal.ViewState): SubmitProposalScreen {
            if (screen == null) {

                cqe = ClarifyingQuestionsEvents(
                        submitProposal.clarifyingQuestions
                )
                events = SubmitProposalScreenEvents(
                        CoverLetterScreenEvents(submitProposal.coverLetter),
                        cqe
                )

                val items =
                        (state.clarifyingQuestions as ClarifyingQuestions.ViewState).items


                println("questions are emoty? ${items.isEmpty()}")



                /*val newState =
                        ViewStateEvents(
                                state.coverLetter,
                                MainActivity.QuestionsViewStateEvents(questions!!)
                        )*/

                screen = SubmitProposalScreen(state, events)

            } else {

                val items =
                        (state.clarifyingQuestions as ClarifyingQuestions.ViewState).items


                println("questions are emoty? ${items.isEmpty()}")

                /*println("ready to alter annotations")
                Annotations.alterAnnotationValueJDK7(
                        MainActivity.QuestionViewStateEvents::class.java,
                        Greeter::class.java,
                        DynamicGreeter("123"))

                when {
                    questions!!.isEmpty() && items.isNotEmpty() -> {
                        println("filling questions")
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
                    questions!!.isNotEmpty() && items.isNotEmpty() -> {
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
                        )*/

                screen = screen!!.copy(state)
            }

            return screen!!
        }

        fun wrapCQ() {

        }
    }
}


/*
data class ViewStateEvents(
        val coverLetter: CoverLetter.ViewState,
        val clarifyingQuestions: MainActivity.QuestionsViewStateEvents
) : UiState*/
