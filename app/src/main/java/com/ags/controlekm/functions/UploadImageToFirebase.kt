package com.ags.controlekm.functions

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream

// FUNÇÃO DE UPLOAD DE IMAGEM PARA O STORAGE
fun UploadImage(
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

/***
fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    val scaleWidth = maxWidth.toFloat() / width
    val scaleHeight = maxHeight.toFloat() / height

    val matrix = android.graphics.Matrix()
    matrix.postScale(scaleWidth, scaleHeight)

    return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
}

fun UploadImage(
    bitmap: Bitmap,
    context: ComponentActivity,
    imgName: String,
    callback: (Boolean) -> Unit,
    onImageUrlLoaded: (String) -> Unit,
    maxWidth: Int,
    maxHeight: Int,
) {
    // Redimensiona a imagem
    val resizedBitmap = resizeBitmap(bitmap, maxWidth, maxHeight)

    val storageRef = FirebaseStorage.getInstance().reference
    val imageRef = storageRef.child("images/$imgName")

    val baos = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val imageData = baos.toByteArray()

    imageRef.putBytes(imageData).addOnSuccessListener {
        callback(true)
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            // Execute a ação onImageUrlLoaded com o link da imagem
            onImageUrlLoaded(uri.toString())
        }

    }.addOnFailureListener { exception ->
        callback(false)
    }
}**/