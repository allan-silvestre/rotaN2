package com.ags.controlekm.ui.views.app.fragments

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ags.controlekm.navigation.navigateSingleTopTo
import com.ags.controlekm.ui.views.app.models.MenuItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    navController: NavHostController,
    onDismissRequest: () -> Unit,
    onClick: () -> Unit,
) {

    val itemsBottomSheet = listOf(
        MenuItem(
            "Colaboradores",
            "news",
            Icons.Filled.Person,
            Icons.Outlined.Person,
        ),
        MenuItem(
            "Locais de atendimento",
            "enderecosAtendimento",
            Icons.Filled.LocationCity,
            Icons.Outlined.LocationCity,
        ),
        MenuItem(
            "Configurações",
            "news",
            Icons.Filled.Settings,
            Icons.Outlined.Settings,
        ),
    )

    val sheetState = rememberModalBottomSheetState()

    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState
    ) {
        // Sheet content
        itemsBottomSheet.forEachIndexed { index, item ->
            NavigationDrawerItem(
                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                selected = false, //index == selectedItemIndex,
                onClick = {
                    selectedItemIndex = index
                    navController.navigateSingleTopTo(item.navHostLink)
                    onClick()
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedItemIndex) {
                            item.selectedIcon
                        } else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                modifier = Modifier
                    .padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}