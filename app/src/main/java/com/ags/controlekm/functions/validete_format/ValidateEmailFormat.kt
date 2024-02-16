package com.ags.controlekm.functions.validete_format

import android.util.Patterns

fun validateEmailFormat(text: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(text).matches()
}