package com.ags.controlekm.functions

//VERIFICAÇÃO SIMPLES APENAS NÚMERO
fun VerificacaoApenasNumero(text: String): Boolean {
    return text.matches(Regex("[0-9]+"))
    return true
}