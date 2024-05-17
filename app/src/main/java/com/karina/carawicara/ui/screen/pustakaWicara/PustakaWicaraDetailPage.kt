package com.karina.carawicara.ui.screen.pustakaWicara

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonNav
import com.karina.carawicara.ui.component.ImageLibrary

@Composable
fun PustakaWicaraDetailPage(navController: NavHostController) {
    val openDialog = remember {
        mutableStateOf(false)
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth()
            ) {
                ButtonNav(
                    onClick = { navController.popBackStack() },
                    icon = R.drawable.ic_arrow_back,
                    iconColor = Color.Black.toArgb(),
                    borderColor = Color.DarkGray.toArgb(),
                    backgroundColor = Color.White.toArgb()
                )
                Spacer(modifier = Modifier.weight(1f))
                ButtonNav(
                    onClick = { openDialog.value = true },
                    icon = R.drawable.ic_filter,
                    iconColor = Color.White.toArgb(),
                    borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                    backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                )
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Row (
                        verticalAlignment = Alignment.Bottom,
                    ){
                        Text(
                            text = "A",
                            fontSize = 128.sp,
                            color = Color.Black,
                        )
                        Text(
                            text = "A",
                            fontSize = 64.sp,
                            color = Color.Black,
                        )
                    }
                    Spacer(modifier = Modifier.height(48.dp))
                    ButtonNav(
                        onClick = { /*TODO*/ },
                        icon = R.drawable.ic_speaker,
                        iconColor = Color.White.toArgb(),
                        borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                        backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                    )
                }
                LazyColumn {
                    item {
                        Column(
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                ImageLibrary(
                                    onClick = { /*TODO*/ },
                                    image = R.drawable.air,
                                    text = "Air",
                                    homonym = "a.ir"
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                ImageLibrary(
                                    onClick = { /*TODO*/ },
                                    image = R.drawable.api,
                                    text = "Api",
                                    homonym = "a.pi"
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                ImageLibrary(
                                    onClick = { /*TODO*/ },
                                    image = R.drawable.aku,
                                    text = "Aku",
                                    homonym = "a.ku"
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                ImageLibrary(
                                    onClick = { /*TODO*/ },
                                    image = R.drawable.asap,
                                    text = "A",
                                    homonym = "a.sap"
                                )
                            }
                        }
                    }
                }
            }
        }
        if (openDialog.value) {
            Dialog(
                onDismissRequest = { openDialog.value = false }
            ) {
                FilterDialog(
                    image1 = R.drawable.umum,
                    image2 = R.drawable.hewan,
                    image3 = R.drawable.tumbuhan
                )
            }
        }
    }
}