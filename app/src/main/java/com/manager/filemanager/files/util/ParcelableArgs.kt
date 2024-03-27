package com.manager.filemanager.files.util

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import kotlin.reflect.KClass


interface ParcelableArgs : Parcelable

fun <Args : ParcelableArgs> Intent.putArgs(args: Args, argsClass: KClass<Args>): Intent =
    putExtra(argsClass.java.name, args)

inline fun <reified Args : ParcelableArgs> Intent.putArgs(args: Args) = putArgs(args, Args::class)


fun <Args : ParcelableArgs> Bundle.getArgsOrNull(argsClass: KClass<Args>): Args? =
    getParcelable(argsClass.java.name)

inline fun <reified Args : ParcelableArgs> Bundle.getArgsOrNull() = getArgsOrNull(Args::class)


@Parcelize
class Args(val savedInstanceState: @WriteWith<BundleParceler> Bundle?) : ParcelableArgs