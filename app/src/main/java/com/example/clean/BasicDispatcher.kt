package com.example.clean

import android.app.Activity
import android.support.annotation.LayoutRes
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.clean.screens.Screen
import flow.Dispatcher
import flow.Flow
import flow.Traversal
import flow.TraversalCallback

internal class BasicDispatcher(
        private val activity: Activity,
        val dataBinder: DataBinder
) : Dispatcher {

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

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
            /*if (destKey == currentKey) {
                println("SAME STUFF, not doing anything VVVV")
                println("current: $currentKey")
                println("dest: $destKey")
                callback.onTraversalCompleted()
                return
            }*/

            //if (destKey is Screen && currentKey is Screen) {
            if (destKey is Screen && destKey::class == currentKey!!::class) {
                /*val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(frame.windowToken, 0)*/

                /*currentView.postDelayed(
                        {currentView.clearFocus();}, 500
                )*/

                /*currentView.postDelayed(
                        {dataBinder.bind(currentView, destKey.state, destKey.events);
                        *//*currentView.clearFocus()*//*
                        currentView.requestFocus()},
                        2000
                )*/

                dataBinder.bind(currentView, destKey.state, destKey.events, destKey.listBindings);
                /*currentView.clearFocus()*/
                currentView.requestFocus()
                println("same stuff for sure ${destKey.state}")

                //hideKeyboard(activity)

                callback.onTraversalCompleted()
                return
            }

            println("clean stuff")
            frame.removeAllViews()
        }

        @LayoutRes val layout: Int = when (destKey) {
            is WelcomeScreen -> R.layout.welcome_screen
            is Screen -> {
                println("switched to key $destKey")
                destKey.layout
            }
            else -> throw AssertionError("Unrecognized screen " + destKey)
        }

        val incomingView = LayoutInflater.from(traversal.createContext(destKey, activity)) //
                .inflate(layout, frame, false)

        if (destKey is Screen) {

            dataBinder.bind(incomingView, destKey.state, destKey.events, destKey.listBindings)
        }

        frame.addView(incomingView)
        traversal.getState(traversal.destination.top()).restore(incomingView)

        callback.onTraversalCompleted()
    }
}
