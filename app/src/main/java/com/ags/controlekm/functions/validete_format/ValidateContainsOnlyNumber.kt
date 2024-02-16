package com.ags.controlekm.functions.validete_format

//VERIFICAÇÃO SIMPLES APENAS NÚMERO
fun validateContainsOnlyNumbers(text: String): Boolean {
    return text.matches(Regex("[0-9]+"))
}