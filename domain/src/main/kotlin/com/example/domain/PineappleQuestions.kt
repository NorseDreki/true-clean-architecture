package com.example.domain


/*
class PineappleQuestions : UiComponent {

    val results = PublishSubject.create<UiResult>()

    override fun processCommands(commands: Observable<UiCommand>) {
        commands.compose(paProcessor).subscribe(results)
    }

    override fun produceResults(): Observable<UiResult> {
        return results.publish { shared ->
            Observable.concat(
                shared,
                shared.compose(paAnsweredProcessor)
            )
        }
    }

    override fun render(): Observable<UiState> {
        produceResults().compose(pineappleQuestionsReducer)
    }

    override fun asTransformer(): Observable.Transformer<UiResult, UiState> {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

sealed class PineappleQuestionsEvents : UiEvent {

    data class PineappleQuestionAnswerUpdated(val question: String, val answer: String): PineappleQuestionsEvents()
}

sealed class PineappleQuestionsResult : UiResult {

    data class Questions(val questions: List<Question>) : PineappleQuestionsResult()
    object NoQuestions: PineappleQuestionsResult()

    data class Valid(val question: String, val answer: String) : PineappleQuestionsResult()
    data class EmptyAnswer(val question: String) : PineappleQuestionsResult()

    data class AllQuestionsAnswered(val answered: Boolean) : PineappleQuestionsResult()
}

sealed class PineappleQuestionCommand : UiCommand {

    data class INIT(val jobDetails: JobDetails, val proposal: Proposal) : PineappleQuestionCommand()

    //only for internal use? from event
    data class UpdatePineappleQuestionAnswer(val question: String, val answer: String) : PineappleQuestionCommand()
}

val toPQCommands =
    Observable.Transformer<UiEvent, UiCommand> {
        it.map {
            when (it) {
                is PineappleQuestionsEvents.PineappleQuestionAnswerUpdated ->
                    PineappleQuestionCommand.UpdatePineappleQuestionAnswer(event.question, event.answer)
                else -> { throw IllegalStateException("sdaf")
                }
            }
        }
    }

data class AllQuestionsAnswered(
    val totalQuestions: Int? = null,
    val answeredQuestions: MutableSet<String> = mutableSetOf()
)

val paAnsweredProcessor =
    Observable.Transformer<UiResult, UiResult> { t ->
        t.scan(AllQuestionsAnswered()) { state, result ->
            when (result) {
                is PineappleQuestionsResult.Questions -> {
                    state.copy(result.questions.size, mutableSetOf())
                }
                is PineappleQuestionsResult.NoQuestions -> {
                    state.copy(0, mutableSetOf())
                }
                is PineappleQuestionsResult.Valid -> {
                    state.copy(answeredQuestions = state.answeredQuestions.apply {add(result.question)})
                }
                is PineappleQuestionsResult.EmptyAnswer -> {
                    state.copy(answeredQuestions = state.answeredQuestions.apply {remove(result.question)})
                }
                else -> throw IllegalStateException("sdf")
            }
        }
            .map {
                //implicitly handles "no questions" case
                PineappleQuestionsResult.AllQuestionsAnswered(it.totalQuestions == it.answeredQuestions.size)
            }
    }

val paProcessor =
    Observable.Transformer<UiCommand, UiResult> { t ->
        t.map {
            when (it) {
                is PineappleQuestionCommand.INIT -> {
                    PineappleQuestionsResult.Questions(it.jobDetails.job.questions)
                }
                is PineappleQuestionCommand.UpdatePineappleQuestionAnswer -> {
                    val validated = it.answer.trim()


                    if (validated.isNotEmpty()) {
                        PineappleQuestionsResult.Valid(it.question, "sdaf")
                    } else {
                        PineappleQuestionsResult.EmptyAnswer(it.question)
                    }
                }
                else -> throw IllegalStateException("sdf")
            }
        }
    }

val pineappleQuestionsReducer =
    { state: PineappleQuestionsViewState, result: UiResult ->
        when (result) {
            is PineappleQuestionsResult.Valid -> {
                val old =
                    state.items.find { it.question == result.question }

                //old?.apply { answer = result.answer }

                state
            }
            else -> throw IllegalStateException("sdaf")
        }
    }

data class PineappleQuestionsViewState(
    val items : List<PineappleQuestion>
) : UiState
*/