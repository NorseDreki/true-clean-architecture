package com.example.clean

import android.databinding.BindingAdapter
import android.databinding.adapters.ListenerUtil
import android.text.Editable
import android.widget.EditText
import java.text.NumberFormat
import java.util.*

object ModelBindingAdapters {
    private val numberFormatter = NumberFormat.getNumberInstance(Locale.US)
    val DEFAULT_NUMBER_VALUE = 0.0

    private fun areValuesSame(first: String, second: String): Boolean {
        val firstDouble = parseNumberOrDefault(first)
        val secondDouble = parseNumberOrDefault(second)
        return first.startsWith(second) && firstDouble == secondDouble
    }

    private fun parseNumberOrDefault(value: String): Double {
        try {
            return java.lang.Double.parseDouble(value)
        } catch (e: NumberFormatException) {
            return DEFAULT_NUMBER_VALUE
        }

    }

    @JvmStatic
    @BindingAdapter("onTextChanged")
    fun bindEditText(view: EditText, observableString: ObservableProperty<String>) {

        val watcher = object : AfterTextChangedListener() {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                println("text changed to $s, ${view.tag}, $observableString, $view")
                println("editext ${view.text}")

                //view.setText("changed")
                //view.selectAll()

                val value = s.toString()

                //view.postDelayed(
                observableString.set(if (value.length > 0) value else "")
                //)
            }
        }

        val oldListener = ListenerUtil.trackListener(view, watcher, R.id.afterTextChangeListener)

        if (oldListener != null) {
            view.removeTextChangedListener(oldListener)
        }

        if (view.tag.toString() == "q2") {
            println("setting error ${view.tag}")
            view.setError(view.tag.toString())
        }

        println("added watcher for $observableString ${view.tag} $view")
        view.addTextChangedListener(watcher)

        /*val newValue = observableString.get()
        if (view.text.toString() != newValue) {
            view.setText(newValue)
            view.setSelection(if (view.text != null) view.text.length else 0)
        }*/
    }
}
