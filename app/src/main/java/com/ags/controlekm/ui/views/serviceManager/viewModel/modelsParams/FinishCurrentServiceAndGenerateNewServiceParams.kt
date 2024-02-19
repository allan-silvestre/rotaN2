package com.ags.controlekm.ui.views.serviceManager.viewModel.modelsParams

data class FinishCurrentServiceAndGenerateNewServiceParams(
    var departureAddress: String,
    var serviceAddress: String,
    var departureKm: Int,
    var date: String,
    var time: String,
    var serviceSummary: String,
)
