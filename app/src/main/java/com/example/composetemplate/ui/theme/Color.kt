package com.example.composetemplate.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val Green = Color(0xFF2CB631)
val Red = Color(0xFFD81533)
val Orange = Color(0xFFFF9800)
val White = Color(0xFFFFFFFF)
val DarkBlue222325 = Color(0xFF222325)
val Gray37383A = Color(0xFF37383A)
val Gray67 = Color(0xABFFFFFF)
val LightBlue01B3BD = Color(0xFF01B3BD)

/**
 * The Immutable annotation is used for optimization. For more information,
 * @see Immutable
 *
 * @property CustomColorsPalette A data class that contains all our custom colors.
 * Note: Colors that depend on the UI mode (dark, light) are set as Unspecified here and are given their actual values later in the Theme class.
 */

@Immutable
data class CustomColorsPalette(
     val success: Color = Green,
     val error: Color = Red,
     val loginScreen: Color = DarkBlue222325,
     val loginTextField: Color = Gray37383A,
     val loginButtonDisable: Color = Gray67,
     val loginEnable: Color = LightBlue01B3BD,
     val authEditTextContainer: Color = Color.DarkGray,
     val text: Color = White,
     val circleImageBorder: Color = White,
     val nativeDialogBackground: Color = White,
     val progress: Color = White,
     val title: Color = Color.Unspecified,
     val cursor: Color = Color.Unspecified,
)
