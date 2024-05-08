package com.karina.carawicara.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StageBox(
    stage: Int,
    onClick: () -> Unit
) {
    val colors = listOf(Color.LightGray, Color.Yellow, Color.Green, Color.Blue, Color.Red)
    Box(
        modifier = Modifier
            .size(55.dp, 24.dp)
            .padding(4.dp)
            .clickable(onClick = onClick)
            .border(1.dp, Color.Black, RoundedCornerShape(4.dp)) // Menambahkan border di sini
            .background(color = colors[stage.coerceIn(colors.indices)], shape = RoundedCornerShape(4.dp))
    )
}

@Preview
@Composable
fun StageBoxPreview() {
    StageBox(stage = 0, onClick = { /* Handle click here */ })
}