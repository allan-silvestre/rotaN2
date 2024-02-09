package com.ags.controlekm.viewModels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.AppDatabase
import com.ags.controlekm.models.CurrentUser
import com.ags.controlekm.database.repositorys.CurrentUserRepository
import com.ags.controlekm.functions.checkEmailFormat
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var authResult = MutableStateFlow(false)
    var isLoginEnabled = mutableStateOf(true)
    private val repository: CurrentUserRepository

    val currentUserData: Flow<CurrentUser>

    init {
        val currentUserDao = AppDatabase.getDatabase(application).currentUserDao()
        this.repository = CurrentUserRepository(currentUserDao)
        currentUserData = repository.currentUser
    }

    fun login(email: String, senha: String) {
        if (email.isEmpty() || !checkEmailFormat(email) || senha.isEmpty()) {
            authResult.value = false
        } else {
            viewModelScope.launch {
                if (isLoginEnabled.value) {
                    try {
                        // Autenticar com o Firebase
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                            .addOnCompleteListener { task ->
                                if (task.isComplete) {
                                    authResult.value = true
                                }
                            }
                    } catch (e: FirebaseException) {
                        // Verificar o tipo de exceção
                        when (e) {
                            is FirebaseAuthInvalidUserException -> {
                                // Usuário não existe, fazer algo
                                FirebaseAuth.getInstance().signOut()
                            }

                            is FirebaseAuthInvalidCredentialsException -> {
                                // Credenciais inválidas, fazer algo
                                FirebaseAuth.getInstance().signOut()
                            }

                            is FirebaseAuthException -> {
                                FirebaseAuth.getInstance().signOut()
                            }

                            else -> {
                                // Outro tipo de erro, fazer algo
                            }
                        }
                    }
                }
            }
        }
    }
}