package com.ags.controlekm.database.ViewModels

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExecutarFuncaoViewModel(application: Application) : AndroidViewModel(application) {
    var context: Application = getApplication()

    private val _loading = mutableStateOf(false)
    val loading get() = _loading

    fun executar(function: () -> Unit, onExecuted: (String) -> Unit, onError: () -> Unit) {
        // Verifique se já está carregando
        if (!loading.value) {
            // Iniciar o carregamento
            _loading.value = true

            viewModelScope.launch {
                try {
                    // Simular uma função assíncrona real
                    val resultado = withContext(Dispatchers.IO) {
                        delay(1000)
                        function()
                        _loading.value = false
                        "isCompleted"
                    }

                    // Chamar a função de retorno de sucesso
                    onExecuted("isCompleted")
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Erro desconhecido, não foi possivél executar essa ação",
                        Toast.LENGTH_SHORT
                    ).show()
                    onError()
                } finally {
                    // Finalizar o carregamento, mesmo em caso de erro
                    _loading.value = false
                }
            }
        }
    }

    fun inicializar(function: () -> Unit, onExecuted: (Boolean) -> Unit, onError: () -> Unit) {
        // Verifique se já está carregando
        if (!loading.value) {
            // Iniciar o carregamento
            _loading.value = true

            viewModelScope.launch {
                try {
                    // Simular uma função assíncrona real
                    val resultado = withContext(Dispatchers.IO) {
                        delay(1000)
                        function()
                        _loading.value = false
                        "isCompleted"
                    }

                    // Chamar a função de retorno de sucesso
                    onExecuted(resultado.equals("isCompleted"))
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Erro desconhecido, não foi possivél executar essa ação",
                        Toast.LENGTH_SHORT
                    ).show()
                    onError()
                } finally {
                    // Finalizar o carregamento, mesmo em caso de erro
                    _loading.value = false
                }
            }
        }
    }

}