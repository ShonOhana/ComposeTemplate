package com.example.composetemplate.presentation.common

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetemplate.R
import com.example.composetemplate.data.models.server_models.Lecture
import com.example.composetemplate.data.remote.analytics.AnalyticsLogger.Companion.logLectureTopic
import com.example.composetemplate.ui.theme.CustomTheme
import com.example.composetemplate.utils.Constants.Companion.LECTURE_CARD_NEXT_LECTURE_TEXT
import com.example.composetemplate.utils.Constants.Companion.LECTURE_CARD_PAST_TIME_TEXT
import com.example.composetemplate.utils.DateUtil

@Composable
fun LectureCard(
    modifier: Modifier = Modifier,
    lecture: Lecture,
    isExpanded: Boolean = false,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isExpanded) 300.dp else 150.dp)
            .padding(8.dp)
            .clickable {
                logLectureTopic(lecture.topic)
            },
        shape = RoundedCornerShape(0.dp, 13.dp, 13.dp, 13.dp),
        colors = CardDefaults.cardColors(containerColor = CustomTheme.colors.loginEnable)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            if (isExpanded) {
                Text(
                    text = LECTURE_CARD_NEXT_LECTURE_TEXT,
                    color = CustomTheme.colors.text,
                    style = CustomTheme.typography.getLectureTopicStyle(),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = modifier.height(10.dp))

            if (!isExpanded) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background), // Replace with your image
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(50.dp)
                            .border(1.dp, CustomTheme.colors.circleImageBorder, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = lecture.developerName,
                        style = CustomTheme.typography.getLectureAuthorStyle(),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background), // Replace with your image
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(100.dp)
                            .align(Alignment.CenterHorizontally)
                            .border(1.dp, CustomTheme.colors.circleImageBorder, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = lecture.developerName,
                        color = CustomTheme.colors.text,
                        style = CustomTheme.typography.getLectureAuthorStyle(),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            Spacer(modifier = modifier.height(10.dp))

            Text(
                text = lecture.topic,
                color = CustomTheme.colors.text,
                style = CustomTheme.typography.getLectureTopicStyle(),
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = modifier.height(6.dp))

            Text(
                text = if (lecture.isPast) LECTURE_CARD_PAST_TIME_TEXT else DateUtil.lectureFormatDate(lecture.dueDate),
                style = CustomTheme.typography.getLectureTimeStampStyle(lecture.isPast),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

/* This is preview to see the Ui of the LectureCard */
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LectureCardPrev() {
    LectureCard(
        lecture = Lecture(
            "idddddddd",
            "Ktor",
            "User name",
            "2024-10-21T07:43:59.183Z"
        )
    )
}