package com.ags.controlekm.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.AppDatabase
import com.ags.controlekm.database.repositorys.UserRepository
import com.ags.controlekm.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    val allUsers: Flow<List<User>>

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        this.repository = UserRepository(userDao)
        allUsers = repository.allUsers
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

// COMO CHAMAR O VIEWMODEL DENTRO DE UMA FUNÇÃO COMPOSE
// val users: List<User> by userViewModel.allUsers.collectAsState(emptyList())