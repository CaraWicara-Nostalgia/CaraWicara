package com.karina.carawicara.ui.screen.suaraPintar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonNav
import com.karina.carawicara.ui.component.StageBox

@Composable
fun SuaraPintarPage(
    image: Int,
    navHostController: NavHostController
) {
    val backgroundColor = remember { mutableStateOf(Color.White) }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(31.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ButtonNav(
                    onClick = { navHostController.popBackStack() },
                    icon = R.drawable.ic_x,
                    iconColor = Color.White.toArgb(),
                    borderColor = MaterialTheme.colorScheme.errorContainer.toArgb(),
                    backgroundColor = MaterialTheme.colorScheme.error.toArgb(),
                    enabled = true
                )
                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                }
                StageBox(stage = 0, onClick = { /* Handle click here */ })
                StageBox(stage = 0, onClick = { /* Handle click here */ })
                StageBox(stage = 0, onClick = { /* Handle click here */ })
                StageBox(stage = 0, onClick = { /* Handle click here */ })
            }
//            Spacer(modifier = Modifier.weight(1f))
//            CountdownTimer(
//                durationMillis = 10000,
//                onTimerFinished = { /* Handle timer finished */ },
//                onTwoSecondsLeft = { backgroundColor.value = Color.Red }
//            )
            Spacer(modifier = Modifier.weight(1f))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(311.dp, 332.dp)
                        .border(
                            2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(image),
                        contentDescription = null,
                        modifier = Modifier
                            .size(279.dp, 300.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Ayo tebak nama hewan ini!",
                    fontSize = 24.sp,
                    color = Color(MaterialTheme.colorScheme.primary.toArgb())
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            ButtonNav(
                onClick = { navHostController.navigate("suaraPintarRecordPage") },
                icon = R.drawable.ic_arrow_forward,
                iconColor = Color.Black.toArgb(),
                borderColor = Color.DarkGray.toArgb(),
                backgroundColor = Color.White.toArgb(),
                enabled = true
            )
        }
    }
}