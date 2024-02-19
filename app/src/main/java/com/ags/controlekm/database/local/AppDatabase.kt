package com.ags.controlekm.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ags.controlekm.database.local.daos.AddressDao
import com.ags.controlekm.database.local.daos.CompanyDao
import com.ags.controlekm.database.local.daos.CurrentUserDao
import com.ags.controlekm.database.local.daos.ServiceDao
import com.ags.controlekm.database.local.daos.UserDao
import com.ags.controlekm.database.models.CurrentUser
import com.ags.controlekm.database.models.Company
import com.ags.controlekm.database.models.Address
import com.ags.controlekm.database.models.User
import com.ags.controlekm.database.models.Service

@Database(
    entities = [
        CurrentUser::class,
        User::class,
        Company::class,
        Address::class,
        Service::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currentUserDao(): CurrentUserDao
    abstract fun userDao(): UserDao
    abstract fun companyDao(): CompanyDao
    abstract fun addressDao(): AddressDao
    abstract fun serviceDao(): ServiceDao

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