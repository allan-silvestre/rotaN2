package com.ags.controlekm.ui.views.userManager.ViewModel

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.remote.repositories.FirebaseUserRepository
import com.ags.controlekm.database.local.repositories.UserRepository
import com.ags.controlekm.database.models.User
import com.google.firebase.storage.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val firebaseRepository: FirebaseUserRepository
) : ViewModel() {

    val allUsers: Flow<List<User>> = repository.allUsers

    init {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.getAllUser().collect{ userList ->
                userList.forEach { user ->
                    repository.insert(user)
                }
            }
        }
    }

    fun uploadImage(
        bitmap: Bitmap,
        context: ComponentActivity,
        imgName: String,
        callback: (Boolean) -> Unit,
        onImageUrlLoaded: (String) -> Unit,
    ) {
        val storageRef = com.google.firebase.Firebase.storage.reference
        val imageRef = storageRef.child("images/$imgName")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        imageRef.putBytes(imageData).addOnSuccessListener {
            callback(true)
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Execute a ação onImageUrlLoaded com o link da imagem
                onImageUrlLoaded(uri.toString())
            }

        }.addOnFailureListener {exception ->
            callback(false)
        }
    }

    fun insert(user: User) {
        viewModelScope.launch {
            repository.insert(user)
        }
    }

    fun update(user: User) {
        viewModelScope.launch {
            repository.update(user)
        }
    }

    fun delete(user: User) {
        viewModelScope.launch {
            repository.delete(user)
        }
    }
}