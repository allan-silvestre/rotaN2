package com.ags.controlekm.ui.views.login.viewModel.validateFields

import androidx.lifecycle.ViewModel
import com.ags.controlekm.functions.validete_format.validateEmailFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ValidateFieldsLogin @Inject constructor(): ViewModel() {
    fun validateEmailAndPassword(email: String, password: String): Boolean {
        return if (email.isEmpty() || password.isEmpty()) {
            println("Preencha todos os campos para continuar")
            false
        } else if ( !validateEmailFormat(email) ){
            println("O formato do e-mail não é válido")
            false
        } else { true }
    }
}