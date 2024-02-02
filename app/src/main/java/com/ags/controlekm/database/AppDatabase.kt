package com.ags.controlekm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ags.controlekm.database.Daos.CurrentUserDao
import com.ags.controlekm.database.Daos.EmpresaClienteDao
import com.ags.controlekm.database.Daos.EnderecoAtendimentoDao
import com.ags.controlekm.database.Daos.UserDao
import com.ags.controlekm.database.Daos.ViagemSuporteTecnicoDao
import com.ags.controlekm.database.Models.CurrentUser
import com.ags.controlekm.database.Models.EmpresaCliente
import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.ags.controlekm.database.Models.User
import com.ags.controlekm.database.Models.ViagemSuporteTecnico

@Database(
    entities = [
        CurrentUser::class,
        User::class,
        EmpresaCliente::class,
        EnderecoAtendimento::class,
        ViagemSuporteTecnico::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currentUserDao(): CurrentUserDao
    abstract fun userDao(): UserDao
    abstract fun empresaClienteDao(): EmpresaClienteDao
    abstract fun enderecoAtendimentoDao(): EnderecoAtendimentoDao
    abstract fun viagemSuporteTecnicoDao(): ViagemSuporteTecnicoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}