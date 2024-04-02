/*
 * Copyright (c)  2024  Juan Nascimento
 * Part of FileManagerSphere - SafeClickListener.kt
 * SPDX-License-Identifier: GPL-3.0-or-later
 * More details at: https://www.gnu.org/licenses/
 */

package com.manager.filemanager.interfaces.manager

import android.view.View

class SafeClickListener(
    private var defaultInterval: Int = 1000,
    private val onSafeCLick: (View) -> Unit,
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0

    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTimeClicked >= defaultInterval) {
            lastTimeClicked = currentTime
            onSafeCLick(v)
        }
    }
}