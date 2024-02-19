package com.ags.controlekm.ui.views.login

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ags.controlekm.R
import com.ags.controlekm.ui.components.textField.FormularioOutlinedTextField
import com.ags.controlekm.ui.components.textField.FormularioOutlinedTextFieldMenu
import com.ags.controlekm.ui.views.login.viewModel.LoginViewModel
import com.ags.controlekm.navigation.navigateSingleTopTo
import com.ags.controlekm.ui.components.buttons.ButtonDefault
import com.ags.controlekm.ui.views.loading.LoadingView
import com.ags.controlekm.ui.views.login.viewModel.modelsParams.LoginParams

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@Composable
fun LoginView(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel<LoginViewModel>(),
) {
    val loading by loginViewModel.loading.collectAsState(false)

    val showLoginView by loginViewModel.showLoginview.collectAsState(false)

    val loginResult by loginViewModel.loginResult.collectAsState(false)

    var email by remember { mutableStateOf("") }
    val emailError by rememberSaveable { mutableStateOf(true) }
    val emailErrorMessage by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val senhaError by rememberSaveable { mutableStateOf(true) }
    val senhaErrorMessage by remember { mutableStateOf("") }

    if (loading) {
        LoadingView()
    } else if (loginResult) {
        navController.popBackStack()
        navController.navigateSingleTopTo("home")
    } else if(showLoginView) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Image(
                    modifier = Modifier
                        .size(120.dp),
                    painter = painterResource(id = R.drawable.nlogo),
                    contentDescription = ""
                )
            }
            item {
                Text(
                    text = "Login",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W900
                )
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
                    value = password,
                    onValueChange = { password = it },
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
                ButtonDefault(
                    "Entrar",
                    onClick = {
                        loginViewModel.login(LoginParams(email, password))
                    })
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
                Spacer(modifier = Modifier.height(100.dp))
            }
            item {
                Text(
                    "Uma solução",
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp
                )
                Image(
                    modifier = Modifier.size(70.dp),
                    painter = painterResource(id = R.drawable.aglogo),
                    contentDescription = ""
                )
            }
        }
    }
}
