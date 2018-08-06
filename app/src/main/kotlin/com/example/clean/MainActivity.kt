package com.example.clean

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.clean.screens2.Screen
import com.example.clean.screens2.SubmitProposalScreen
import com.example.domain.UiState
import com.example.domain.framework.asStandalone
import com.example.domain.framework.extraCommand
import com.example.domain.models.ItemDetails
import com.example.domain.models.Question
import com.example.domain.submitProposal.SubmitProposal
import com.example.domain.submitProposal2.suggestedTip.SuggestedTip
import com.example.domain.submitProposal2.suggestedTip.UserSuggestion
import com.google.gson.GsonBuilder
import com.upwork.android.core.BasicKeyParceler
import flow.Direction
import flow.Flow
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers


class MainActivity : AppCompatActivity() {

    private lateinit var sp2: com.example.domain.submitProposal2.SubmitProposal

    val changer = Changer()

    fun cr(): com.example.domain.submitProposal2.SubmitProposal {
        val flowNavigator2 = FlowNavigator2(this, changer)
        val pt = com.example.domain.submitProposal2.proposeTip.ProposeTip()
        val pc = com.example.domain.submitProposal2.doSubmitProposal.proposalConfirmation.ProposalConfirmation(flowNavigator2)
        val cl = com.example.domain.submitProposal2.coverLetter.CoverLetter()
        val cq = com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions()
        val dsp = com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal(flowNavigator2, pc)

        val st = SuggestedTip(object : UserSuggestion{
            override fun getSuggestionForUser(): Int {
                return 200
            }

            override fun hasSuggestionAvailable(): Boolean {
                return true
            }
        })

        val sp = com.example.domain.submitProposal2.SubmitProposal(pt, st, cl, cq, dsp)

        return sp
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.basic_activity_frame)

        //val flowNavigator = FlowNavigator(this)
       // val flowNavigator2 = FlowNavigator2(this)


        val questions = listOf(
                Question("1", "q1"),
                Question("2", "q2"),
                Question("3", "q3")
        )


        val itemDetails = ItemDetails("1234", true, questions)

        val cmd2 =
                Observable.just<SubmitProposal.Command>(SubmitProposal.Command.DATA(itemDetails))

        /*sp.process(cmd2).materialize().subscribe {
            println("MAT: $it")
        }*/

        sp2 = cr()

        val view = findViewById<View>(android.R.id.content)
        view.postDelayed(
                {
                    val flow = Flow.get(this)
                    //sp.render().compose(toScreen).subscribe(this::changeKey)

                    /*sp2.asStandalone(com.example.domain.submitProposal2.SubmitProposal.Command.DATA(itemDetails))
                            .viewStates()
                            .compose(com.example.clean.screens2.ToScreen(sp2))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::changeKey)*/

                    val toS = com.example.domain.submitProposal2.SubmitProposal.ViewState::class.java to com.example.clean.screens2.ToScreen(sp2)

                    changer.setTop(sp2.asStandalone(com.example.domain.submitProposal2.SubmitProposal.Command.DATA(itemDetails))
                            .viewStates().cast(UiState::class.java),
                            toS as Pair<Class<UiState>, ObservableTransformer<UiState, Screen>>
                            )

                    changer.screens()/*.compose { obs ->
                        obs.map {
                            when (it) {
                                is com.example.domain.submitProposal2.SubmitProposal.ViewState ->
                                    Observable.just(it).compose(com.example.clean.screens2.ToScreen(sp2))
                            *//*is ProposalConfirmation.ViewState ->
                                    Observable.just(it).compose()*//*
                                else -> throw IllegalStateException("sddf")
                            }
                        }
                    }.flatMap { it }*/
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                println("flow set @ ${Thread.currentThread().name}")
                                flow.set(it)
                            }

                }, 1500
        )
    }

    fun changeKey(screen: Screen) {
        println(">>>>>>>>>>>>>>>> changing key to $screen")

        val flow = Flow.get(this)


        val hasSp = flow.history.iterator().asSequence().firstOrNull { it is SubmitProposalScreen }

        if (hasSp == null) {
            println("navigate to sp")
            flow.set(screen)

        } else {

            val newHistoryList = flow.history.map {
                if (it is SubmitProposalScreen) {
                    screen
                } else {
                    it
                }
            }.reversed()//.forEach { println("HISTORY $it") }

            newHistoryList.forEach { println("HISTORY $it") }

            val newHistory = flow.history.buildUpon().clear().pushAll(newHistoryList).build()

            flow.setHistory(newHistory, Direction.REPLACE)
        }


        /*val top = flow.history.iterator().next()
        println("top is $top")

        if (hasSp == null || top is SubmitProposalScreen) {
            println("navigate to sp")
            flow.set(screen)
        }*/
    }

    override fun attachBaseContext(baseContext: Context) {
        var baseContext = baseContext

        val gson = GsonBuilder()
                .registerTypeAdapter(Key::class.java, KeyTypeAdapter())
                .create()

        val p = BasicKeyParceler(gson)


        baseContext = Flow.configure(baseContext, this) //
                .dispatcher(BasicDispatcher(this, DefaultDataBinder())) //
                .defaultKey(WelcomeScreen()) //
                .keyParceler(p) //
                .install()

        super.attachBaseContext(baseContext)
    }


    override fun onBackPressed() {
        //sp.fromEvent(SubmitProposal.Command.ToNextStep)
        sp2.extraCommand(com.example.domain.submitProposal2.SubmitProposal.Command.ToNextStep)

        //sp.render().compose(toScreen).subscribe(this::changeKey)
        //Flow.get(this).set(WelcomeScreen())

        /*if (!Flow.get(this).goBack()) {
            super.onBackPressed()
        }*/
    }
}
