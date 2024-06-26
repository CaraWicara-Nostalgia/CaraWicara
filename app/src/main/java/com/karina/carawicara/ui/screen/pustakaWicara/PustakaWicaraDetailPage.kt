package com.karina.carawicara.ui.screen.pustakaWicara

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.karina.carawicara.R
import com.karina.carawicara.data.letterSounds
import com.karina.carawicara.data.libraryData
import com.karina.carawicara.ui.component.ButtonNav
import com.karina.carawicara.ui.component.ButtonSpeaker
import com.karina.carawicara.ui.component.ImageLibrary

@Composable
fun PustakaWicaraDetailPage(navController: NavHostController, selectedLetter: String) {
    val openDialog = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val items = libraryData[selectedLetter] ?: emptyList()
    val letterSound = letterSounds[selectedLetter] ?: R.raw.sound_pustaka_a

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
                    backgroundColor = Color.White.toArgb(),
                    enabled = true
                )
                Spacer(modifier = Modifier.weight(1f))
                ButtonNav(
                    onClick = { openDialog.value = true },
                    icon = R.drawable.ic_filter,
                    iconColor = Color.White.toArgb(),
                    borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                    backgroundColor = MaterialTheme.colorScheme.primary.toArgb(),
                    enabled = true
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Text(
                            text = selectedLetter,
                            fontSize = 128.sp,
                            color = Color.Black,
                        )
                        Text(
                            text = selectedLetter,
                            fontSize = 64.sp,
                            color = Color.Black,
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    ButtonSpeaker(
                        onClick = { },
                        iconColor = Color.White.toArgb(),
                        borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                        backgroundColor = MaterialTheme.colorScheme.primary.toArgb(),
                        enabled = true,
                        mediaPlayer = MediaPlayer.create(context, letterSound)
                    )
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(32.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(items) { item ->
                        ImageLibrary(
                            onClick = { },
                            image = item.image,
                            text = item.text,
                            homonym = item.homonym,
                            mediaPlayer = MediaPlayer.create(context, item.sound)
                        )
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