package com.ags.controlekm

import android.content.Context
import com.ags.controlekm.database.AppDatabase
import com.ags.controlekm.database.daos.AddressDao
import com.ags.controlekm.database.daos.AddressDao_Impl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideAddressDao(appDatabase: AppDatabase): AddressDao {
        return appDatabase.addressDao()
    }

}