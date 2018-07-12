package com.example.clean

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.clean.screens.Screen
import com.example.clean.screens.SubmitProposalScreen
import com.example.clean.screens.ToScreen
import com.example.domain.models.ItemDetails
import com.example.domain.models.Question
import com.example.domain.submitProposal.*
import com.google.gson.GsonBuilder
import com.upwork.android.core.BasicKeyParceler
import flow.Direction
import flow.Flow
import io.reactivex.Observable


class MainActivity : AppCompatActivity() {

    private lateinit var sp: SubmitProposal

    private lateinit var cl: CoverLetter

    lateinit var toScreen: ToScreen


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.basic_activity_frame)

        val flowNavigator = FlowNavigator(this)

        cl = CoverLetter()
        val cq = ClarifyingQuestions()
        val dsp = DoSubmitProposal(flowNavigator, ProposalConfirmation())
        sp = SubmitProposal(cl, cq, dsp)
        toScreen = ToScreen(sp)

        val questions = listOf(
                Question("1", "q1"),
                Question("2", "q2"),
                Question("3", "q3")
        )


        val itemDetails = ItemDetails("1234", true, null)

        val cmd2 =
                Observable.just<SubmitProposal.Command>(SubmitProposal.Command.DATA(itemDetails))

        sp.process(cmd2).materialize().subscribe {
            println("MAT: $it")
        }

        val view = findViewById<View>(android.R.id.content)
        view.postDelayed(
                {sp.render().compose(toScreen).subscribe(this::changeKey)}, 1500
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

        val p =  BasicKeyParceler(gson)

        baseContext = Flow.configure(baseContext, this) //
                .dispatcher(BasicDispatcher(this, DefaultDataBinder())) //
                .defaultKey(WelcomeScreen()) //
                .keyParceler(p) //
                .install()

        super.attachBaseContext(baseContext)
    }


    override fun onBackPressed() {
        sp.fromEvent(SubmitProposal.Command.ToNextStep)
        //sp.render().compose(toScreen).subscribe(this::changeKey)
        //Flow.get(this).set(WelcomeScreen())

        /*if (!Flow.get(this).goBack()) {
            super.onBackPressed()
        }*/
    }
}
