package com.ags.controlekm.database.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Address(
    @PrimaryKey
    @NonNull
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var state: String = "",
    var city: String = "",
    var district: String = "",
    var streetName: String = "",
    var number: Int = 0,
) {
    fun toStringEnderecoAtendimento(): String {
        return "$name"
    }
}

