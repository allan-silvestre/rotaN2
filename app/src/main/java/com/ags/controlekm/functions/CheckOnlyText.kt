package com.ags.controlekm.functions

//VERIFICAÇÃO SIMPLES APENAS TEXTO
fun checkOnlyText(text: String): Boolean {
    if (text.equals("Selecionar") || text.equals("")) {
        return false
    }
    return text.matches(Regex("[\\p{L}-]+"))
    return true
}