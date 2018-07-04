package com.example.clean

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.domain.UiState
import com.example.domain.models.ItemDetails
import com.example.domain.models.Question
import com.example.domain.submitProposal.ClarifyingQuestions
import com.example.domain.submitProposal.CoverLetter
import com.example.domain.submitProposal.SubmitProposal
import com.google.gson.GsonBuilder
import com.upwork.android.core.BasicKeyParceler
import flow.Flow
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import me.tatarka.bindingcollectionadapter2.ItemBinding


class MainActivity : AppCompatActivity() {

    private lateinit var sp: SubmitProposal

    private lateinit var cl: CoverLetter

    private lateinit var cqe: ClarifyingQuestionsEvents

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.basic_activity_frame)

        cl = CoverLetter()
        cle = CoverLetterEvents(cl)
        val cq = ClarifyingQuestions()
        cqe = ClarifyingQuestionsEvents(cq, ItemBinding.of(BR.v, R.layout.pineapple_question_item), this)
        sp = SubmitProposal(cl, cq)

        val questions = listOf(
                Question("1", "q1"),
                Question("2", "q2")
        )


        val itemDetails = ItemDetails("1234", true, questions)

        val cmd2 =
                Observable.just<SubmitProposal.Command>(SubmitProposal.Command.DATA(itemDetails))

        val cmd = ReplaySubject.create<SubmitProposal.Command>()


        cmd.onNext(SubmitProposal.Command.DATA(itemDetails))
        sp.process(cmd2).materialize().subscribe {
            println("MAT: $it")
        }


    }


    data class Screen(val state: QuestionsViewStateEvents,
                      val events: ClarifyingQuestionsEvents

    )

    private lateinit var cle: CoverLetterEvents


    data class QuestionViewStateEvents(
            val onChanged: ObservableProperty<String>,
            val wrapped: ClarifyingQuestions.QuestionViewState)//: ClarifyingQuestions.QuestionViewState(wrapped.id, wrapped.question, wrapped.answer)

    data class QuestionsViewStateEvents(
            val items: List<MainActivity.QuestionViewStateEvents> = listOf())

    private var st: List<QuestionViewStateEvents>? = null

    fun changeKey(state: UiState) {
        //cle = CoverLetterEvents(cl)



        val items = (state as SubmitProposal.ViewState)
                .clarifyingQuestions
                .items

        if (items.isEmpty()) return

        if (st == null && items.isNotEmpty()) {
            println("was null")
            st = (state as SubmitProposal.ViewState)
                    .clarifyingQuestions
                    .items
                    .map {
                        val qvs = QuestionViewStateEvents(onChanged = ObservableProperty(), wrapped = it)
                        qvs.onChanged.observe().subscribe {
                            println("!!!!! changed $it for $qvs")
                            cqe.onChanged.onNext(
                                    qvs
                            )
                        }
                        println("mapping $qvs")

                        qvs
                    }
        } else {
            println("notnull")
            st =
                    (state as SubmitProposal.ViewState).clarifyingQuestions.items
                            .zip(st!!)
                            .map {
                                println("mapping")
                                QuestionViewStateEvents(it.second.onChanged, it.first)
                            }

        }

        val screen = Screen(QuestionsViewStateEvents(st!!),
                cqe)

        println("resulting $st")

        Flow.get(this).set(screen)
        //println("11111111111 $state")
    }

    override fun attachBaseContext(baseContext: Context) {
        var baseContext = baseContext

        val gson = GsonBuilder()
                .registerTypeAdapter(Key::class.java, KeyTypeAdapter())
                .create()

        val p =  BasicKeyParceler(gson)

        baseContext = Flow.configure(baseContext, this) //
                .dispatcher(BasicDispatcher(this, DefaultDataBinder())) //
                .defaultKey(WelcomeScreen()) //
                .keyParceler(p) //
                .install()

        super.attachBaseContext(baseContext)
    }

    override fun onBackPressed() {
        sp.render().subscribe(this::changeKey)
        //Flow.get(this).set(WelcomeScreen())

        /*if (!Flow.get(this).goBack()) {
            super.onBackPressed()
        }*/
    }

}

