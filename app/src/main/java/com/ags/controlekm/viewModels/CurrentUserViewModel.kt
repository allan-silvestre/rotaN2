package com.ags.controlekm.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.firebaseRepositories.CurrentUserServices
import com.ags.controlekm.models.CurrentUser
import com.ags.controlekm.database.repositorys.CurrentUserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentUserViewModel @Inject constructor(
    private val repository: CurrentUserRepository
): ViewModel() {
    var currentUserData: Flow<CurrentUser> = repository.currentUser
    var currentUser = mutableStateOf(FirebaseAuth.getInstance().currentUser)

    init {
        var currentUserServices = CurrentUserServices(currentUser.value?.uid.toString())

        viewModelScope.launch {
            FirebaseAuth.getInstance().addAuthStateListener { auth ->
                currentUser.value = auth.currentUser
                currentUser.value?.reload()
                getCurrentUserData(currentUser.value?.uid.toString()) { loadedUserData ->
                    insert(loadedUserData)
                }
            }
        }
            currentUserServices.emailIsVerifield(currentUser.value?.isEmailVerified ?: false)
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

    // FAZ DOWNLOAD DOS DADOS DO USUÁRIO ATUAL FIREBASE PARA O ROOM
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