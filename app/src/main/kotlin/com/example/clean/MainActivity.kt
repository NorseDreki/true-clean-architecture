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


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.basic_activity_frame)

        val cl = CoverLetter()
        val cq = ClarifyingQuestions()
        val sp = SubmitProposal(cl, cq)

        val itemDetails = ItemDetails("1234", false, null)
        val cmd =
                Observable.just(SubmitProposal.Command.DATA(itemDetails))
                        .cast(SubmitProposal.Command::class.java)

        sp.process(cmd).subscribe()

        sp.render().subscribe(this::changeKey)
    }

    fun changeKey(state: UiState) {
        Flow.get(this).set(state)

    }

    override fun attachBaseContext(baseContext: Context) {
        var baseContext = baseContext

        val gson = GsonBuilder()
                .registerTypeAdapter(Key::class.java, KeyTypeAdapter())
                .create()

        val p =  BasicKeyParceler(gson)

        baseContext = Flow.configure(baseContext, this) //
                .dispatcher(BasicDispatcher(this)) //
                .defaultKey(WelcomeScreen()) //
                .keyParceler(p) //
                .install()
        super.attachBaseContext(baseContext)
    }

    override fun onBackPressed() {
        if (!Flow.get(this).goBack()) {
            super.onBackPressed()
        }
    }

}

