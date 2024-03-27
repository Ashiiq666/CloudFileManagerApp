
package com.manager.filemanager.manager.files.ui


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.manager.filemanager.R
import com.manager.filemanager.files.util.asColor
import com.manager.filemanager.files.util.getColorByAttr
import com.manager.filemanager.files.util.shortAnimTime
import com.manager.filemanager.files.util.withModulatedAlpha
import com.manager.filemanager.settings.preference.Preferences


object CheckableItemBackground {

    @SuppressLint("RestrictedApi")
    fun create(context: Context): Drawable{
        val typedValue = TypedValue()
        val resolved = context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSecondary, typedValue, true)
        val colorOnSecondary = if (resolved) typedValue.data else 0


        return AnimatedStateListDrawableCompat().apply {
            val shortAnimTime = context.shortAnimTime
            setEnterFadeDuration(shortAnimTime)
            setExitFadeDuration(shortAnimTime)
            val opacity = Preferences.Interface.selectedFileBackgroundOpacity
            val transparentColor = ColorDrawable(Color.TRANSPARENT)
            val primaryColor = context.getColorByAttr( com.google.android.material.R.attr.colorPrimaryContainer)
            val checkedColor = primaryColor.asColor().withModulatedAlpha(opacity).value
            val normalColor = colorOnSecondary
            val mNormalColor = ColorDrawable(normalColor)

            val backgroundSelected = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.card_radius_normal)!!)
            DrawableCompat.setTint(backgroundSelected, checkedColor)

            val background = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.card_radius_normal)!!)
            DrawableCompat.setTint(background, normalColor)
            if (Preferences.Interface.isEnabledRoundedCorners) {
                addState(intArrayOf(android.R.attr.state_checked), backgroundSelected)
                addState(intArrayOf(), if (Preferences.Interface.isEnabledTransparentListBackground) transparentColor else background)
            } else{
                addState(intArrayOf(android.R.attr.state_checked), ColorDrawable(checkedColor))
                addState(intArrayOf(), if (Preferences.Interface.isEnabledTransparentListBackground) transparentColor else mNormalColor)
            }
        }
}}