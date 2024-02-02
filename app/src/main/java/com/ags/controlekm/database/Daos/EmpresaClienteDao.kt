package com.ags.controlekm.database.Daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ags.controlekm.database.Models.EmpresaCliente
import kotlinx.coroutines.flow.Flow

@Dao
interface EmpresaClienteDao {
    @Query("SELECT * FROM empresaCliente")
    fun getAllEmpresaCliente(): Flow<List<EmpresaCliente>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (empresaCliente: EmpresaCliente)

    @Update
    suspend fun update (empresaCliente: EmpresaCliente)

    @Delete
    suspend fun delete (empresaCliente: EmpresaCliente)
}