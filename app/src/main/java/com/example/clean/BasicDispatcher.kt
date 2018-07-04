package com.example.clean

import android.app.Activity
import android.support.annotation.LayoutRes
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import flow.Dispatcher
import flow.Flow
import flow.Traversal
import flow.TraversalCallback

internal class BasicDispatcher(
        private val activity: Activity,
        val dataBinder: DataBinder
) : Dispatcher {

    override fun dispatch(traversal: Traversal, callback: TraversalCallback) {
        Log.d("BasicDispatcher", "dispatching " + traversal)
        val destKey = traversal.destination.top<Any>()

        val frame = activity.findViewById<View>(R.id.basic_activity_frame) as ViewGroup

        // We're already showing something, clean it up.
        if (frame.childCount > 0) {
            val currentView = frame.getChildAt(0)

            // Save the outgoing view state.
            if (traversal.origin != null) {
                traversal.getState(traversal.origin!!.top()).save(currentView)
            }

            // Short circuit if we would just be showing the same view again.
            val currentKey = Flow.getKey<Any>(currentView)
            if (destKey == currentKey) {
                println("same stuff")
                callback.onTraversalCompleted()
                return
            }

            if (destKey is MainActivity.Screen && currentKey is MainActivity.Screen) {
                dataBinder.bind(currentView, destKey.state, destKey.events)
                println("same stuff for sure")
                callback.onTraversalCompleted()
                return
            }

            println("clean stuff")
            frame.removeAllViews()
        }

        @LayoutRes val layout: Int = when (destKey) {
            is WelcomeScreen -> R.layout.welcome_screen
            is MainActivity.Screen -> {
                println("zzzzzz $destKey")
                R.layout.pineapple_questions
            }
            else -> throw AssertionError("Unrecognized screen " + destKey)
        }

        val incomingView = LayoutInflater.from(traversal.createContext(destKey, activity)) //
                .inflate(layout, frame, false)

        if (destKey is MainActivity.Screen) {

            dataBinder.bind(incomingView, destKey.state, destKey.events)
        }

        frame.addView(incomingView)
        traversal.getState(traversal.destination.top()).restore(incomingView)

        callback.onTraversalCompleted()
    }
}
