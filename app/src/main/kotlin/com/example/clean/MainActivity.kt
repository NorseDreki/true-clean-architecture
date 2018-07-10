package com.example.clean

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.clean.screens.Screen
import com.example.clean.screens.ToScreen
import com.example.domain.models.ItemDetails
import com.example.domain.models.Question
import com.example.domain.submitProposal.ClarifyingQuestions
import com.example.domain.submitProposal.CoverLetter
import com.example.domain.submitProposal.DoSubmitProposal
import com.example.domain.submitProposal.SubmitProposal
import com.google.gson.GsonBuilder
import com.upwork.android.core.BasicKeyParceler
import flow.Flow
import io.reactivex.Observable


class MainActivity : AppCompatActivity() {

    private lateinit var sp: SubmitProposal

    private lateinit var cl: CoverLetter

    lateinit var toScreen: ToScreen


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.basic_activity_frame)

        cl = CoverLetter()
        val cq = ClarifyingQuestions()
        val dsp = DoSubmitProposal()
        sp = SubmitProposal(cl, cq, dsp)
        toScreen = ToScreen(sp)

        val questions = listOf(
                Question("1", "q1"),
                Question("2", "q2"),
                Question("3", "q3")
        )


        val itemDetails = ItemDetails("1234", true, questions)

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

        Flow.get(this).set(screen)
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
