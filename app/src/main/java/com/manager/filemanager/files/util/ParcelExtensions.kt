
package com.manager.filemanager.files.util



import android.os.Parcel
import android.os.Parcelable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import com.manager.filemanager.files.compat.readParcelableListCompat


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