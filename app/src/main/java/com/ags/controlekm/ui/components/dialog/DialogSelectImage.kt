package com.ags.controlekm.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

// ALERTA DIALOG PARA SELECIONAR IMAGEM DO PERFIL
@Composable
fun AlertDialogSelectImage(
    onDismissRequest: () -> Unit,
    abrirCamera: () -> Unit,
    abrirGallery: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(64.dp),
            shape = RoundedCornerShape(16.dp),

            ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Título alertDialog
                Text(
                    text = "Imagem do perfil",
                    modifier = Modifier
                        //.fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        IconButton(
                            modifier = Modifier
                                .background(Color.White, CircleShape)
                                .size(40.dp)
                                .padding(10.dp),
                            onClick = {
                                abrirCamera.invoke()
                            }
                        ) {
                            Icon(
                                Icons.Filled.CameraAlt,
                                contentDescription = "Abrir Camera",
                            )
                        }
                        Text(
                            text = "Camera",
                            modifier = Modifier
                                //.fillMaxSize()
                                .wrapContentSize(Alignment.Center),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 9.sp,
                        )
                    }

                    Spacer(modifier = Modifier.padding(16.dp))

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        IconButton(
                            modifier = Modifier
                                .background(Color.White, CircleShape)
                                .size(40.dp)
                                .padding(10.dp),
                            onClick = {
                                abrirGallery.invoke()
                            }
                        ) {
                            Icon(
                                Icons.Filled.Image,
                                contentDescription = "Abrir Galeria",
                            )
                        }
                        Text(
                            text = "Galeria",
                            modifier = Modifier
                                //.fillMaxSize()
                                .wrapContentSize(Alignment.Center),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 9.sp,
                        )
                    }
                }

            }

        }
    }
}
