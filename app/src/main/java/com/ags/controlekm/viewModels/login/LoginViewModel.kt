package com.ags.controlekm.viewModels.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.ags.controlekm.database.firebaseRepositories.FirebaseCurrentUserRepository
import com.ags.controlekm.database.models.database.CurrentUser
import com.ags.controlekm.database.repositorys.CurrentUserRepository
import com.ags.controlekm.navigation.navigateSingleTopTo
import com.ags.controlekm.viewModels.CurrentUserViewModel
import com.ags.controlekm.viewModels.login.modelsParamsFunctions.LoginParams
import com.ags.controlekm.viewModels.login.validateFields.ValidateFieldsLogin
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateFieldsLogin: ValidateFieldsLogin,
    private val repository: CurrentUserRepository,
    private val firebaseRepository: FirebaseCurrentUserRepository
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
        _loading.value = true
        if (validateFieldsLogin.validateEmailAndPassword(params.email, params.password)) {
            viewModelScope.launch {
                try {
                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(params.email, params.password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                viewModelScope.launch {
                                    launch {
                                        firebaseRepository.getCurrentUser().collect {
                                            if (it != null) {
                                                repository.insert(it)
                                                firebaseRepository.updateEmailVerification(
                                                    FirebaseAuth.getInstance().currentUser?.isEmailVerified
                                                        ?: false
                                                )
                                            }
                                        }
                                    }
                                }
                                println("logado com sucesso")
                                _loginResult.value = true

                            } else {
                                println("dados invalidos")
                                _loginResult.value = false
                            }
                        }
                    delay(2000L)
                    _loading.value = false
                } catch (e: FirebaseException) {
                    println(e)
                }
            }
        }
    }
}