package com.ags.controlekm.ui.views.serviceManager.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EmailVerifieldDialog(
    onDismissRequest: () -> Unit,
) {
    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    val authStateListener = rememberUpdatedState {
        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            currentUser = firebaseAuth.currentUser
        }
    }

    var count by remember { mutableStateOf(0) }

    var running by remember { mutableStateOf(true) }
    var tempoRestante by remember { mutableStateOf(2 * 60 * 1000) }

    val format = SimpleDateFormat("mm:ss", Locale.getDefault())

    val progressIndicator = remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { }) {
        Card(
            modifier = Modifier
                .wrapContentSize(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
        ) {
            AnimatedContent(
                targetState = count,
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
                                .wrapContentSize()
                                .padding(32.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Seu email não foi verificado, \n por favor, realize a verificação para ter acesso a todas as funcionalidades disponivéis para seu você.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Column(
                                modifier = Modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.End
                            ) {
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
                                        count = 1
                                        currentUser?.sendEmailVerification()
                                    }
                                ) {
                                    Text(
                                        text = "Verificar agora",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    modifier = Modifier
                                        .padding(end = 6.dp)
                                        .clickable {
                                            count = 0
                                            onDismissRequest()
                                        },
                                    text = "Continuar sem verificar",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    1 -> {
                        LaunchedEffect(running) {
                            while (running && tempoRestante > 0) {
                                delay(1000)
                                tempoRestante -= 1000
                            }
                            running = false
                        }
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(32.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column(
                                modifier = Modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "Um link de verificação foi enviado para ${currentUser?.email}.",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Após seguir as instruções e realizar a verificação, por favor clique em confirmar.",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
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
                                        count = 2
                                    }
                                ) {
                                    Text(
                                        text = "Confirmar",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        modifier = Modifier.padding(end = 3.dp),
                                        text = "Não recebeu o e-mail?",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        modifier = Modifier
                                            .padding(end = 6.dp)
                                            .clickable {
                                                if (!running) {
                                                    currentUser?.sendEmailVerification()
                                                    tempoRestante = 2 * 60 * 1000
                                                    running = true
                                                }
                                            },
                                        text = "Reenviar",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (!running) MaterialTheme.colorScheme.surfaceTint else Color.LightGray
                                    )
                                    Text(
                                        text = if (!running) "" else format.format(tempoRestante),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                    2 -> {
                        progressIndicator.value = true
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = "Verificando suas informações",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (progressIndicator.value) {
                                LinearProgressIndicator(
                                    modifier = Modifier
                                        .padding(start = 50.dp, end = 50.dp, top = 10.dp, bottom = 50.dp),
                                    color = MaterialTheme.colorScheme.secondary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                )
                            }
                            LaunchedEffect(authStateListener.value){
                                authStateListener.value
                                while(progressIndicator.value){
                                    currentUser?.reload()
                                    delay(2000)
                                    if(currentUser?.isEmailVerified == true) {
                                        count = 3
                                        progressIndicator.value = false
                                    } else {
                                        count = 1
                                        progressIndicator.value = false
                                    }
                                }
                            }
                        }
                    }
                    3 -> {
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(32.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column(
                                modifier = Modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "Seu e-mail ${currentUser?.email}\n foi verificado com sucesso, obrigado!!.",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
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
                                        count = 0
                                        onDismissRequest()
                                    }
                                ) {
                                    Text(
                                        text = "Ir para Home",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

