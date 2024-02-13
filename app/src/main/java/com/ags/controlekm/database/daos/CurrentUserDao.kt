package com.ags.controlekm.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ags.controlekm.models.database.CurrentUser
import kotlinx.coroutines.flow.Flow

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