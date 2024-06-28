package com.karina.carawicara.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karina.carawicara.R

@Composable
fun ButtonImage(
    onClick: () -> Unit,
    image: Int,
    text: String,
    border: Int,
    background: Int
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(146.dp, 117.dp)
            .clickable(onClick = onClick)
            .border(2.dp, color = Color(border), shape = RoundedCornerShape(12.dp))
            .background(color = Color(background), shape = RoundedCornerShape(12.dp))
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(4.dp)
        ){
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .size(114.dp, 60.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
            Text(
                text = text,
                color = Color.Black,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Preview
@Composable
fun ButtonImagePreview() {
    ButtonImage(
        onClick = { /* Handle click here */ },
        image = R.drawable.kucing,
        text = "Kucing",
        border = Color.Gray.toArgb(),
        background = Color.White.toArgb()
    )
}