package com.karina.carawicara.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.karina.carawicara.R

@Composable
fun ImageSound(
    onClick: () -> Unit,
    image: Int,
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
    ){
        Image(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier
                .size(279.dp, 300.dp)
                .clip(RoundedCornerShape(6.dp))
        )
        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(24.dp)
        ){
            ButtonNav(
                onClick = { /*TODO*/ },
                icon = R.drawable.ic_speaker,
                iconColor = Color.White.toArgb(),

                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
            )
        }
    }
}