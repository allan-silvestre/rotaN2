package com.ags.controlekm.database.Daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import kotlinx.coroutines.flow.Flow

@Dao
interface ViagemSuporteTecnicoDao {
    @Query("SELECT * FROM viagemsuportetecnico")
    fun getAllServices(): Flow<List<ViagemSuporteTecnico>>

    @Query("SELECT * FROM viagemsuportetecnico WHERE tecnicoId = :tecnicoId")
    fun getViagensCurrentUser(tecnicoId: String): Flow<List<ViagemSuporteTecnico>>

    @Query("SELECT * FROM viagemsuportetecnico WHERE tecnicoId BETWEEN :firstDayWeek AND :lastDayWeek")
    fun getCurrentWeekData(firstDayWeek: Long, lastDayWeek: Long): Flow<List<ViagemSuporteTecnico>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(viagemSuporteTecnico: ViagemSuporteTecnico)

    @Update
    suspend fun update(viagemSuporteTecnico: ViagemSuporteTecnico)

    @Query("DELETE FROM viagemsuportetecnico")
    fun deleteAll()

    @Delete
    suspend fun delete(viagemSuporteTecnico: ViagemSuporteTecnico)
}