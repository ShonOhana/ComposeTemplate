package com.example.composetemplate.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val TitleStyle = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Bold,
    fontSize = 48.sp,
    letterSpacing = 0.5.sp
)

/**
 * The Immutable annotation is used for optimization. For more information,
 * @see Immutable
 *
 * @property CustomTypography A data class that contains all our text styles.
 */
@Immutable
data class CustomTypography(
    val title: TextStyle = TextStyle.Default,
) {
    @Composable
    fun getLectureAuthorStyle(): TextStyle {
        return MaterialTheme.typography.titleLarge.copy(
            color = CustomTheme.colors.text,
            fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun getLectureTopicStyle(): TextStyle {
        return MaterialTheme.typography.titleMedium
    }

    @Composable
    fun getLectureTimeStampStyle(isPast: Boolean): TextStyle {
        return MaterialTheme.typography.bodySmall.copy(
            color = if (isPast) CustomTheme.colors.error
            else CustomTheme.colors.text
        )
    }

    @Composable
    fun getPastLectureTitleStyle(): TextStyle {
        return MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.ExtraBold,
            color = CustomTheme.colors.text,
        )
    }

    @Composable
    fun getLecturesTopBarTitleStyle(): TextStyle {
        return MaterialTheme.typography.titleMedium.copy(
            color = CustomTheme.colors.loginEnable,
        )
    }
}