/*
 * Copyright (c)  2023  Juan Nascimento
 * Part of FileManagerSphere - ParcelExtensions.kt
 * SPDX-License-Identifier: GPL-3.0-or-later
 * More details at: https://www.gnu.org/licenses/
 */

package com.etb.filemanager.files.util

/*
 * Copyright (c) 2023 Hai Zhang <dreaming.in.code.zh@gmail.com>
 * All Rights Reserved.
 */


import android.os.Parcel
import android.os.Parcelable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import com.etb.filemanager.files.compat.readParcelableListCompat


fun <T : Parcelable?> Parcel.readParcelableListCompat(classLoader: ClassLoader?): List<T> =
    readParcelableListCompat(mutableListOf(), classLoader)

inline fun <reified E : Parcelable?, L : MutableList<E>> Parcel.readParcelableListCompat(
    list: L
): L = readParcelableListCompat(list, E::class.java.classLoader)

inline fun <reified T : Parcelable?> Parcel.readParcelableListCompat(): List<T> =
    readParcelableListCompat(mutableListOf())

@OptIn(ExperimentalContracts::class)
inline fun <R> Parcel.use(block: (Parcel) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return try {
        block(this)
    } finally {
        recycle()
    }
}