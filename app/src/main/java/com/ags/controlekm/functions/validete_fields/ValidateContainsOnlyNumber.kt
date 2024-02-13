package com.ags.controlekm.functions.validete_fields

//VERIFICAÇÃO SIMPLES APENAS NÚMERO
fun validateContainsOnlyNumbers(text: String): Boolean {
    return text.matches(Regex("[0-9]+"))
    return true
}