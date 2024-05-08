package com.karina.carawicara.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.karina.carawicara.R

@Composable
fun ButtonCategoryLibrary(
    onClick: () -> Unit,
    image: Int,
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(2.dp, color = Color.DarkGray, shape = RoundedCornerShape(8.dp))
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = image),
                modifier = Modifier.clip(RoundedCornerShape(2.dp)),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun ButtonCategoryLibraryPreview() {
    ButtonCategoryLibrary(
        onClick = { /* Handle click here */ },
        image = R.drawable.umum
    )
}