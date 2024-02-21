package com.ags.controlekm.ui.views.serviceManager.viewModel.modelsParams

data class FinishCurrentServiceAndGenerateNewServiceParams(
    val departureAddress: String,
    val serviceAddress: String,
    val departureKm: Int,
    val date: String,
    val time: String,
    val serviceSummary: String,
)
