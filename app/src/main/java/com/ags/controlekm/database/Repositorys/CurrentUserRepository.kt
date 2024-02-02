package com.ags.controlekm.database.Repositorys

import com.ags.controlekm.database.Daos.CurrentUserDao
import com.ags.controlekm.database.Daos.UserDao
import com.ags.controlekm.database.Models.CurrentUser
import com.ags.controlekm.database.Models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CurrentUserRepository(private val currentUserDao: CurrentUserDao) {
    val currentUser: Flow<CurrentUser> = currentUserDao.getCurrentUser()

    suspend fun insert(currentUser: CurrentUser) {
        currentUserDao.insert(currentUser)
    }

    suspend fun update(currentUser: CurrentUser) {
        currentUserDao.update(currentUser)
    }

    suspend fun delete(currentUser: CurrentUser) {
        currentUserDao.delete(currentUser)
    }
}