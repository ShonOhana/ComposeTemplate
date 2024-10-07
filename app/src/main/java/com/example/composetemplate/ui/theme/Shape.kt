package com.example.composetemplate.ui.theme


import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

val roundedCornerShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

val cutCornerShapes = Shapes(
    extraSmall = CutCornerShape(4.dp),
    small = CutCornerShape(8.dp),
    medium = CutCornerShape(16.dp),
    large = CutCornerShape(24.dp),
    extraLarge = CutCornerShape(32.dp)
)

val circle = CircleShape

/**
 * The Immutable annotation is used for optimization. For more information,
 * @see Immutable
 *
 * @property CustomTypography A data class that contains all our text styles.
 */
@Immutable
data class CustomShapes(
    val button: Shape = circle,
    val roundedTextField:Shape = circle,
    val roundedBaseNativeDialog:Shape = RoundedCornerShape(16.dp)
)