package com.karina.carawicara.ui.component

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karina.carawicara.R

@Composable
fun ImageLibrary(
    onClick: () -> Unit,
    image: Int,
    text: String,
    homonym: String,
    mediaPlayer: MediaPlayer
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(160.dp, 180.dp)
            .clickable(onClick = onClick)
            .border(2.dp, color = Color.DarkGray, shape = RoundedCornerShape(12.dp))
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
        ){
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .size(114.dp, 60.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Column {
                    Text(
                        text = text,
                        color = Color.Black,
                        fontSize = 20.dp.value.sp,
                    )
                    Text(
                        text = homonym,
                        color = Color.LightGray,
                        fontSize = 20.dp.value.sp,
                    )
                }
                Box(
                    modifier = Modifier
                        .clickable(onClick = onClick)
                        .border(2.dp, color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(8.dp))
                        .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(0.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ButtonSpeaker(
                            onClick = {
                                if (mediaPlayer.isPlaying) {
                                    mediaPlayer.stop()
                                    mediaPlayer.prepare() // Prepare the MediaPlayer to be used again
                                } else {
                                    mediaPlayer.start()
                                }
                            },
                            iconColor = Color.White.toArgb(),
                            borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                            backgroundColor = MaterialTheme.colorScheme.primary.toArgb(),
                            enabled = true,
                            mediaPlayer = mediaPlayer
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ImageLibraryPreview() {
    ImageLibrary(
        onClick = { /* Handle click here */ },
        image = R.drawable.kucing,
        text = "A",
        homonym = "a.ir",
        mediaPlayer = MediaPlayer.create(LocalContext.current, R.raw.sound_pustaka_a)
    )
}