package com.karina.carawicara.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karina.carawicara.R

@Composable
fun CardHome(
    image: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
            .size(311.dp, 220.dp),
    ) {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = image),
                contentDescription = null
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        for (i in 1..4) {
                            Image(
                                painter = painterResource(id = R.drawable.round_home), // Placeholder image, replace with actual images
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.Gray)
                                    .padding(0.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "299+",
                            fontSize = 10.sp,
                            color = Color.Green
                        )
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "299 Lebih Pengguna Berhasil Mengerjakan",
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                ButtonNav(
                    onClick = { onClick() },
                    icon = R.drawable.ic_arrow_forward,
                    iconColor = Color.White.toArgb(),
                    borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                    backgroundColor = MaterialTheme.colorScheme.primary.toArgb(),
                    enabled = true
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
fun CardHomePreview(){
    CardHome(
        image = R.drawable.card_home_1,
        onClick = { /* Handle click here */ }
    )
}