package com.ags.controlekm.database.local.repositories

import com.ags.controlekm.database.local.daos.CurrentUserDao
import com.ags.controlekm.database.models.CurrentUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentUserRepository @Inject constructor(
    private val currentUserDao: CurrentUserDao
) {
    fun getCurrentUser(): Flow<CurrentUser>?{
        return currentUserDao.getCurrentUser()
    }
    suspend fun insert(currentUser: CurrentUser) {
        currentUserDao.insert(currentUser)
    }
    suspend fun update(currentUser: CurrentUser) {
        currentUserDao.update(currentUser)
    }
    suspend fun deleteCurrentUser(){
        withContext(Dispatchers.IO) {
            currentUserDao.deleteCurrentUser()
        }
    }
}