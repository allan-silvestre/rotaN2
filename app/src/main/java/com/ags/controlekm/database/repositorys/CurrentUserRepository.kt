package com.ags.controlekm.database.repositorys

import com.ags.controlekm.database.daos.CurrentUserDao
import com.ags.controlekm.models.CurrentUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentUserRepository @Inject constructor(
    private val currentUserDao: CurrentUserDao
) {
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