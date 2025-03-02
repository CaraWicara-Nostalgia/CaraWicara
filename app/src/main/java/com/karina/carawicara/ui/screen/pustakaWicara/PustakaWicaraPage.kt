package com.karina.carawicara.ui.screen.pustakaWicara

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonAlphabetLibrary

@Composable
fun PustakaWicaraPage(navController: NavHostController) {
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
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Kembali",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                navController.popBackStack()
                            }
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Kamusku",
                        fontSize = 24.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp) // tambahkan padding jika diperlukan
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.boy_3),
                        contentDescription = null,
                        modifier = Modifier.size(84.dp)
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    AlphabetRow(
                        navController = navController,
                        alphabets = listOf("A", "B", "C")
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    AlphabetRow(
                        navController = navController,
                        alphabets = listOf("D", "E", "F")
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    AlphabetRow(
                        navController = navController,
                        alphabets = listOf("G", "H", "I")
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    AlphabetRow(
                        navController = navController,
                        alphabets = listOf("J", "K", "L")
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    AlphabetRow(
                        navController = navController,
                        alphabets = listOf("M", "N", "O")
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    AlphabetRow(
                        navController = navController,
                        alphabets = listOf("P", "Q", "R")
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    AlphabetRow(
                        navController = navController,
                        alphabets = listOf("S", "T", "U")
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    AlphabetRow(
                        navController = navController,
                        alphabets = listOf("V", "W", "X")
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    AlphabetRow(
                        navController = navController,
                        alphabets = listOf("Y", "Z")
                    )
                }
            }
        }
    }
}

@Composable
fun AlphabetRow(navController: NavHostController, alphabets: List<String>) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        alphabets.forEach { alphabet ->
            ButtonAlphabetLibrary(
                onClick = { navController.navigate("pustakaWicaraDetailPage/$alphabet") },
                alphabet = alphabet,
                isSelected = false,
                borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                backgroundColor = MaterialTheme.colorScheme.primary.toArgb()
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}