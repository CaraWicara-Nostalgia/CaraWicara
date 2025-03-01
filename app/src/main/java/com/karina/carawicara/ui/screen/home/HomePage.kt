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
                Image(
                    modifier = Modifier.size(130.dp, 24.dp),
                    painter = painterResource(id = R.drawable.logo_color),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.weight(1f))
                ButtonNav(
                    onClick = { navController.navigate("pustakaWicaraPage")},
                    icon = R.drawable.ic_library,
                    iconColor = MaterialTheme.colorScheme.primary.toArgb(),
                    enabled = true
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                item {
                    Column {
                        CardHome(
                            onClick = { navController.navigate("paduGambarPage") },
                            title = "Padu Gambar",
                            description = "Mencocokkan gambar yang sesuai"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CardHome(
                            onClick = { navController.navigate("kenaliAkuPage/Coba tirukan gerakan wajah seperti contoh diatas!") },
                            title = "Kenali Aku",
                            description = "Mengekspresikan suasana hati"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CardHome(
                            onClick = { navController.navigate("susunKataPage/0") },
                            title = "Susun Kata",
                            description = "Merangkai kata menjadi kalimat"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CardHome(
                            onClick = { navController.navigate("suaraPintarPage") },
                            title = "Suara Pintar",
                            description = "Latihan pelafalan kalimat sederhana"
                        )
                    }
                }
            }
        }
    }
}