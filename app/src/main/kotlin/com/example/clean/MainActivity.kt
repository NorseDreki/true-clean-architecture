package com.example.clean

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.upwork.android.core.BasicKeyParceler
import flow.Flow


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.basic_activity_frame)
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

