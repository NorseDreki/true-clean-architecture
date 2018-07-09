package com.example.clean.screens

import com.example.clean.BR
import com.example.clean.ObservableProperty
import com.example.clean.R
import com.example.domain.submitProposal.ClarifyingQuestions
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ClarifyingQuestionsEvents(
        val clarifyingQuestions: ClarifyingQuestions

) : OnItemClickListener {

    val itemBinding: ItemBinding<ClarifyingQuestions.QuestionViewState>

    fun blah() {
        println("blah")
    }


    override fun onItemClick(item: ClarifyingQuestions.QuestionViewState?, text: CharSequence) {
        clarifyingQuestions.fromEvent(ClarifyingQuestions.Command.UpdateAnswer(item!!.id, text.toString()))

//        println("item click")
        //clarifyingQuestions.fromEvent(ClarifyingQuestions.Command.UpdateAnswer(item.id, item.onChanged.get()
    }

    val onTextChanged = ObservableProperty<String>()

    //val onChanged = PublishSubject.create<MainActivity.QuestionViewStateEvents>()

    init {
        itemBinding = ItemBinding.of<ClarifyingQuestions.QuestionViewState>(BR.v, R.layout.clarifying_question_item)
                .bindExtra(BR.listener, this)
                .bindExtra(BR.some, Some())

        println("init CLE")
        onTextChanged.observe().subscribe {
            println("00000000CQ change $it");

            //clarifyingQuestions.fromEvent(ClarifyingQuestions.Command.UpdateAnswer(it.id, it.onChanged.get()
            //coverLetter.fromEvent(CoverLetter.Command.UpdateCoverLetter(it))
        }

        /*onChanged.onErrorResumeNext(
                Observable.empty<MainActivity.QuestionViewStateEvents>()
        ).subscribe {
            println("CUMCHNAGE")

            //activity.runOnUiThread {
                clarifyingQuestions.fromEvent(ClarifyingQuestions.Command.UpdateAnswer(it.wrapped.id, it.onChanged.get()
                        ?: ""))
            //}
        }*/
    }




}

class Some :Runnable {
    override fun run() {
        println("dsfads")
    }
}

val some = { println("23421")}

interface OnItemClickListener {
    fun onItemClick(item: ClarifyingQuestions.QuestionViewState?, text: CharSequence)
}