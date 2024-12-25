package com.example.composetemplate.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * This file defines a composable function `BaseTimePickerDialog` that serves as a reusable
 * base component for displaying a Material 3 style time picker dialog. It provides a flexible
 * API for customization and integration with various UI requirements.
 *
 * Key Features:
 * - Displays a customizable time picker dialog with user-defined dismiss and confirm buttons.
 * - Allows seamless integration with Jetpack Compose's state management via `MutableState<Boolean>`.
 * - Provides flexibility for developers to define the behavior and appearance of buttons through
 *   composable lambdas (`dismissButton`, `confirmButton`).
 * - Supports color customization for the time picker selector using the `color` parameter.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTimePickerDialog(
    showTimePicker: MutableState<Boolean>,
    color: Color,
    timePickerState: TimePickerState,
    dismissButton: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit
) {

    AlertDialog(
        modifier = Modifier.wrapContentSize(),
        onDismissRequest = { showTimePicker.value = false },
        dismissButton = dismissButton,
        confirmButton = confirmButton,
        text = {

            Column {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors().copy(
                        selectorColor = color
                    )
                )
            }
        }
    )
}