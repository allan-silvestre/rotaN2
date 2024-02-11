package com.ags.controlekm.ui.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ags.controlekm.R
import com.ags.controlekm.ui.components.textField.FormularioOutlinedTextField
import com.ags.controlekm.functions.navigateSingleTopTo
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ForgotPasswordView(navController: NavHostController) {

    val currentUser = FirebaseAuth.getInstance().currentUser

    var countContent by rememberSaveable { mutableIntStateOf(0) }

    val progressIndicator = remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf(true) }
    var emailErrorMessage by remember { mutableStateOf("") }

    var emailList = mutableListOf<String>()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // RECUPERAÇÃO DE EMAILS CADASTRADOS
        Image(
            modifier = Modifier
                .size(150.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = ""
        )
        Text(
            text = "Recuperação de senha",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "Informe seu email para continuar",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))
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
                    Column(
                        modifier = Modifier
                            .wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
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
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                1 -> {
                    Column(
                        modifier = Modifier
                            .wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Um e-mail com as instruções para alterar sua senha foi enviado para ${email}, \n lembre-se de verificar sua caixa de spam.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                2 -> {
                    Column(
                        modifier = Modifier
                            .wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Ocorreu um erro ao tentar enviar um e-mail para ${email}, \n tente novamente mais tarde",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(5.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 5.dp,
                pressedElevation = 2.dp
            ),
            onClick = {

                if (countContent > 0) {
                    navController.navigateSingleTopTo("login")
                } else {
                    if (!emailList.contains(email) || email.isEmpty()) {
                        emailError = false
                        emailErrorMessage = "E-mail não encontrado."
                    } else {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener { Task ->
                                if (Task.isSuccessful) {
                                    countContent = 1
                                } else {
                                    countContent = 2
                                }
                            }
                    }
                }
            }
        ) {
            if (countContent > 0) {
                Text(
                    text = "Voltar para a tela de login",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

            } else {
                Text(
                    text = "Enviar email de recuperação",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

