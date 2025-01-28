package com.example.composetemplate.presentation.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(
    type: TopAppBarType,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    ),
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors()
) {
    when(type) {
        TopAppBarType.LARGE_TOP_APP_BAR -> {
            LargeTopAppBar(
                modifier = modifier,
                scrollBehavior =  scrollBehavior,
                actions = actions,
                title = title,
                colors = colors
            )
        }

        TopAppBarType.MEDIUM_TOP_APP_BAR -> {
            MediumTopAppBar(
                modifier = modifier,
                scrollBehavior =  scrollBehavior,
                actions = actions,
                title = title,
                colors = colors
            )
        }
        TopAppBarType.SMALL_TOP_APP_BAR -> {
            TopAppBar(
                modifier = modifier,
                scrollBehavior =  scrollBehavior,
                actions = actions,
                title = title,
                colors = colors
            )
        }
    }

}

enum class TopAppBarType {
    LARGE_TOP_APP_BAR,
    MEDIUM_TOP_APP_BAR,
    SMALL_TOP_APP_BAR,
}