package com.ags.controlekm.models.params

import com.ags.controlekm.models.database.CurrentUser

data class newServiceParams(
    val departureAddress: String,
    val serviceAddress: String,
    val departureKm: Int,
    val date: String,
    val time: String
)
