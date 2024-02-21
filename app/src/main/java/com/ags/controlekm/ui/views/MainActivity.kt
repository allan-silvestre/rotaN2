package com.ags.controlekm.ui.views

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import com.ags.controlekm.ui.views.app.App
import com.ags.controlekm.ui.views.loading.Shimmer
import com.example.compose.AppTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(
                    tonalElevation = 5.dp,
                    shadowElevation = 5.dp
                ) {
                    FirebaseApp.initializeApp(this)
                    Firebase.appCheck.installAppCheckProviderFactory(
                        PlayIntegrityAppCheckProviderFactory.getInstance(),
                    )
                    App()
                    //Shimmer()
                }
            }
        }
    }
}