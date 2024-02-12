package com.ags.controlekm.di

import com.ags.controlekm.database.firebaseRepositories.FirebaseAddressRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {
    @Singleton
    @Provides
    fun databaseReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child("rotaN2").child("address")
    }
    @Singleton
    @Provides
    fun firebaseAddressRepository(databaseReference: DatabaseReference): FirebaseAddressRepository {
        return FirebaseAddressRepository(databaseReference)
    }

}