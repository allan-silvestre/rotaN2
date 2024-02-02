package com.ags.controlekm.functions

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

fun registerUser(email: String, password: String): Boolean {
    //FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser
    return try {
        currentUser?.sendEmailVerification()
            ?.addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                } else {
                }
            }
        true
    } catch (e: FirebaseAuthException) {
        // Handle exceptions, como usuário já existente, senha fraca, etc.
        false
    }
}

/****** VERICAR EMAIL
currentUser?.sendEmailVerification()
?.addOnCompleteListener { task ->
if (task.isSuccessful) {
// Email de verificação enviado com sucesso
} else {
// Tratamento de erro ao enviar o email de verificação
}
}
 */