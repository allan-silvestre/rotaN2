package com.ags.controlekm.viewModels.app

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AppViewModel @Inject constructor(): ViewModel() {

    private var _showAppbarAndBottomBar = MutableStateFlow(false)
    val showAppbarAndBottomBar = _showAppbarAndBottomBar.asStateFlow()

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            _showAppbarAndBottomBar.value = it.currentUser != null
        }
    }
}