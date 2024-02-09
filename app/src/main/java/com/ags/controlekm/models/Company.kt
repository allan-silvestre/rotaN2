package com.ags.controlekm.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Company(
    @PrimaryKey
    @NonNull
    val id: String = UUID.randomUUID().toString(),
    var image: String? = "",
    var companyName: String? = "",
    var accessLevel: String? = "",
    var phoneNumber: String? = "",
    var email: String? = "",
    var ein: String? = "",
    var emailVerification: Boolean? = false
)