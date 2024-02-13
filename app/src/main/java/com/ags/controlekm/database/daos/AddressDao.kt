package com.ags.controlekm.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ags.controlekm.models.database.Address
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("SELECT * FROM address")
    fun getAllAddress(): Flow<List<Address>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (address: Address)

    @Update
    suspend fun update (address: Address)

    @Delete
    suspend fun delete (address: Address)
}