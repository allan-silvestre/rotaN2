package com.ags.controlekm.functions.validete_text_format

//VERIFICAÇÃO SIMPLES APENAS NÚMERO
fun validateContainsOnlyNumbers(text: String): Boolean {
    return text.matches(Regex("[0-9]+"))
    return true
}