package com.ags.controlekm.ui.views.app.fragments

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataThresholding
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.outlined.DataThresholding
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ags.controlekm.navigation.navigateSingleTopTo
import com.ags.controlekm.ui.views.app.models.BottomNavigationItem

@Composable
fun BottomBar(navController: NavHostController) {
    NavigationBar(
        modifier = Modifier.wrapContentSize(),
    ) {
        val itemsBottomBar = listOf(
            BottomNavigationItem(
                title = "Home",
                "home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                hasNews = false
            ),
            BottomNavigationItem(
                title = "newService",
                "newService",
                selectedIcon = Icons.Filled.DirectionsCar,
                unselectedIcon = Icons.Outlined.DirectionsCar,
                hasNews = false
            ),
            BottomNavigationItem(
                title = "news",
                "news",
                selectedIcon = Icons.Filled.Newspaper,
                unselectedIcon = Icons.Outlined.Newspaper,
                hasNews = true,
                badgeCount = 45
            ),
            BottomNavigationItem(
                title = "home",
                "home",
                selectedIcon = Icons.Filled.DataThresholding,
                unselectedIcon = Icons.Outlined.DataThresholding,
                hasNews = false,
            ),
        )

        var selectedItem by rememberSaveable { mutableStateOf(0) }

        itemsBottomBar.forEachIndexed { index, item ->
            NavigationBarItem(
                modifier = Modifier,
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigateSingleTopTo(item.title)
                },
                label = { Text(text = item.title) },
                alwaysShowLabel = false,
                icon = {
                    BadgedBox(
                        modifier = Modifier,
                        badge = {
                            if (item.badgeCount != null) {
                                Badge {
                                    Text(text = item.run { badgeCount.toString() })
                                }
                            } else if (item.hasNews) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (index == selectedItem) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    }
}