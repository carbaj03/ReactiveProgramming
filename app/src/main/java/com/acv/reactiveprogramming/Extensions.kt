package com.acv.reactiveprogramming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.*


infix fun ViewGroup.inflate(res: Int) =
        LayoutInflater.from(context).inflate(res, this, false)

fun Bundle.bundleToMap(): Map<String, Any> {
    val results = HashMap<String, Any>(size())
    val keys = keySet()
    for (key in keys) {
        results.put(key, get(key))
    }
    return results
}
