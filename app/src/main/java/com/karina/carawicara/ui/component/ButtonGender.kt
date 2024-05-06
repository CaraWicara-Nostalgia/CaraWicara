package com.karina.carawicara.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.karina.carawicara.R

@Composable
fun ButtonGender (
    onClick: () -> Unit,
    image: Int,
    text: String,
){
    val isClicked = remember {
        mutableStateOf(false)
    }

    val borderColor = when (text) {
        "Laki-laki" -> MaterialTheme.colorScheme.primary
        "Perempuan" -> MaterialTheme.colorScheme.secondary
        else -> Color.Gray
    }

    Button(
        onClick = {
            isClicked.value = !isClicked.value
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = if (isClicked.value)
            Modifier.border(4.dp, borderColor, shape = RoundedCornerShape(16.dp))
        else
            Modifier.border(1.dp, borderColor, shape = RoundedCornerShape(16.dp))
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ){
                Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    alignment = Alignment.BottomEnd,
                    modifier = Modifier.size(72.dp)
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(end = 32.dp, start = 32.dp, top = 16.dp, bottom = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ButtonGenderPreview() {
    ButtonGender(
        onClick = {},
        image = R.drawable.child_boy,
        text = stringResource(R.string.genderBoy)
    )
}