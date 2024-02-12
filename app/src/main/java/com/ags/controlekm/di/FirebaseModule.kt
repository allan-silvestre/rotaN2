package com.ags.controlekm.di

import com.ags.controlekm.database.firebaseRepositories.FirebaseAddressRepository
import com.ags.controlekm.database.firebaseRepositories.FirebaseCurrentUserRepository
import com.ags.controlekm.database.firebaseRepositories.FirebaseServiceRepository
import com.ags.controlekm.database.firebaseRepositories.FirebaseUserRepository
import com.google.firebase.auth.FirebaseAuth
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
        return FirebaseDatabase.getInstance().reference.child("rotaN2")
    }
    @Singleton
    @Provides
    fun firebaseServiceRepository(databaseReference: DatabaseReference): FirebaseServiceRepository {
        return FirebaseServiceRepository(databaseReference.child("services"))
    }
    @Singleton
    @Provides
    fun firebaseCurrentUserRepository(databaseReference: DatabaseReference): FirebaseCurrentUserRepository {
        return FirebaseCurrentUserRepository(
            FirebaseAuth.getInstance().currentUser?.uid.toString(),
            databaseReference.child("users")
        )
    }

    @Singleton
    @Provides
    fun firebaseUserRepository(databaseReference: DatabaseReference): FirebaseUserRepository {
        return FirebaseUserRepository(databaseReference.child("users"))
    }
    @Singleton
    @Provides
    fun firebaseAddressRepository(databaseReference: DatabaseReference): FirebaseAddressRepository {
        return FirebaseAddressRepository(databaseReference.child("address"))
    }

}