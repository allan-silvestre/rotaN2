package com.ags.controlekm.database.local.repositories

import com.ags.controlekm.database.local.daos.UserDao
import com.ags.controlekm.database.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    suspend fun insert(user: User) {
        userDao.insert(user)
    }
    suspend fun update(user: User) {
        userDao.update(user)
    }
    suspend fun delete(user: User) {
        userDao.delete(user)
    }
}