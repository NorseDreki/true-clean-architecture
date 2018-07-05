package com.example.clean

import com.example.domain.submitProposal.ClarifyingQuestions
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ClarifyingQuestionsEvents(
        val clarifyingQuestions: ClarifyingQuestions,
        val itemBinding: ItemBinding<MainActivity.QuestionViewStateEvents>
) {

    val onTextChanged = ObservableProperty<String>()

    val onChanged = PublishSubject.create<MainActivity.QuestionViewStateEvents>()

    init {
        println("init CLE")
        onTextChanged.observe().subscribe {
            println("00000000 change $it");
            //coverLetter.fromEvent(CoverLetter.Command.UpdateCoverLetter(it))
        }

        onChanged.onErrorResumeNext(
                Observable.empty<MainActivity.QuestionViewStateEvents>()
        ).subscribe {
            println("CUMCHNAGE")

            //activity.runOnUiThread {
                clarifyingQuestions.fromEvent(ClarifyingQuestions.Command.UpdateAnswer(it.wrapped.id, it.onChanged.get()
                        ?: ""))
            //}
        }
    }
}
