package com.kroegerama.kaiteki.recyclerview.example

import android.app.Activity
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import  androidx.core.util.Pair as UtilPair

class ActivityOptionsBuilder {
    internal val pairs = ArrayList<UtilPair<View, String?>>()
    fun add(view: View, transitionName: String = ViewCompat.getTransitionName(view)!!) {
        pairs.add(UtilPair(view, transitionName))
    }
}

fun activityOptions(activity: Activity, block: ActivityOptionsBuilder.() -> Unit): ActivityOptionsCompat {
    val builder = ActivityOptionsBuilder()
    builder.apply(block)
    return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *builder.pairs.toTypedArray())
}
