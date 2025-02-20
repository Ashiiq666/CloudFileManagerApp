package com.manager.filemanager.interfaces.settings.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.manager.filemanager.interfaces.manager.SafeClickListener


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}


fun View.setMargins(margin: Int) {
    val layoutParams = this.layoutParams as ConstraintLayout.LayoutParams
    layoutParams.setMargins(margin, margin, margin, margin)
    this.layoutParams = layoutParams
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.click(onClick: (View) -> Unit) {
    val safeClickListener =
        SafeClickListener {
            onClick(it)
        }
    setOnClickListener(safeClickListener)
}
