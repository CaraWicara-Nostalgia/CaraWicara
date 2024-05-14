package com.karina.carawicara.ui.screen.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonAlphabetLibrary
import com.karina.carawicara.ui.component.ButtonNav

@Composable
fun LibraryPage(navController: NavHostController) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Column {
            Row {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth()
                ) {
                    ButtonNav(
                        onClick = { navController.popBackStack() },
                        icon = R.drawable.ic_arrow_back,
                        iconColor = Color.Black.toArgb(),
                        borderColor = Color.DarkGray.toArgb(),
                        backgroundColor = Color.White.toArgb()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Kamusku",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp) // tambahkan padding jika diperlukan
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.boy_3),
                        contentDescription = null
                    )
                }
            }
            LazyColumn {
                item{
                    Column (
                        modifier = Modifier.padding(32.dp)
                    ){
                        Row (
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            ButtonAlphabetLibrary(
                                onClick = { navController.navigate("libraryDetailPage") },
                                alphabet = "A",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "B",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "C",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row (
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "D",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "E",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "F",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row (
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "G",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "H",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "I",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row (
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "J",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "K",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "L",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row (
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "M",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "N",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "O",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row (
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "P",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "Q",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "R",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row (
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "S",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "T",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "U",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row (
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "V",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "W",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "X",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row (
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "Y",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            ButtonAlphabetLibrary(
                                onClick = { /*TODO*/ },
                                alphabet = "Z",
                                isSelected = false,
                                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
                            )
                        }
                    }
                }
            }
        }
    }
}