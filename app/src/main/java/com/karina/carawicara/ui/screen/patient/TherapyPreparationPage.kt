package com.karina.carawicara.ui.screen.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karina.carawicara.R
import com.karina.carawicara.data.Patient
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TherapyPreparationPage(
    navController: NavController,
    patientId: String,
    nextRoute: String,
    viewModel: PatientViewModel
) {
    var patient by remember { mutableStateOf<Patient?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch patient details
    LaunchedEffect(patientId) {
        isLoading = true
        patient = viewModel.getPatientById(patientId)
        delay(500) // Short delay for loading effect
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Persiapan Terapi") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                // Loading state
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Show loading indicator
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Memuat data pasien...")
                }
            } else if (patient == null) {
                // Error state
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Pasien tidak ditemukan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.popBackStack() }
                    ) {
                        Text("Kembali")
                    }
                }
            } else {
                // Patient found, show preparation UI
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Patient profile image
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Patient Avatar",
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Patient name
                    Text(
                        text = patient!!.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Patient age
                    Text(
                        text = "${patient!!.age} tahun",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Therapy illustration
                    Image(
                        painter = painterResource(id = R.drawable.ic_exercise), // Replace with appropriate therapy image
                        contentDescription = "Therapy Illustration",
                        modifier = Modifier.size(150.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Therapy info
                    Text(
                        text = "Siap untuk memulai terapi?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Pastikan pasien dalam kondisi baik dan siap untuk mengikuti aktivitas terapi",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // Start button
                    Button(
                        onClick = {
                            // Navigate to the next route (therapy page)
                            navController.navigate(nextRoute) {
                                // Clear backstack to avoid going back to selection
                                popUpTo("patientSelectionForTherapy/{nextRoute}") {
                                    inclusive = true
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Mulai Terapi",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CircularProgressIndicator() {
    androidx.compose.material3.CircularProgressIndicator(
        modifier = Modifier.size(48.dp),
        color = MaterialTheme.colorScheme.primary
    )
}