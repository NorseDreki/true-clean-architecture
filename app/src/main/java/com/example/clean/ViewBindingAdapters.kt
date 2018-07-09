package com.example.clean

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.support.constraint.ConstraintLayout
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

import io.reactivex.subjects.Subject


object ViewBindingAdapters {

    @JvmStatic
    @BindingAdapter("dialog")
    fun bindDialog(view: View, visible: Boolean) {
        bindVisibility(view, visible, View.GONE)
    }

    @JvmStatic
    @BindingAdapter("visible")
    fun bindVisibilityGone(view: View, visible: Boolean) {
        bindVisibility(view, visible, View.GONE)
    }

    @JvmStatic
    @BindingAdapter(value = *arrayOf("visible", "visibilityWhenFalse"))
    fun bindVisibility(view: View, visible: Boolean, visibilityWhenFalse: Int) {
        view.visibility = if (visible) View.VISIBLE else visibilityWhenFalse

        if (view.parent is ConstraintLayout) {
            val parent = view.parent as ConstraintLayout
            parent.requestLayout()
        }
    }

    @JvmStatic
    @BindingAdapter("onClick")
    fun bindOnClick(view: View, onClicked: Subject<View>) {
        view.setOnClickListener(View.OnClickListener { onClicked.onNext(it) })
    }

    @JvmStatic
    @BindingAdapter("onTouch")
    fun bindOnTouch(view: View, onTouched: Subject<MotionEvent>) {
        view.setOnTouchListener { touchView, event ->
            onTouched.onNext(event)
            false
        }
    }

    @JvmStatic
    @BindingConversion
    fun convertObservablePropertyToProperty(observableProperty: ObservableProperty<Boolean>): Boolean? {
        return observableProperty.get()
    }

    @JvmStatic
    @BindingAdapter("hasFocus")
    fun bindHasFocus(view: EditText, focused: ObservableProperty<Boolean>) {
        bindHasFocus(view as View, focused)

        val imm = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (focused.get()!!) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        } else {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    @JvmStatic
    @BindingAdapter("hasFocus")
    fun bindHasFocus(view: View, focused: ObservableProperty<Boolean>) {
        if (focused.get()!!) {
            view.requestFocus()
        } else {
            view.clearFocus()
        }

        view.setOnFocusChangeListener { v, hasFocus -> focused.set(hasFocus) }
    }

    @JvmStatic
    @BindingAdapter("hasFocusNoKeyboard")
    fun bindHasFocusNoKeyboard(view: EditText, focused: ObservableProperty<Boolean>) {
        view.setOnFocusChangeListener { v, hasFocus -> focused.set(hasFocus) }
    }

    @JvmStatic
    @BindingAdapter("requestFocus")
    fun bindHasFocus(view: View, unused: Boolean) {
        view.requestFocus()
    }

    @JvmStatic
    @BindingAdapter("hideKeyboard")
    fun bindHideKeyboard(view: View, unused: Boolean) {
        val imm = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @JvmStatic
    @BindingAdapter("layout_marginTop")
    fun setLayoutMarginTop(view: View, margin: Float) {
        val lp = view.layoutParams as ViewGroup.MarginLayoutParams
        if (lp != null) {
            lp.setMargins(lp.leftMargin, margin.toInt(), lp.rightMargin, lp.bottomMargin)
            view.layoutParams = lp
        }
    }

    @JvmStatic
    @BindingAdapter("layout_marginBottom")
    fun setLayoutMarginBottom(view: View, margin: Float) {
        val lp = view.layoutParams as ViewGroup.MarginLayoutParams
        if (lp != null) {
            lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin, margin.toInt())
            view.layoutParams = lp
        }
    }

    @JvmStatic
    // android: prefix is necessary for layout_height and layout_width to work
    @BindingAdapter("android:layout_height")
    fun setLayoutHeight(view: View, height: Float) {
        val layoutParams = view.layoutParams
        layoutParams.height = height.toInt()
        view.layoutParams = layoutParams
    }

    @JvmStatic
    @BindingAdapter("android:layout_width")
    fun setLayoutWidth(view: View, width: Float) {
        val layoutParams = view.layoutParams
        layoutParams.width = width.toInt()
        view.layoutParams = layoutParams
    }
}
