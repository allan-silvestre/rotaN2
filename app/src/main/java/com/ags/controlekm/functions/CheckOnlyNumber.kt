package com.ags.controlekm.functions

//VERIFICAÇÃO SIMPLES APENAS NÚMERO
fun checkOnlyNumbers(text: String): Boolean {
    return text.matches(Regex("[0-9]+"))
    return true
}