package com.ags.controlekm.database.repositorys

import com.ags.controlekm.database.daos.UserDao
import com.ags.controlekm.database.models.database.User
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