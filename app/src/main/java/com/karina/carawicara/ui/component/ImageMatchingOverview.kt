package com.karina.carawicara.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ImageMatchingOverview(
    onClick: () -> Unit,
    border: Int,
    background: Int,
    text: String,
    image: Int,
    message: String,
) {
    Column (
        verticalArrangement = Arrangement.Center,
    ){
        Image(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .size(375.dp, 128.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Column (
                modifier = Modifier.align(Alignment.Center)
            ){
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onClick() },
                    Modifier
                        .size(311.dp, 47.dp)
                        .border(2.dp, color = Color(border), shape = RoundedCornerShape(12.dp))
                        .background(color = Color(background), shape = RoundedCornerShape(12.dp))
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(12.dp),
                    content = {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                )
            }
        }
    }
}