package com.ags.controlekm.functions.validete_text_format

//VERIFICAÇÃO SENHA
fun validatePasswordFormat(text: String): Boolean {
    return text.matches(Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^*()_&+=!])(?!.*\\s).+$"))
    return true
}