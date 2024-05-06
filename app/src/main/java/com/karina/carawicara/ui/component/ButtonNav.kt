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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.karina.carawicara.R

@Composable
fun ButtonNav(
    onClick: () -> Unit,
    icon: Int,
    iconColor: Int,
    borderColor: Int,
    backgroundColor: Int,
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(2.dp, color = Color(borderColor), shape = RoundedCornerShape(12.dp))
            .background(color = Color(backgroundColor), shape = RoundedCornerShape(12.dp))
            .alpha(0.4f)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Color(iconColor),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun ButtonNavPreview() {
    ButtonNav(
        onClick = { /* Handle click here */ },
        icon = R.drawable.ic_arrow_forward,
        iconColor = Color.White.toArgb(),
        borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
        backgroundColor = MaterialTheme.colorScheme.primary.toArgb(),
    )
}

