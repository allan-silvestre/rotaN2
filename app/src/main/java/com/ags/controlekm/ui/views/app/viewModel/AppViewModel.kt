package com.ags.controlekm.ui.views.app.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.local.repositories.CurrentUserRepository
import com.ags.controlekm.database.models.CurrentUser
import com.ags.controlekm.database.models.Service
import com.ags.controlekm.database.remote.repositories.FirebaseCurrentUserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppViewModel @Inject constructor(
    private val firebaseCurrentUserRepository: FirebaseCurrentUserRepository,
    private val currentUserRepository: CurrentUserRepository,
    application: Application
): AndroidViewModel(application) {
    private var _showAppbarAndBottomBar = MutableStateFlow(false)
    val showAppbarAndBottomBar = _showAppbarAndBottomBar.asStateFlow()

    private var _isNetworkAvailable = MutableStateFlow(false)
    val isNetworkAvailable = _isNetworkAvailable.asStateFlow()

    val currentUser = flow<CurrentUser> {
        while (true) {
            val _currentUser = MutableStateFlow(CurrentUser())
            currentUserRepository.getCurrentUser()!!.firstOrNull()?.let {
                _currentUser.value = it
            }
            emit(_currentUser.value)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        CurrentUser()
    )

    init {
        isNetworkAvailable()
        FirebaseAuth.getInstance().addAuthStateListener {
            _showAppbarAndBottomBar.value = it.currentUser != null
            viewModelScope.launch(Dispatchers.IO) {
                when {
                    it.currentUser != null -> firebaseCurrentUserRepository.getCurrentUser().collect{
                        currentUserRepository.insert(it)
                        firebaseCurrentUserRepository.updateEmailVerification(
                            FirebaseAuth.getInstance().currentUser!!.isEmailVerified
                        )
                    }

                    it.currentUser == null -> currentUserRepository.deleteCurrentUser()

                }
            }
        }
    }
    fun isNetworkAvailable():Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val context = getApplication<Application>()

                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                val networkCapabilities = connectivityManager.activeNetwork
                val activeNetwork =
                    connectivityManager.getNetworkCapabilities(networkCapabilities)

                _isNetworkAvailable.value = when {
                    activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> true
                    activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> true
                    activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> true
                    else -> false
                }
                kotlinx.coroutines.delay(1000)
            }
        }
        return _isNetworkAvailable.value
    }
}