package com.newsapp.util

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object CustomAnalytics {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun init(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    fun logEvent(key: String, bundle: Bundle) {
        firebaseAnalytics.logEvent(key, bundle)
    }
}