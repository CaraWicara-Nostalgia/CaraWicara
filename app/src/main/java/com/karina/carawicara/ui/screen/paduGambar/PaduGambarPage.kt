package com.karina.carawicara.ui.screen.paduGambar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonImage
import com.karina.carawicara.ui.component.ButtonNav
import com.karina.carawicara.ui.component.PopupOverview
import com.karina.carawicara.ui.component.ImageSound
import com.karina.carawicara.ui.component.StageBox

@Composable
fun PaduGambarPage(
    navController: NavHostController
){
    val isKucingClicked = remember { mutableStateOf(false) }
    val isKelinciClicked = remember { mutableStateOf(false) }

    val stageBoxStatus = remember {
        mutableStateOf(0)
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Column (
            modifier = Modifier
                .padding(31.dp)
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ){
                ButtonNav(
                    onClick = { navController.popBackStack() },
                    icon = R.drawable.ic_x,
                    iconColor = Color.White.toArgb(),
                    borderColor = MaterialTheme.colorScheme.errorContainer.toArgb(),
                    backgroundColor = MaterialTheme.colorScheme.error.toArgb(),
                    enabled = true
                )
                Row (
                    modifier = Modifier.padding(8.dp)
                ){
                    StageBox(stage = stageBoxStatus.value, onClick = { /* Handle click here */ })
                    StageBox(stage = 0, onClick = { /* Handle click here */ })
                    StageBox(stage = 0, onClick = { /* Handle click here */ })
                    StageBox(stage = 0, onClick = { /* Handle click here */ })
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            ImageSound(
                onClick = { /*TODO*/ },
                image = R.drawable.kucing_2
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                Column {
                    ButtonImage(
                        onClick = { isKucingClicked.value = true },
                        image = R.drawable.kucing,
                        text = "Kucing",
                        border = Color.Gray.toArgb(),
                        background = if (isKucingClicked.value) Color.Green.toArgb() else Color.White.toArgb()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    ButtonImage(
                        onClick = { /*TODO*/ },
                        image = R.drawable.anjing,
                        text = "Anjing",
                        border = Color.Gray.toArgb(),
                        background = Color.White.toArgb()
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    ButtonImage(
                        onClick = { /*TODO*/ },
                        image = R.drawable.kuda,
                        text = "Kuda",
                        border = Color.Gray.toArgb(),
                        background = Color.White.toArgb()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    ButtonImage(
                        onClick = { isKelinciClicked.value = true},
                        image = R.drawable.kelinci,
                        text = "Kelinci",
                        border = Color.Gray.toArgb(),
                        background = if (isKelinciClicked.value) Color.Red.toArgb() else Color.White.toArgb()
                    )
                }
            }
            if (isKucingClicked.value) {
                Dialog(
                    onDismissRequest = { isKucingClicked.value = false }
                ) {
                    PopupOverview(
                        onClick = {
                            stageBoxStatus.value = 1
                            isKucingClicked.value = false
                        },
                        border = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                        background = MaterialTheme.colorScheme.primary.toArgb(),
                        text = "Yey, kamu berhasil menjawab dengan benar",
                        image = R.drawable.boy_2,
                        message = "Lanjutkan"
                    )
                }
            }

            if (isKelinciClicked.value) {
                Dialog(
                    onDismissRequest = { isKelinciClicked.value = false }
                ) {
                    PopupOverview(
                        onClick = { isKelinciClicked.value = false },
                        border = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                        background = MaterialTheme.colorScheme.primary.toArgb(),
                        text = "Yah, kamu belum berhasil menjawab dengan benar",
                        image = R.drawable.boy_4,
                        message = "Coba lagi"
                    )
                }
            }
        }
    }
}