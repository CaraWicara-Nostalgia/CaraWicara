package com.karina.carawicara.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonAlphabet(
    onClick: () -> Unit,
    alphabet: String,
    isSelected: Boolean,
    borderColor: Int,
    backgroundColor: Int
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(2.dp, color = Color(borderColor), shape = RoundedCornerShape(12.dp))
            .background(color = Color(backgroundColor), shape = RoundedCornerShape(12.dp))
            .size(93.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = alphabet,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontSize = 48.sp
            )
        }
    }
}

@Preview
@Composable
fun ButtonAlphabetLibraryPreview() {
    ButtonAlphabet(
        onClick = { /* Handle click here */ },
        alphabet = "A",
        isSelected = false,
        borderColor = Color.DarkGray.toArgb(),
        backgroundColor = Color.White.toArgb()
    )
}