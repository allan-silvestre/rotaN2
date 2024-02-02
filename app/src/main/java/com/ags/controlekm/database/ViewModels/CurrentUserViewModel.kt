package com.ags.controlekm.database.ViewModels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.AppDatabase
import com.ags.controlekm.database.FirebaseServices.CurrentUserServices
import com.ags.controlekm.database.Models.CurrentUser
import com.ags.controlekm.database.Repositorys.CurrentUserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class CurrentUserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CurrentUserRepository
    val currentUserData: Flow<CurrentUser>
    var currentUser = mutableStateOf(FirebaseAuth.getInstance().currentUser)

    init {
        val currentUserDao = AppDatabase.getDatabase(application).currentUserDao()
        this.repository = CurrentUserRepository(currentUserDao)
        currentUserData = repository.currentUser
        var currentUserServices = CurrentUserServices(currentUser.value?.uid.toString())

        viewModelScope.launch {
            FirebaseAuth.getInstance().addAuthStateListener { auth ->
                currentUser.value = auth.currentUser
                currentUser.value?.reload()
                getCurrentUserData(currentUser.value?.uid.toString()) { loadedUserData ->
                    insert(loadedUserData)
                }

                currentUserServices.emailIsVerifield(currentUser.value!!.isEmailVerified)

            }

        }

    }

    fun insert(currentUser: CurrentUser) {
        viewModelScope.launch {
            repository.insert(currentUser)
        }
    }

    fun update(currentUser: CurrentUser) {
        viewModelScope.launch {
            repository.update(currentUser)
        }
    }

    fun delete(currentUser: CurrentUser) {
        viewModelScope.launch {
            repository.delete(currentUser)
        }
    }

    fun getCurrentUserData(id: String, onDataChanged: (CurrentUser) -> Unit) {
        val userRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference
                .child("rotaN2")
                .child("users")
                .child(id)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData: CurrentUser = snapshot.getValue(CurrentUser::class.java)!!
                onDataChanged(userData)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors, se necessário
            }
        }

        // Adiciona o listener
        userRef.addValueEventListener(valueEventListener)

    }

}