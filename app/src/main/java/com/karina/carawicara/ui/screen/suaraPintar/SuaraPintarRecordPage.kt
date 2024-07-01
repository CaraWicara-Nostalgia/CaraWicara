package com.karina.carawicara.ui.screen.suaraPintar

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonNav
import com.karina.carawicara.ui.component.PopupOverview
import com.karina.carawicara.ui.component.StageBox

@Composable
fun SuaraPintarRecordPage(navHostController: NavHostController) {
    val backgroundColor = remember { mutableStateOf(Color.White) }
    val sendButtonClicked = remember { mutableStateOf(false) }
    val stageBoxStatus = remember { mutableStateOf(0) }
    val isPlaying = remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("audio_animation.json"))

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
                    onClick = { navHostController.navigate("homePage") },
                    icon = R.drawable.ic_x,
                    iconColor = Color.White.toArgb(),
                    borderColor = MaterialTheme.colorScheme.errorContainer.toArgb(),
                    backgroundColor = MaterialTheme.colorScheme.error.toArgb(),
                    enabled = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                repeat(4) {
                    StageBox(stage = 0, onClick = { /* Handle click here */ })
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color(0xFFFEEFD4))
                    .border(2.dp, Color(0xFFFEE4B7))
                    .padding(4.dp)
            ) {
                Text(
                    text = "Tahan tombol rekam untuk mulai merekam suara",
                    fontSize = 20.sp,
                    color = Color(0xFFFCB028),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                LottieAnimation(
                    composition = composition,
                    isPlaying = isPlaying.value,
                    iterations = Int.MAX_VALUE, // Loop terus menerus
                    modifier = Modifier
                        .size(200.dp) // Sesuaikan ukuran sesuai kebutuhan Anda
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Nama hewan tadi adalah ...",
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(36.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ButtonNav(
                        onClick = {
                            isPlaying.value = true // Mulai animasi Lottie ketika tombol diklik
                        },
                        icon = R.drawable.ic_mic,
                        iconColor = Color.White.toArgb(),
                        borderColor = MaterialTheme.colorScheme.errorContainer.toArgb(),
                        backgroundColor = MaterialTheme.colorScheme.error.toArgb(),
                        enabled = true
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Button(
                        onClick = {
                            sendButtonClicked.value = true
                        },
                        Modifier
                            .size(239.dp, 47.dp)
                            .border(
                                2.dp,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        content = {
                            Text(
                                text = "Kirim",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    )
                }
            }
            if (sendButtonClicked.value) {
                Dialog(
                    onDismissRequest = {
                        sendButtonClicked.value = false
                        isPlaying.value = false // Stop animasi Lottie ketika dialog ditutup
                    }
                ) {
                    PopupOverview(
                        onClick = {
                            stageBoxStatus.value = 0
                            sendButtonClicked.value = false
                            isPlaying.value = false // Stop animasi Lottie ketika dialog ditutup
                        },
                        border = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                        background = MaterialTheme.colorScheme.primary.toArgb(),
                        text = "Yey, kamu berhasil menjawab dengan benar",
                        image = R.drawable.boy_2,
                        message = "Lanjutkan"
                    )
                }
            }
        }
    }
}
