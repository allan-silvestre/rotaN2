package com.ags.controlekm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.firebaseRepositories.FirebaseCurrentUserRepository
import com.ags.controlekm.database.models.database.CurrentUser
import com.ags.controlekm.database.repositorys.CurrentUserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentUserViewModel @Inject constructor(
    private val firebaseCurrentUserRepository: FirebaseCurrentUserRepository,
    private val currentUserRepository: CurrentUserRepository
) : ViewModel() {
    val currentUser = flow<CurrentUser> {
        while (true) {
            val _currentUser = MutableStateFlow(CurrentUser())
            currentUserRepository.getCurrentUser()!!.firstOrNull()?.let {
                _currentUser.value = it
            }
            emit(_currentUser.value)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        CurrentUser()
    )

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            viewModelScope.launch(Dispatchers.IO) {
                when {
                    it.currentUser != null -> firebaseCurrentUserRepository.getCurrentUser().collect{
                        insert(it)
                        firebaseCurrentUserRepository.updateEmailVerification(
                            FirebaseAuth.getInstance().currentUser!!.isEmailVerified
                        )
                    }

                    it.currentUser == null -> deleteCurrentUser()

                }
            }
        }
    }

    fun insert(currentUser: CurrentUser) {
        viewModelScope.launch {
            currentUserRepository.insert(currentUser)
        }
    }

    fun update(currentUser: CurrentUser) {
        viewModelScope.launch {
            currentUserRepository.update(currentUser)
        }
    }

    fun deleteCurrentUser() {
        viewModelScope.launch {
            currentUserRepository.deleteCurrentUser()
        }
    }
}