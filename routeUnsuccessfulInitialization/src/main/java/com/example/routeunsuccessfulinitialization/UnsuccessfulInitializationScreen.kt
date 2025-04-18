package com.example.routeunsuccessfulinitialization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.sharedui.dimensions.Padding
import com.example.sharedui.sharedUiElements.text.guidanceText
import com.example.sharedui.theme.Background
import com.example.sharedui.R
import com.example.sharedui.sharedUiElements.label.primaryCenteredLabel

@Composable
internal fun unsuccessfulInitializationScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(Padding.P20)
            .testTag("UnsuccessfulAuthenticationScreen"),

        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,

        content = {

            // upper part
            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),

                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                content = {


                    guidanceText(
                        content = stringResource(R.string.whoops)
                    )
                }
            )

            // lower part
            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),

                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                content = {

                    primaryCenteredLabel(
                        content = stringResource(R.string.initialization_process_error_information)
                    )
                }
            )
        }
    )
}