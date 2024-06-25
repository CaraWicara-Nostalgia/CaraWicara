package com.karina.carawicara.ui.component

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.karina.carawicara.R

@Composable
fun ButtonSpeaker(
    onClick: () -> Unit,
    iconColor: Int,
    borderColor: Int,
    backgroundColor: Int,
    enabled: Boolean,
    mediaPlayer: MediaPlayer
) {
    Box(
        modifier = Modifier
            .clickable(onClick = {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.release()
                } else {
                    mediaPlayer.start()
                }
            }, enabled = enabled)
            .border(2.dp, color = Color(borderColor), shape = RoundedCornerShape(12.dp))
            .background(
                color = if (enabled) Color(backgroundColor) else Color.Gray,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_speaker),
                contentDescription = null,
                tint = Color(iconColor),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}