package com.ags.controlekm.database.Daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ags.controlekm.database.Models.CurrentUser
import com.ags.controlekm.database.Models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Dao
interface CurrentUserDao {
    @Query("SELECT * FROM currentuser")
    fun getCurrentUser(): Flow<CurrentUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentuser: CurrentUser)

    @Update
    suspend fun update(currentuser: CurrentUser)

    @Delete
    suspend fun delete(currentuser: CurrentUser)
}