package com.ags.controlekm.components.textField

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FormularioTextFieldMenu(
    modifier: Modifier = Modifier,
    readOnly: Boolean,
    //leadingIconVector: ImageVector?,
    trailingIconVector: ImageVector?,
    trailingOnClick: () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visualTransformation: VisualTransformation,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    capitalization: KeyboardCapitalization,
    erro: Boolean,
    erroMensagem: String,
) {

    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        TextField(
            //shape = RoundedCornerShape(50.dp),
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly,
            trailingIcon = {
                if (trailingIconVector != null) {
                    IconButton(
                        onClick = {
                            trailingOnClick()
                        },
                        content = {
                            Icon(
                                imageVector = trailingIconVector,
                                contentDescription = "",
                            )
                        }
                    )
                }
            },
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                //disabledTextColor = Color.Black, // Text color
                //focusedPlaceholderColor = Color.Gray, // Placeholder color
                //focusedBorderColor = Color.Black, // Border color when focused
                //unfocusedBorderColor = Color.LightGray, // Border color when not focused
                //cursorColor = Color.DarkGray, // Cursor color
                //containerColor = Color.White,
                //unfocusedLabelColor = Color.Black,
                //focusedLabelColor = Color.Black,
                //focusedTextColor = Color.DarkGray,
                //unfocusedTextColor = Color.DarkGray,
                //disabledTrailingIconColor = Color.Black,
                //unfocusedTrailingIconColor = Color.Black,
                //focusedTrailingIconColor = Color.Black,
                //errorTextColor = Color.Red

            ),
            value = value,
            visualTransformation = visualTransformation,
            onValueChange = {
                onValueChange(it)
            },
            singleLine = true,
            label = {
                Text(
                    text = label,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                )
            },
            isError = !erro,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
                capitalization = capitalization,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        if (!erro) {
            Text(
                text = erroMensagem,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FormularioOutlinedTextFieldMenu(
    modifier: Modifier = Modifier,
    readOnly: Boolean,
    //leadingIconVector: ImageVector?,
    trailingIconVector: ImageVector?,
    trailingOnClick: () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visualTransformation: VisualTransformation,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    capitalization: KeyboardCapitalization,
    erro: Boolean,
    erroMensagem: String,
) {

    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        OutlinedTextField(
            //shape = RoundedCornerShape(50.dp),
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly,
            trailingIcon = {
                if (trailingIconVector != null) {
                    IconButton(
                        onClick = {
                            trailingOnClick()
                        },
                        content = {
                            Icon(
                                imageVector = trailingIconVector,
                                contentDescription = "",
                            )
                        }
                    )
                }
            },
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                //disabledTextColor = Color.Black, // Text color
                //focusedPlaceholderColor = Color.Gray, // Placeholder color
                //focusedBorderColor = Color.Black, // Border color when focused
                //unfocusedBorderColor = Color.LightGray, // Border color when not focused
                //cursorColor = Color.DarkGray, // Cursor color
                //containerColor = Color.White,
                //unfocusedLabelColor = Color.Black,
                //focusedLabelColor = Color.Black,
                //focusedTextColor = Color.DarkGray,
                //unfocusedTextColor = Color.DarkGray,
                //disabledTrailingIconColor = Color.Black,
                //unfocusedTrailingIconColor = Color.Black,
                //focusedTrailingIconColor = Color.Black,
                //errorTextColor = Color.Red

            ),
            value = value,
            visualTransformation = visualTransformation,
            onValueChange = {
                onValueChange(it)
            },
            singleLine = true,
            label = {
                Text(
                    text = label,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                )
            },
            isError = !erro,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
                capitalization = capitalization,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        if (!erro) {
            Text(
                text = erroMensagem,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}