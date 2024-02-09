package com.ags.controlekm.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity()
data class User(
    @PrimaryKey
    @NonNull
    val id: String = UUID.randomUUID().toString(),
    var image: String? = "",
    var name: String? = "",
    var lastName: String?= "",
    var birth: String? = "",
    var gender: String? = "",
    var accessLevel: String? = "",
    var registrationNumber: String? = "",
    var phoneNumber: String? = "",
    var cpf: String? = "",
    var email: String? = "",
    var einYourEmployer: String? = "",
    var position: String? = "",
    var sector: String? = "",
    var kmBackup: String? = "0",
    var lastKm: String? = "0",
    var emailVerification: Boolean? = false
)