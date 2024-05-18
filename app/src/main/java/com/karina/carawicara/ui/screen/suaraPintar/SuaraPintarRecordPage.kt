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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonNav
import com.karina.carawicara.ui.component.StageBox

@Composable
fun SuaraPintarRecordPage(
    navHostController: NavHostController
) {
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
            Spacer(modifier = Modifier.weight(1f))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color(0xFFFEEFD4))
                    .border(2.dp, Color(0xFFFEE4B7))
                    .size(311.dp, 43.dp) // Add this line to give your Box a size
            ) {
                Text(
                    text = "tahan tombol rekam untuk mulai merekam suara",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFCB028),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Nama hewan tadi adalah ...",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(36.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ButtonNav(
                        onClick = { /*TODO*/ },
                        icon = R.drawable.ic_mic,
                        iconColor = Color.White.toArgb(),
                        borderColor = MaterialTheme.colorScheme.errorContainer.toArgb(),
                        backgroundColor = MaterialTheme.colorScheme.error.toArgb(),
                        enabled = true
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Button(
                        onClick = { /*TODO*/ },
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
        }
    }
}