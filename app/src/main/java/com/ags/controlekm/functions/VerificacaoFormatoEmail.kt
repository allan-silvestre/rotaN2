package com.ags.controlekm.functions

import android.util.Patterns

//VERIFICAÇÃO EMAIL
fun VerificacaoFormatoEmail(text: String): Boolean {
    return text != null && Patterns.EMAIL_ADDRESS.matcher(text).matches()
}