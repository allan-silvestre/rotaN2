package com.ags.controlekm.database.ViewModels

import android.app.Application
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class executarFuncaoViewModel(application: Application) : AndroidViewModel(application) {

    private val _carregando = mutableStateOf(false)
    val carregando get() = _carregando

    fun executarFuncao(function: () -> Unit, onExecuted: (String) -> Unit, onError: () -> Unit) {
        // Verifique se já está carregando
        if (!carregando.value) {
            // Iniciar o carregamento
            _carregando.value = true

            viewModelScope.launch {
                try {
                    // Simular uma função assíncrona real
                    val resultado = withContext(Dispatchers.IO) {
                        delay(1000)
                        function()
                        "Função executada com sucesso!"
                    }

                    // Chamar a função de retorno de sucesso
                    onExecuted(resultado)
                } catch (e: Exception) {
                    // Lidar com erros, se necessário
                    onError()
                } finally {
                    // Finalizar o carregamento, mesmo em caso de erro
                    _carregando.value = false
                }
            }
        }
    }
}