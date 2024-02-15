package com.ags.controlekm.viewModels

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerformFunctionViewModel(application: Application) : AndroidViewModel(application) {
    var context: Application = getApplication()

    private val _loading = mutableStateOf(false)
    val loading get() = _loading

    fun performFunctionTry(function: () -> Unit, onError: () -> Unit) {
        _loading.value = true
        viewModelScope.launch {
            try {
                function()
            } catch (e: Exception) {
                onError()
            } finally {
                _loading.value = false
            }
        }
    }



}