package com.ags.controlekm.database.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ags.controlekm.database.models.CurrentUser
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentUserDao {
    @Query("SELECT * FROM currentuser")
    fun getCurrentUser(): Flow<CurrentUser>?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentuser: CurrentUser)
    @Update
    suspend fun update(currentuser: CurrentUser)
    @Query("DELETE FROM currentuser")
    suspend fun deleteCurrentUser()
}