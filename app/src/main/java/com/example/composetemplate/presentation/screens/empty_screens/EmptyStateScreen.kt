package com.example.composetemplate.presentation.screens.empty_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composetemplate.R
import com.example.composetemplate.data.models.local_models.ErrorType
import com.example.composetemplate.managers.StringsKeyManager.EMPTY_STATE_NO_LECTURES_MESSAGE
import com.example.composetemplate.managers.StringsKeyManager.EMPTY_STATE_NO_LECTURES_TITLE
import com.example.composetemplate.managers.StringsKeyManager.EMPTY_STATE_NO_NETWORK_MESSAGE
import com.example.composetemplate.managers.StringsKeyManager.EMPTY_STATE_NO_NETWORK_TITLE
import com.example.composetemplate.managers.StringsKeyManager.EMPTY_STATE_PERMISSION_DENIED_MESSAGE
import com.example.composetemplate.managers.StringsKeyManager.EMPTY_STATE_PERMISSION_DENIED_TITLE
import com.example.composetemplate.managers.StringsKeyManager.EMPTY_STATE_SERVER_ERROR_MESSAGE
import com.example.composetemplate.managers.StringsKeyManager.EMPTY_STATE_SERVER_ERROR_TITLE
import com.example.composetemplate.ui.theme.CustomTheme
import com.example.composetemplate.ui.theme.CustomTheme.colors
import com.example.composetemplate.utils.Constants.Companion.EMPTY_STATE_ACCESS_DENIED_SYNOPSIS
import com.example.composetemplate.utils.Constants.Companion.EMPTY_STATE_ACCESS_DENIED_TITLE
import com.example.composetemplate.utils.Constants.Companion.EMPTY_STATE_GENERAL_BUG_SYNOPSIS
import com.example.composetemplate.utils.Constants.Companion.EMPTY_STATE_GENERAL_BUG_TITLE
import com.example.composetemplate.utils.Constants.Companion.EMPTY_STATE_NO_INTERNET_SYNOPSIS
import com.example.composetemplate.utils.Constants.Companion.EMPTY_STATE_NO_INTERNET_TITLE
import com.example.composetemplate.utils.Constants.Companion.EMPTY_STATE_NO_LECTURE_SYNOPSIS
import com.example.composetemplate.utils.Constants.Companion.EMPTY_STATE_NO_LECTURE_TITLE
import com.example.composetemplate.utils.StringProvider
import com.example.composetemplate.utils.exceptions.NoDataException
import com.example.composetemplate.utils.exceptions.NoInternetConnectionException
import com.example.composetemplate.utils.exceptions.UnauthorizedException
import java.lang.Exception

/**
 * Composable function for displaying an empty state screen based on an exception.
 *
 * @param modifier Modifier for customizing the layout of the empty state.
 * @param exception An optional exception that is used to display a specific empty state screen.
 *
 * Displays different empty state screens based on the type of exception:
 * - `UnauthorizedException`: Shows an "Access Denied" screen.
 * - `null`: Shows a "No Data" screen when there are no Data available.
 * - `Other exceptions`: Shows a "General bug" screen for any other errors.
 */

@Composable
fun EmptyStateScreen(
    modifier: Modifier = Modifier,
    errorType: ErrorType?,
) {
    when (errorType) {
        ErrorType.UNAUTHORIZED -> {
            EmptyListScreen(
                modifier = modifier,
                drawableResource = R.drawable.access_denied,
                title = StringProvider.getString(EMPTY_STATE_PERMISSION_DENIED_TITLE),
                synopsis = StringProvider.getString(EMPTY_STATE_PERMISSION_DENIED_MESSAGE)
            )
        }
        ErrorType.NETWORK_ERROR -> {
            EmptyListScreen(
                modifier = modifier,
                drawableResource = R.drawable.no_signal,
                title = StringProvider.getString(EMPTY_STATE_NO_NETWORK_TITLE),
                synopsis = StringProvider.getString(EMPTY_STATE_NO_NETWORK_MESSAGE)
            )
        }
        /* Display a default empty list screen if there's a parse exception or no data */
        ErrorType.NO_DATA -> {
            EmptyListScreen(
                modifier = modifier,
                drawableResource = R.drawable.empty_list,
                title = StringProvider.getString(EMPTY_STATE_NO_LECTURES_TITLE),
                synopsis = StringProvider.getString(EMPTY_STATE_NO_LECTURES_MESSAGE)
            )
        }
        else -> {
            EmptyListScreen(
                modifier = modifier,
                drawableResource = R.drawable.general_bug,
                title = StringProvider.getString(EMPTY_STATE_SERVER_ERROR_TITLE),
                synopsis = StringProvider.getString(EMPTY_STATE_SERVER_ERROR_MESSAGE)
            )
        }
    }
}

/**
 * Composable function for displaying a customizable empty list screen.
 *
 * @param modifier Modifier for styling this composable.
 * @param drawableResource Resource ID for the image displayed at the top of the screen.
 * @param title The main title text displayed in the empty state.
 * @param synopsis The descriptive text displayed below the title in the empty state.
 *
 * Layout:
 * - A top title labeled "OnO Lectures"
 * - An image occupying around 45% of the screen height
 * - A main title text
 * - A synopsis text
 */
@Composable
fun EmptyListScreen(
    modifier: Modifier = Modifier,
    drawableResource: Int,
    title: String,
    synopsis: String
) {
    Column(
        modifier = modifier
            .background(colors.loginScreen)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "OnO Lectures",
            style = CustomTheme.typography.getLecturesEmptyStateTitleStyle(),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        Spacer(modifier = modifier.height(24.dp))

        Image(
            painter = painterResource(id = drawableResource), // Replace with your image resource
            contentDescription = "Empty state image",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .padding(bottom = 16.dp)
        )

        Spacer(modifier = modifier.height(16.dp))

        Text(
            text = title,
            style = CustomTheme.typography.getLectureTopicStyle(),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = synopsis,
            style = CustomTheme.typography.getEmptyStateSynopsisStyle(),
            textAlign = TextAlign.Center
        )
    }
}
