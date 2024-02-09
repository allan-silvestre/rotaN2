package com.ags.controlekm.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ags.controlekm.models.Service
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Query("SELECT * FROM service")
    fun getAllServices(): Flow<List<Service>>

    @Query("SELECT * FROM service WHERE technicianId = :tecnicoId")
    fun getViagensCurrentUser(tecnicoId: String): Flow<List<Service>>

    @Query("SELECT * FROM service WHERE technicianId BETWEEN :firstDayWeek AND :lastDayWeek")
    fun getCurrentWeekData(firstDayWeek: Long, lastDayWeek: Long): Flow<List<Service>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(service: Service)

    @Update
    suspend fun update(service: Service)

    @Query("DELETE FROM service")
    fun deleteAll()

    @Delete
    suspend fun delete(service: Service)
}