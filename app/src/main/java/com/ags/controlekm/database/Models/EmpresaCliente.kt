package com.ags.controlekm.database.Models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class EmpresaCliente(
    @PrimaryKey
    @NonNull
    val id: String = UUID.randomUUID().toString(),
    var imagem: String? = "",
    var nomeFantasia: String? = "",
    var razaoSocial: String? = "",
    var nivelAcesso: String? = "",
    var telefone: String? = "",
    var email: String? = "",
    var cnpj: String? = "",
    var emailVerificado: Boolean? = false
)