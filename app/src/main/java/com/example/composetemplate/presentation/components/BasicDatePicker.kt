package com.example.composetemplate.presentation.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.composetemplate.ui.theme.CustomTheme

/**
 * BaseDatePicker Component
 *
 * This composable provides a flexible and customizable implementation of a DatePicker using Jetpack Compose.
 * It supports different configurations to cater to various use cases, such as full DatePicker with title and headline,
 * or a minimalist version without a headline.
 * Use `BaseDatePicker` to render a DatePicker with predefined color themes and configurable layout.
 * Choose between `FULL` or `NO_HEADLINE` types to customize the appearance.
 * Customize colors, title, headline, and other behaviors as per your requirements.
 * This component uses Material 3's experimental API. Add the @OptIn(ExperimentalMaterial3Api::class) annotation where necessary.
 */





/** Parameters:
 * type: A [DatePickerType] specifying the layout of the DatePicker (`FULL` or `NO_HEADLINE`).
 * modifier: A [Modifier] for applying layout or behavior changes to the DatePicker.
 * colors: A [DatePickerColors] instance to define the color scheme. Defaults to a custom theme from `CustomTheme`.
 * headline: A [Composable] lambda for custom headline content (optional, used in `FULL` type).
 * title: A [Composable] lambda for custom title content (optional, used in `FULL` type). */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseDatePicker(
    type: DatePickerType,
    modifier: Modifier = Modifier,
    colors: DatePickerColors = DatePickerDefaults.colors(),
    headline: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null
) {
    val datePickerState = rememberDatePickerState()

    when (type) {
        DatePickerType.FULL -> FullDatePicker(
            state = datePickerState,
            modifier = modifier,
            colors = colors,
            headline = headline,
            title = title
        )

        DatePickerType.NO_HEADLINE ->
            NoHeadLineDatePicker(
                state = datePickerState,
                modifier = modifier,
                colors = colors
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoHeadLineDatePicker(
    modifier: Modifier = Modifier,
    state: DatePickerState,
    colors: DatePickerColors = DatePickerDefaults.colors()
) {
    DatePicker(
        state = state,
        title = null,
        headline = null,
        showModeToggle = false,
        modifier = modifier,
        colors = colors
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FullDatePicker(
    modifier: Modifier = Modifier,
    state: DatePickerState,
    colors: DatePickerColors = DatePickerDefaults.colors(),
    headline: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null
) {
    DatePicker(
        state = state,
        title = {
            if (title != null) {
                title()
            }
        },
        headline = {
            if (headline != null) {
                headline()
            }
        },
        showModeToggle = true,
        modifier = modifier,
        colors = colors
    )
}

enum class DatePickerType {
    FULL, NO_HEADLINE
}
