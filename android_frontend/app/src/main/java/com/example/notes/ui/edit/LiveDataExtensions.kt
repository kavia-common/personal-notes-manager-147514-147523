package com.example.notes.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Utility extension to expose StateFlow-like observe in older patterns if needed.
 * Not used directly but kept for potential extension.
 */
fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> = this

inline fun <T> LiveData<T>.observe(owner: androidx.lifecycle.LifecycleOwner, crossinline onChanged: (T) -> Unit) {
    observe(owner, Observer { onChanged(it) })
}
