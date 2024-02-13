package com.ags.controlekm.models.database

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class CurrentUser(
    @PrimaryKey
    @NonNull
    val id: String = UUID.randomUUID().toString(),
    var image: String = "",
    var name: String = "",
    var lastName: String= "",
    var birth: String = "",
    var gender: String = "",
    var accessLevel: String = "",
    var registrationNumber: Int = 0,
    var phoneNumber: String = "",
    var cpf: String = "",
    var email: String = "",
    var einYourEmployer: String = "",
    var position: String = "",
    var sector: String = "",
    var kmBackup: Int = 0,
    var lastKm: Int = 0,
    var emailVerification: Boolean? = false
)
