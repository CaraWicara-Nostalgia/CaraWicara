package com.karina.carawicara.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonNav
import com.karina.carawicara.ui.component.CardHome

@Composable
fun HomePage(
    navController: NavController
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp)
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                ButtonNav(
                    onClick = { /* Handle click here */ },
                    icon = R.drawable.ic_gender_male,
                    iconColor = Color.White.toArgb(),
                    borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                    backgroundColor = MaterialTheme.colorScheme.primary.toArgb(),
                    enabled = true
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    modifier = Modifier.size(130.dp, 24.dp),
                    painter = painterResource(id = R.drawable.logo_color),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.weight(1f))
                ButtonNav(
                    onClick = { navController.navigate("pustakaWicaraPage")},
                    icon = R.drawable.ic_library,
                    iconColor = Color.White.toArgb(),
                    borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                    backgroundColor = MaterialTheme.colorScheme.primary.toArgb(),
                    enabled = true
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                item {
                    Column {
                        CardHome(
                            image = R.drawable.card_home_1,
                            onClick = { navController.navigate("paduGambarPage") }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CardHome(
                            image = R.drawable.card_home_4,
                            onClick = { navController.navigate("kenaliAkuPage/Coba tirukan gerakan wajah seperti contoh diatas!")}
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CardHome(
                            image = R.drawable.card_home_2,
                            onClick = { navController.navigate("susunKataPage/0") }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CardHome(
                            image = R.drawable.card_home_3,
                            onClick = { navController.navigate("suaraPintarPage")}
                        )
                    }
                }
            }
        }
    }
}