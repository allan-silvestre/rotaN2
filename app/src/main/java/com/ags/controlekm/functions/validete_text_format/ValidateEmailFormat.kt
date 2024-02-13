package com.ags.controlekm.functions.validete_text_format

import android.util.Patterns

fun validateEmailFormat(text: String): Boolean {
    return text != null && Patterns.EMAIL_ADDRESS.matcher(text).matches()
}