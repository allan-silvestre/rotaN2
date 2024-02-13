package com.ags.controlekm.functions.validete_fields

import android.util.Patterns

fun validateEmailFormat(text: String): Boolean {
    return text != null && Patterns.EMAIL_ADDRESS.matcher(text).matches()
}