package com.karina.carawicara.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonNav(
    onClick: () -> Unit,
    icon: Int,
    iconColor: Int,
    text: String
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(2.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp))
    ) {
            Row(
                modifier = Modifier
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = text,
                    color = Color.Black,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Color(iconColor),
                    modifier = Modifier.size(24.dp)
                )
            }

    }
}

