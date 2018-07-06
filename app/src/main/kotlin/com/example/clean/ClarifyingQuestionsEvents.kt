package com.example.clean

import com.example.domain.submitProposal.ClarifyingQuestions
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ClarifyingQuestionsEvents(
        val clarifyingQuestions: ClarifyingQuestions

) : OnItemClickListener {

    val itemBinding: ItemBinding<MainActivity.QuestionViewStateEvents>


    override fun onItemClick(item: ClarifyingQuestions.QuestionViewState) {

        println("item click")
        //clarifyingQuestions.fromEvent(ClarifyingQuestions.Command.UpdateAnswer(item.id, item.onChanged.get()
    }

    val onTextChanged = ObservableProperty<String>()

    val onChanged = PublishSubject.create<MainActivity.QuestionViewStateEvents>()

    init {
        itemBinding = ItemBinding.of<MainActivity.QuestionViewStateEvents>(BR.v, R.layout.clarifying_question_item)
                .bindExtra(BR.listener, this)

        println("init CLE")
        onTextChanged.observe().subscribe {
            println("00000000CQ change $it");
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

interface OnItemClickListener {
    fun onItemClick(item: ClarifyingQuestions.QuestionViewState)
}