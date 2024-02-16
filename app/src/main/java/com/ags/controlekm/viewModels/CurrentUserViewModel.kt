package com.ags.controlekm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.firebaseRepositories.FirebaseCurrentUserRepository
import com.ags.controlekm.database.models.database.CurrentUser
import com.ags.controlekm.database.models.database.Service
import com.ags.controlekm.database.repositorys.CurrentUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentUserViewModel @Inject constructor(
    private val firebaseRepository: FirebaseCurrentUserRepository,
    private val repository: CurrentUserRepository
): ViewModel() {

    val currentUser = flow<CurrentUser> {
        while (true) {
            val _currentUser = MutableStateFlow(CurrentUser())
            repository.getCurrentUser()!!.firstOrNull()?.let {
                _currentUser.value = it
            }
            emit(_currentUser.value)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        CurrentUser()
    )

    fun insert(currentUser: CurrentUser) {
        viewModelScope.launch {
            repository.insert(currentUser)
        }
    }

    fun update(currentUser: CurrentUser) {
        viewModelScope.launch {
            repository.update(currentUser)
        }
    }

    fun deleteCurrentUser() {
        viewModelScope.launch {
            repository.deleteCurrentUser()
        }
    }
}