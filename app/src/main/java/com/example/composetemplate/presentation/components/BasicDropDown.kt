package com.example.composetemplate.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity

@Composable
fun BasicDropDown(
    data: List<String>,
    modifier: Modifier,
    dropdownMenuModifier: Modifier,
    dropDownHeader: @Composable () -> Unit,
    dropDownItem: @Composable (String) -> Unit
) {

    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    val itemPosition = remember {
        mutableIntStateOf(0)
    }

    val boxWidth = remember { mutableStateOf(0) }

    Box(modifier = modifier
        .clickable {
            isDropDownExpanded.value = true
        }
        .onGloballyPositioned { coordinates ->
            boxWidth.value = coordinates.size.width
        }
    ) {
        dropDownHeader()

        DropdownMenu(
            modifier = dropdownMenuModifier.width(with(LocalDensity.current) { boxWidth.value.toDp() }),
            expanded = isDropDownExpanded.value,
            onDismissRequest = { isDropDownExpanded.value = false }) {
            data.forEachIndexed { index, item ->

                DropdownMenuItem(text = {
                    dropDownItem(item)
                }, onClick = {
                    isDropDownExpanded.value = false
                    itemPosition.intValue = index
                })
            }
        }
    }
}