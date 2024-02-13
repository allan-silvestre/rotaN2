package com.ags.controlekm.functions.firebase

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream

// FUNÇÃO DE UPLOAD DE IMAGEM PARA O STORAGE
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