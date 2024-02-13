package com.ags.controlekm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.firebaseRepositories.FirebaseUserRepository
import com.ags.controlekm.database.repositorys.UserRepository
import com.ags.controlekm.models.database.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val firebaseRepository: FirebaseUserRepository
) : ViewModel() {

    val allUsers: Flow<List<User>> = repository.allUsers

    init {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.getAllUser().collect{ userList ->
                userList.forEach { user ->
                    repository.insert(user)
                }
            }
        }
    }

    fun insert(user: User) {
        viewModelScope.launch {
            repository.insert(user)
        }
    }

    fun update(user: User) {
        viewModelScope.launch {
            repository.update(user)
        }
    }

    fun delete(user: User) {
        viewModelScope.launch {
            repository.delete(user)
        }
    }
}