package com.karina.carawicara.ui.screen.home

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.BottomNavBar
import com.karina.carawicara.ui.component.ButtonNav
import com.karina.carawicara.ui.component.CardHome
import com.karina.carawicara.ui.screen.patient.PatientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavController,
    patientViewModel: PatientViewModel
) {
    Scaffold (
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ){
        paddingValues ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Image(
                        modifier = Modifier.size(130.dp, 24.dp),
                        painter = painterResource(id = R.drawable.logo_color),
                        contentDescription = null
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_library),
                        contentDescription = "Pustaka Wicara",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                navController.navigate("pustakaWicaraPage")
                            }
                            .padding(4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                LazyColumn {
                    item {
                        Column {
                            CardHome(
                                onClick = {
                                    val selectedPatient = patientViewModel.selectedPatient.value
                                    if (selectedPatient != null) {
                                        navController.navigate("flashcardPage")
                                    } else {
                                        navController.navigate("patientSelectionForTherapy/flashcardPage")
                                    } },
                                title = "Flashcard",
                                description = "Belajar kata melalui gambar"
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            CardHome(
                                onClick = { navController.navigate("patientSelectionForTherapy/paduGambarPage") },
                                title = "Padu Gambar",
                                description = "Mencocokkan gambar yang sesuai"
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            CardHome(
                                onClick = { navController.navigate("patientSelectionForTherapy/kenaliAkuPage/Coba tirukan gerakan wajah seperti contoh diatas!") },
                                title = "Kenali Aku",
                                description = "Mengekspresikan suasana hati"
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            CardHome(
                                onClick = { navController.navigate("patientSelectionForTherapy/susunKataPage/0") },
                                title = "Susun Kata",
                                description = "Merangkai kata menjadi kalimat"
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            CardHome(
                                onClick = { navController.navigate("patientSelectionForTherapy/suaraPintarPage") },
                                title = "Suara Pintar",
                                description = "Latihan konsonan_m kalimat sederhana"
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}