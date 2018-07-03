package com.example.clean

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.domain.UiState
import com.example.domain.models.ItemDetails
import com.example.domain.submitProposal.ClarifyingQuestions
import com.example.domain.submitProposal.CoverLetter
import com.example.domain.submitProposal.SubmitProposal
import com.google.gson.GsonBuilder
import com.upwork.android.core.BasicKeyParceler
import flow.Flow
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject


class MainActivity : AppCompatActivity() {

    private lateinit var sp: SubmitProposal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.basic_activity_frame)

        val cl = CoverLetter()
        val cq = ClarifyingQuestions()
        sp = SubmitProposal(cl, cq)

        val itemDetails = ItemDetails("1234", true, null)

        val cmd2 =
                Observable.just<SubmitProposal.Command>(SubmitProposal.Command.DATA(itemDetails))

        val cmd = ReplaySubject.create<SubmitProposal.Command>()


        cmd.onNext(SubmitProposal.Command.DATA(itemDetails))
        sp.process(cmd2).materialize().subscribe {
            println("MAT: $it")
        }


    }

    fun changeKey(state: UiState) {
        Flow.get(this).set(state)
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

