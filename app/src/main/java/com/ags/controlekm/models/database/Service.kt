package com.ags.controlekm.models.database

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Service(
    @PrimaryKey
    @NonNull
    var id: String = UUID.randomUUID().toString(),
    var departureDate: String? = "",
    var dateArrival: String? = "",
    var dateCompletion: String? = "",
    var departureDateToReturn: String? = "",
    var dateArrivalReturn: String? = "",
    var departureTime: String? = "",
    var timeArrival: String? = "",
    var CompletionTime: String? = "",
    var startTimeReturn: String? = "",
    var timeCompletedReturn: String? = "",
    var departureAddress: String? = "",
    var serviceAddress: String? = "",
    var addressReturn: String? = "",
    var departureKm: String? = "",
    var arrivalKm: String? = "",
    var kmDriven: String? = "",
    var profileImgTechnician: String? = "",
    var technicianId: String? = "",
    var technicianName: String? = "",
    var statusService: String? = "",
    var description: String? = "",
)