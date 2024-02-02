package com.ags.controlekm.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ags.controlekm.R
import com.ags.controlekm.components.FormularioOutlinedTextField
import com.ags.controlekm.components.FormularioOutlinedTextFieldMenu
import com.ags.controlekm.database.FirebaseServices.CurrentUserServices
import com.ags.controlekm.database.FirebaseServices.UserServices
import com.ags.controlekm.database.Models.CurrentUser
import com.ags.controlekm.database.Models.User
import com.ags.controlekm.database.ViewModels.LoginViewModel
import com.ags.controlekm.database.ViewModels.UserViewModel
import com.ags.controlekm.functions.navigateSingleTopTo
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginView(
    navController: NavHostController,
    loginViewModel: LoginViewModel = viewModel(),
) {
    val context = LocalContext.current

    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    val loginIsCompleted by loginViewModel.authResult.collectAsState(false)

    val userLoggedData by loginViewModel.currentUserData.collectAsState(null)

    DisposableEffect(currentUser?.uid) {
        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            currentUser = firebaseAuth.currentUser
            currentUser?.reload()
        }
        onDispose { }
    }
    var countContent by rememberSaveable { mutableIntStateOf(0) }

    val progressIndicator = remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf(true) }
    var emailErrorMessage by remember { mutableStateOf("") }

    var senha by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var senhaError by rememberSaveable { mutableStateOf(true) }
    var senhaErrorMessage by remember { mutableStateOf("") }

    val espacoEntreCampos = 6.dp

    var buttonProximoVisible by rememberSaveable { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedContent(
            targetState = countContent,
            label = "",
            transitionSpec = {
                (fadeIn() + slideInHorizontally(animationSpec = tween(600),
                    initialOffsetX = { fullHeight -> fullHeight })).togetherWith(
                    fadeOut(
                        animationSpec = tween(400)
                    )
                )
            }
        ) { targetCount ->
            when (targetCount) {
                0 -> {
                    progressIndicator.value = true
                    buttonProximoVisible = false
                    //AUTENTICAÇÃO DO APARELHO
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Spacer(modifier = Modifier.height(espacoEntreCampos))
                        if (progressIndicator.value) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(50.dp),
                                progress = 0.89f,
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        }
                        if (currentUser != null) {
                            val uid = currentUser?.uid
                            val email = currentUser?.email

                            navController.navigateSingleTopTo("home")
                            progressIndicator.value = false
                        } else {
                            DisposableEffect(Unit) {
                                countContent = 1
                                onDispose {
                                    // Limpa recursos, se necessário
                                }
                            }
                        }
                    }
                }

                1 -> {
                    buttonProximoVisible = true
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        item {
                            Image(
                                modifier = Modifier
                                    .size(150.dp),
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = ""
                            )
                        }
                        item {
                            Text(
                                text = "Login",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                        item {
                            FormularioOutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = false,
                                value = email,
                                onValueChange = { email = it },
                                label = "E-mail",
                                visualTransformation = VisualTransformation.None,
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next,
                                capitalization = KeyboardCapitalization.None,
                                erro = emailError,
                                erroMensagem = emailErrorMessage
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                        item {
                            FormularioOutlinedTextFieldMenu(
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = false,
                                value = senha,
                                onValueChange = { senha = it },
                                trailingIconVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                trailingOnClick = { passwordVisibility = !passwordVisibility },
                                label = "Senha",
                                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next,
                                capitalization = KeyboardCapitalization.None,
                                erro = senhaError,
                                erroMensagem = senhaErrorMessage
                            )
                        }
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(start = 3.dp, end = 6.dp)
                                        .clickable {
                                            navController.navigateSingleTopTo("forgotPassword")
                                        },
                                    text = "Esqueceu sua senha?",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.surfaceTint
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(5.dp),
                                elevation = ButtonDefaults.elevatedButtonElevation(
                                    defaultElevation = 5.dp,
                                    pressedElevation = 2.dp
                                ),
                                onClick = { countContent++ }) {
                                Text(
                                    text = "Entrar",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = "Não tem uma conta?",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(start = 3.dp, end = 6.dp)
                                        .clickable {
                                            navController.navigateSingleTopTo("newUser")
                                        },
                                    text = "Cadastre-se",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.surfaceTint
                                )
                            }
                        }
                    }
                }

                2 -> {
                    loginViewModel.login(email, senha)
                    progressIndicator.value = true
                    buttonProximoVisible = false
                    //AUTENTICAÇÃO DO APARELHO
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Spacer(modifier = Modifier.height(espacoEntreCampos))
                        if (progressIndicator.value) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(50.dp),
                                progress = 0.89f,
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )

                        }
                        if (loginIsCompleted) {
                            progressIndicator.value = false
                            navController.popBackStack()
                            navController.navigateSingleTopTo("home")
                        } else {
                            navController.popBackStack()
                            navController.navigateSingleTopTo("login")
                        }
                    }
                }
            }
        }//FIM DO LAÇO WHEN
    }
}//FIM COLUMN
