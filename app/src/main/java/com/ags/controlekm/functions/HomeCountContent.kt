package com.ags.controlekm.functions

import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import com.google.firebase.auth.FirebaseUser

fun HomeCountContent(
    viagemSuporte: List<ViagemSuporteTecnico>,
    currentUser: FirebaseUser?,
    atendimento: (ViagemSuporteTecnico) -> Unit
): Int {
    if (viagemSuporte.isNotEmpty()) {
        viagemSuporte.forEach {
            if (it.tecnicoId!!.contains(currentUser?.uid.toString())) {
                if (it.statusService != "Finalizado") {
                    atendimento(it)
                    when (it.statusService) {
                        "Em rota", "Em rota, retornando" -> {
                            return 2
                        }
                        "Em andamento" -> {
                            return 3
                        }
                    }
                }
            }
        }
    }
    return 1
}