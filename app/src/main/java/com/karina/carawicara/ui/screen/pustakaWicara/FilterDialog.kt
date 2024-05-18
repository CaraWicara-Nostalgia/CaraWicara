package com.karina.carawicara.ui.screen.pustakaWicara

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonCategoryLibrary

@Composable
fun FilterDialog(
    image1: Int,
    image2: Int,
    image3: Int,
) {
    Column (
        horizontalAlignment = Alignment.End,
    ){
        Icon(painter = painterResource(id = R.drawable.ic_x), contentDescription = null)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color.White)
                .border(2.dp, Color.DarkGray)
                .size(269.dp, 195.dp)
        ){
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(
                    text = "Pilih Kategori",
                    fontSize = 24.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ButtonCategoryLibrary(
                            onClick = { /*TODO*/ },
                            image = image1
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Umum",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ButtonCategoryLibrary(
                            onClick = { /*TODO*/ },
                            image = image2
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Hewan",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ButtonCategoryLibrary(
                            onClick = { /*TODO*/ },
                            image = image3
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tumbuhan",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun FilterDialogPreview() {
    FilterDialog(
        image1 = R.drawable.umum,
        image2 = R.drawable.hewan,
        image3 = R.drawable.tumbuhan
    )
}