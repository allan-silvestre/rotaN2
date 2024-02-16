package com.ags.controlekm.functions.validete_format

//VERIFICAÇÃO SIMPLES APENAS TEXTO
fun validateContainsOnlyText(text: String): Boolean {
    if (text.equals("Selecionar") || text.equals("")) {
        return false
    }
    return text.matches(Regex("[\\p{L}-]+"))
}