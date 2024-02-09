package com.ags.controlekm.database.Daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import kotlinx.coroutines.flow.Flow

@Dao
interface EnderecoAtendimentoDao {
    @Query("SELECT * FROM enderecoatendimento")
    fun getAllAddress(): Flow<List<EnderecoAtendimento>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (enderecoAtendimento: EnderecoAtendimento)

    @Transaction
    @Update
    suspend fun update (enderecoAtendimento: EnderecoAtendimento)

    @Delete
    suspend fun delete (enderecoAtendimento: EnderecoAtendimento)
}