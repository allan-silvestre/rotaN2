package com.ags.controlekm.functions

import android.util.Patterns

fun checkEmailFormat(text: String): Boolean {
    return text != null && Patterns.EMAIL_ADDRESS.matcher(text).matches()
}