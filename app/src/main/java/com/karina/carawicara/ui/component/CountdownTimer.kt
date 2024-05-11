package com.karina.carawicara.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CountdownTimer(
    modifier: Modifier = Modifier,
    durationMillis: Long,
    onTimerFinished: () -> Unit,
    onTwoSecondsLeft: () -> Unit
) {
    val timeLeft = remember { mutableStateOf(durationMillis) }
    val timer = rememberCoroutineScope()

    LaunchedEffect(key1 = durationMillis) {
        timer.launch {
            while (timeLeft.value > 0) {
                delay(1000)
                timeLeft.value -= 1000
                if (timeLeft.value <= 2000) {
                    onTwoSecondsLeft()
                }
            }
            onTimerFinished()
        }
    }

    val minutes = timeLeft.value / 60000
    val seconds = (timeLeft.value % 60000) / 1000

    Text(
        text = String.format("%02d:%02d", minutes, seconds),
        modifier = modifier
            .background(Color.Yellow, shape = RoundedCornerShape(8.dp)) // Add background color
            .clip(RoundedCornerShape(8.dp)) // Add rounded corners
            .padding(horizontal = 8.dp, vertical = 4.dp), // Add padding
        fontSize = 24.sp,
        color = Color.Black
    )
}

@Preview
@Composable
fun CountdownTimerPreview() {
    CountdownTimer(
        durationMillis = 5000,
        onTimerFinished = { /* Handle timer finished */ },
        onTwoSecondsLeft = { /* Handle two seconds left */ }
    )
}
