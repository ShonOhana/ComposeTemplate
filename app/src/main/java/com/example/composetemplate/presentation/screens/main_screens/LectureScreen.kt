package com.example.composetemplate.presentation.screens.main_screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetemplate.data.models.server_models.Lecture
import com.example.composetemplate.presentation.common.LectureCard
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.composetemplate.R
import com.example.composetemplate.ui.theme.CustomTheme
import com.example.composetemplate.ui.theme.CustomTheme.colors

// TODO: put that in top bar and then change background
// TODO: check time format

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LectureScreen(
    modifier: Modifier = Modifier,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr
//    lectures: List<Lecture> //TODO: replace to viewModel to fetch the lectures
) {
    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        // Sample data, replace with ViewModel
        val lectures = listOf(
            Lecture("Ktor", "Shon Ohana", "2025-10-21T07:43:59.183Z"),
            Lecture("Koin", "Raz Cohen", "2026-10-21T07:43:59.183Z"),
            Lecture("Swift", "Tom Zion", "2027-10-21T07:43:59.183Z"),
            Lecture("Compose", "Idan Edri", "2024-10-21T07:43:59.183Z"),
            Lecture("Niggling", "Noam Haba", "2024-10-21T07:43:59.183Z"),
            Lecture("Russian", "Alex something", "2024-10-21T07:43:59.183Z")
        )

        // Separate the lectures into past and upcoming based on the date
        val pastLectures = lectures.filter { it.isPast }
        val upcomingLectures = lectures.filter { !it.isPast }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        // Using ConstraintLayout to center the title
                        ConstraintLayout(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val (titleRef, iconRightRef) = createRefs()

                            Text(
                                text = "Ono Lectures",
                                style = CustomTheme.typography.getLecturesTopBarTitleStyle(),
                                modifier = Modifier.constrainAs(titleRef) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                }
                            )

                            // Right Icons
                            Row(
                                modifier = Modifier.constrainAs(iconRightRef) {
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                }.padding(end = 18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // First Icon
                                IconButton(modifier = modifier.size(36.dp), onClick = { /* Handle first icon click */ }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.add_circle), // Replace with your first icon
                                        contentDescription = "First Icon",
                                        tint = colors.loginEnable
                                    )
                                }
                                // Second Icon
                                IconButton(modifier = modifier.size(36.dp),onClick = { /* Handle second icon click */ }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.settings), // Replace with your second icon
                                        contentDescription = "Second Icon",
                                        tint = colors.loginEnable
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colors.loginScreen
                    ),
                )
            },
            content = { paddingValues ->
                // Apply the background color here
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.loginScreen)
                        .padding(paddingValues)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Upcoming lectures section
                        itemsIndexed(upcomingLectures) { index, lecture ->
                            LectureCard(lecture = lecture, isExpanded = index == 0)
                            if (index == 0)
                                Spacer(modifier = modifier.height(30.dp))
                        }

                        // If there are past lectures, show a "Past Lectures" title and list them
                        if (pastLectures.isNotEmpty()) {
                            // Add a title for the past lectures section
                            item {
                                Text(
                                    text = "Past Lectures",
                                    modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                                    style = CustomTheme.typography.getPastLectureTitleStyle(),
                                )
                            }

                            // Past lectures section
                            items(pastLectures) { lecture ->
                                LectureCard(lecture = lecture)
                            }
                        }
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LectureScreenPrev() {
    LectureScreen()
}