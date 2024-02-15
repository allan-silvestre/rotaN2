package com.ags.controlekm.ui.views.app.fragments

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.ags.controlekm.R
import com.ags.controlekm.viewModels.CurrentUserViewModel
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    drawerState: DrawerState,
    actionsOnClick: () -> Unit,
    currentUserViewModel: CurrentUserViewModel = hiltViewModel<CurrentUserViewModel>()
) {
    val scope = rememberCoroutineScope()

    val currentUserImage = rememberImagePainter(
        data = currentUserViewModel.currentUser.value.image,
        builder = {
            CircularProgressIndicator()
            //placeholder(R.drawable.perfil)
            //error(R.drawable.perfil)
        }
    )

    TopAppBar(
        title = {
            Box {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "${currentUserViewModel.currentUser.value.name} ${currentUserViewModel.currentUser.value.lastName}",
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = currentUserViewModel.currentUser.value.position,
                        fontSize = 11.sp,
                        lineHeight = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            Image(
                painter = currentUserImage,
                contentDescription = "user imagem",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { scope.launch { drawerState.open() } },
            )
        },
        actions = {
            IconButton(
                onClick = {
                    actionsOnClick()
                }) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    )
}


