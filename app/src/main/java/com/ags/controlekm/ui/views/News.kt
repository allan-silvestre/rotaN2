package com.ags.controlekm.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ags.controlekm.R
import com.ags.controlekm.components.Cards.CardNews
import com.ags.controlekm.database.Models.NewsCardItem

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun News(navController: NavHostController) {
    // NEWS CARDITEM
    val cardItemsNews = listOf(
        NewsCardItem(
            "Lorem ipsum",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            painterResource(id = R.drawable.profile01),
            "Anderson",
            "Coordenador de T.I",
            "05/05/2023 - 09:55",
            MaterialTheme.colorScheme.primary
        ),
        NewsCardItem(
            "Lorem ipsum",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            painterResource(id = R.drawable.profile02),
            "Mariana",
            "Assistente de RH",
            "04/05/2023 - 08:33",
            MaterialTheme.colorScheme.primary
        ),
        NewsCardItem(
            "Lorem ipsum",
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
            painterResource(id = R.drawable.profile03),
            "Pedro",
            "Coordenador T.I",
            "28/04/2023 - 14:03",
            MaterialTheme.colorScheme.primary
        ),
        NewsCardItem(
            "Lorem ipsum",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            painterResource(id = R.drawable.profile01),
            "Anderson",
            "Coordenador T.I",
            "27/04/2023 - 08:26",
            MaterialTheme.colorScheme.primary
        ),
    )
    BoxWithConstraints(
        modifier = Modifier
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
        ) {
            itemsIndexed(cardItemsNews) { Index, cardItem ->
                CardNews(
                    cardItem,
                    modifier = Modifier
                        .width(this@BoxWithConstraints.maxWidth)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
