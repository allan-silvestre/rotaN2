package com.ags.controlekm.functions

//VERIFICAÇÃO DO CPF
fun checkCpfFormat(digitos: String): Boolean {
    var validadorA = 0
    var validadorB = 0
    var valorPadraoDivisao = 11

    if (digitos.length == 11) {
        validadorA =
            (
                    (digitos.substring(0, 1).toInt() * 10) +
                            (digitos.substring(1, 2).toInt() * 9) +
                            (digitos.substring(2, 3).toInt() * 8) +
                            (digitos.substring(3, 4).toInt() * 7) +
                            (digitos.substring(4, 5).toInt() * 6) +
                            (digitos.substring(5, 6).toInt() * 5) +
                            (digitos.substring(6, 7).toInt() * 4) +
                            (digitos.substring(7, 8).toInt() * 3) +
                            (digitos.substring(8, 9).toInt() * 2)
                    ) % valorPadraoDivisao
        validadorB =
            (
                    (digitos.substring(0, 1).toInt() * 11) +
                            (digitos.substring(1, 2).toInt() * 10) +
                            (digitos.substring(2, 3).toInt() * 9) +
                            (digitos.substring(3, 4).toInt() * 8) +
                            (digitos.substring(4, 5).toInt() * 7) +
                            (digitos.substring(5, 6).toInt() * 6) +
                            (digitos.substring(6, 7).toInt() * 5) +
                            (digitos.substring(7, 8).toInt() * 4) +
                            (digitos.substring(8, 9).toInt() * 3) +
                            (digitos.substring(9, 10).toInt() * 2)
                    ) % valorPadraoDivisao

        if (validadorA.equals(0) || validadorA.equals(1)) {
            validadorA = 0
        } else {
            validadorA = valorPadraoDivisao - validadorA
        }

        if (validadorB.equals(0) || validadorB.equals(1)) {
            validadorB = 0
        } else {
            validadorB = valorPadraoDivisao - validadorB
        }

        return !(validadorA != digitos.substring(9, 10).toInt() ||
                validadorB != digitos.substring(10, 11).toInt())
    } else if (digitos.equals("") || digitos.length < 11) {
        return false
    }

    return true
}

