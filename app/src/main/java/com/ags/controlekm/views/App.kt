package com.ags.controlekm.views

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataThresholding
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.DataThresholding
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberImagePainter
import com.ags.controlekm.R
import com.ags.controlekm.components.Dialog.DialogEmailVerifield
import com.ags.controlekm.database.ViewModels.CurrentUserViewModel
import com.ags.controlekm.functions.navigateSingleTopTo
import com.ags.controlekm.objects.BottomNavigationItem
import com.ags.controlekm.objects.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "UnrememberedMutableState"
)
@Composable
fun App( currentUserViewModel: CurrentUserViewModel = viewModel() ) {

    val userLoggedData by currentUserViewModel.currentUserData.collectAsState(null)

    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    val context = LocalContext.current
    val navController = rememberNavController()

    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    var selectedBottomBarItemIndex by rememberSaveable { mutableStateOf(0) }

    var itemsVisible by rememberSaveable { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    //ImagemPerfilAlertDialog
    var dialogEmailVisible = remember { mutableStateOf(true) }

    val perfilImg = rememberImagePainter(
        data = userLoggedData?.imagem,
        builder = {
            // You can customize loading and error placeholders if needed
            placeholder(R.drawable.perfil)
            error(R.drawable.perfil)
        }
    )

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

    ModalNavigationDrawer(
        modifier = Modifier,
        drawerContent = {
            AnimatedVisibility(visible = itemsVisible) {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxWidth(0.7f),
                    drawerTonalElevation = 6.dp,
                    drawerShape = RoundedCornerShape(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        //verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.height(32.dp))
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .clickable {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                },
                            model = userLoggedData?.imagem,
                            contentDescription = "",
                            loading = {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(35.dp),
                                    color = MaterialTheme.colorScheme.secondary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                )
                            },
                        )
                        if (userLoggedData?.emailVerificado == true) {
                            Icon(
                                modifier = Modifier
                                    .size(16.dp),
                                imageVector = Icons.Filled.Verified,
                                tint = Color(0xFF228B22),
                                contentDescription = ""
                            )
                        } else {
                            Icon(
                                modifier = Modifier
                                    .size(16.dp),
                                imageVector = Icons.Filled.Verified,
                                tint = Color(0xFFeb0b0b),
                                contentDescription = ""
                            )
                        }
                        Text(
                            text = currentUser?.email.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${userLoggedData?.nome} ${userLoggedData?.sobrenome}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = "${userLoggedData?.cargo}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light
                        )
                        Text(
                            text = "${userLoggedData?.setor}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light
                        )
                        Icon(
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    scope.launch {
                                        drawerState.close()
                                        FirebaseAuth
                                            .getInstance()
                                            .signOut()
                                        navController.popBackStack()
                                        navController.navigateSingleTopTo("login")
                                    }
                                },
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = ""
                        )
                        Divider(
                            modifier = Modifier
                                .padding(top = 8.dp, start = 6.dp, end = 6.dp),
                        )
                        // CONTEUDO PARTE INFERIOR
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.padding(32.dp),
                                text = "LOGO"
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
            // FIM DA ANIMAÇÃO
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                AnimatedVisibility(visible = itemsVisible) {
                    TopAppBar(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(start = 8.dp, end = 8.dp),
                        title = {
                            Box {
                                Column(
                                    modifier = Modifier,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        //modifier = Modifier.height(16.dp),
                                        text = "${userLoggedData?.nome} ${userLoggedData?.sobrenome}",
                                        fontSize = 12.sp,
                                        lineHeight = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                    Text(
                                       // modifier = Modifier.height(20.dp),
                                        text = "${userLoggedData?.cargo}",
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
                                painter = perfilImg,
                                contentDescription = "user imagem",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    },
                            )
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    showBottomSheet = true
                                }) {
                                Icon(
                                    Icons.Filled.Menu,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    )
                } //FIM ANIMAÇÃO
            },
            bottomBar = {
                AnimatedVisibility(visible = itemsVisible) {
                    NavigationBar(
                        modifier = Modifier.wrapContentSize(),
                    ) {
                        itemsBottomBar.forEachIndexed { index, item ->
                            NavigationBarItem(
                                modifier = Modifier,
                                selected = selectedBottomBarItemIndex == index,
                                onClick = {
                                    selectedBottomBarItemIndex = index
                                    navController.navigateSingleTopTo(item.title)
                                },
                                /***
                                label = {
                                Text(modifier = Modifier.size(0.dp),
                                text = item.title
                                )
                                },***/
                                /***
                                label = {
                                Text(modifier = Modifier.size(0.dp),
                                text = item.title
                                )
                                },***/
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
                                            imageVector = if (index == selectedBottomBarItemIndex) {
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
                //FIM ANIMAÇÃO
            }
        ) { innerPadding ->
            if (currentUser?.isEmailVerified == false && dialogEmailVisible.value) {
                DialogEmailVerifield(
                    onDismissRequest = { dialogEmailVisible.value = false }
                )
            }
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = "login"
            ) {
                composable("login") {
                    LoginView(navController)
                    DisposableEffect(Unit) {
                        itemsVisible = false
                        onDispose {
                        }
                    }
                }
                composable("newUser") {
                    CadastroUserView(navController)
                }
                composable("forgotPassword") {
                    ForgotPassword(navController)
                }
                composable("home") {
                    Home(navController)
                    DisposableEffect(Unit) {
                        itemsVisible = true
                        onDispose {
                            // Limpa recursos, se necessário
                        }
                    }
                }
                composable("news") {
                    News(navController)
                }
                composable("enderecosAtendimento") {
                    EnderecosAtendimentoView(navController)
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
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
                                navController.navigateSingleTopTo(item.navHostLink)
                                selectedItemIndex = index
                                scope.launch { showBottomSheet = false }
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
        }
    }
}




