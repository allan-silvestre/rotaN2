package com.ags.controlekm.viewModels.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.viewModels.login.modelsParams.LoginParams
import com.ags.controlekm.viewModels.login.validateFields.ValidateFieldsLogin
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateFieldsLogin: ValidateFieldsLogin
) : ViewModel() {
    private var _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private var _showLoginview = MutableStateFlow(false)
    val showLoginview = _showLoginview.asStateFlow()

    private var _loginResult = MutableStateFlow(false)
    val loginResult = _loginResult.asStateFlow()

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            _loginResult.value = it.currentUser != null
            _showLoginview.value = it.currentUser == null
        }
    }

    fun login(params: LoginParams) {
        if (validateFieldsLogin.validateEmailAndPassword(params.email, params.password)) {
            viewModelScope.launch {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(params.email, params.password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _loading.value = true
                            _loginResult.value = task.result.user != null
                            _showLoginview.value = task.result.user == null
                            println("ID: ${task.result.user!!.uid} esta logado")
                        } else {
                            println("Email ou senha incorretos")
                        }
                    }
                delay(2000L)
                _loading.value = false
            }
        }
    }
}