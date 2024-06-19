package com.karina.carawicara.ui.screen.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonGender
import com.karina.carawicara.ui.component.ButtonNav

@Composable
fun OnBoardingPage(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val isMaleButtonClicked = remember {
        mutableStateOf(false)
    }
    val isFemaleButtonClicked = remember {
        mutableStateOf(false)
    }

    Box (
        modifier = modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.onboardTitle),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(id = R.string.onboardDescription),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(64.dp)
            ) {
                ButtonGender(
                    onClick = {
                        isMaleButtonClicked.value = true
                        isFemaleButtonClicked.value = false
                    },
                    image = R.drawable.child_boy,
                    text = "Laki-laki"
                )
                Spacer(modifier = Modifier.height(32.dp))
                ButtonGender(
                    onClick = {
                        isFemaleButtonClicked.value = true
                        isMaleButtonClicked.value = false
                    },
                    image = R.drawable.girl,
                    text = "Perempuan"
                )
            }

            val buttonNavBackgroundColor = when {
                isMaleButtonClicked.value -> MaterialTheme.colorScheme.primary
                isFemaleButtonClicked.value -> MaterialTheme.colorScheme.secondary
                else -> Color.LightGray
            }

            val buttonNavBorderColor = when {
                isMaleButtonClicked.value -> MaterialTheme.colorScheme.primaryContainer
                isFemaleButtonClicked.value -> MaterialTheme.colorScheme.secondaryContainer
                else -> Color.LightGray
            }

            ButtonNav(
                onClick = { navHostController.navigate("homePage") },
                icon = R.drawable.ic_arrow_forward,
                iconColor = Color.White.toArgb(),
                borderColor = buttonNavBorderColor.toArgb(),
                backgroundColor = buttonNavBackgroundColor.toArgb(),
                enabled = isMaleButtonClicked.value || isFemaleButtonClicked.value
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}