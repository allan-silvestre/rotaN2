package com.ags.controlekm.functions

//VERIFICAÇÃO SENHA
fun checkPasswordFormat(text: String): Boolean {
    return text.matches(Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^*()_&+=!])(?!.*\\s).+$"))
    return true
}