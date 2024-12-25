package com.example.composetemplate.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.composetemplate.ui.theme.CustomTheme.colors

/**
 * BasicBottomDialog Component
 *
 * This composable provides a reusable implementation of a basic bottom dialog (bottom sheet) using Jetpack Compose.
 * It leverages Material 3's `ModalBottomSheet` to display a dismissible bottom sheet dialog.
 *
 * Notes:
 * - The component uses Material 3's experimental API; include the `@OptIn(ExperimentalMaterial3Api::class)` annotation where necessary.
 * - `colors` is assumed to be a predefined object holding theme-related colors.
 */



/** Parameters:
 * - modifier: A [Modifier] for applying layout or behavior changes to the dialog.
 * - bottomSheetUI: A [Composable] lambda defining the UI content displayed in the bottom sheet.
 * - containerColor: A [Color] specifying the background color of the bottom sheet.
 * - scrimColor: A [Color] specifying the overlay color when the dialog is visible.
 * - onDismiss: A lambda function called when the dialog is dismissed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicBottomDialog(
    modifier: Modifier,
    bottomSheetUI: @Composable () -> Unit,
    containerColor: Color = colors.bottomSheetDialogBackground,
    scrimColor: Color = colors.bottomSheetScrimColor,
    onDismiss: () -> Unit) {

    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss.invoke()
        },
        sheetState = sheetState,
        modifier = modifier,
        containerColor = containerColor,
        scrimColor = scrimColor
    ) {
        bottomSheetUI()
    }
}