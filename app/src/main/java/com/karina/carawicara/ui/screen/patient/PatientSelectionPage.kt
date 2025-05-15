package com.karina.carawicara.ui.screen.patient

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karina.carawicara.data.Patient

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientSelectionPage(
    navController: NavController,
    viewModel: PatientViewModel,
    nextRoute: String
) {
    var searchQuery by remember { mutableStateOf("") }
    val patients by viewModel.patients.collectAsState()

    val filteredPatients = if (searchQuery.isEmpty()) {
        patients
    } else {
        patients.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    val onPatientSelected = { patientId: String ->
        viewModel.setSelectedPatientId(patientId)
        navController.navigate(nextRoute) {
            popUpTo("patientSelectionForTherapy/{nextRoute}") { inclusive = true}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pilih Pasien") },
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
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ){
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cari pasien...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search"
                    )
                },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (patients.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "Tidak ada pasien",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { navController.navigate("addPatientPage") },
                        ) {
                            Text("Tambah Pasien")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(filteredPatients) { patient ->
                        PatientSelectionItem(
                            patient = patient,
                            onClick = {
                                Log.d("PatientSelectionPage", "Selected patient: ${patient.name}")

                                try {
                                    viewModel.setSelectedPatientId(patient.id)

                                    val route = when {
                                        nextRoute.startsWith("kenaliAkuPage/") -> {
                                            val message = nextRoute.substringAfter("kenaliAkuPage/")
                                            "kenaliAkuPage/$message"
                                        }
                                        nextRoute.startsWith("susunKataPage/") -> {
                                            val index = nextRoute.substringAfter("susunKataPage/")
                                            "susunKataPage/$index"
                                        }
                                        else -> {
                                            nextRoute
                                        }
                                    }
                                    navController.navigate(route)
                                } catch (e: Exception) {
                                    Log.e("PatientSelectionPage", "Error navigating to $nextRoute: ${e.message}")
                                }
                            },
                            viewModel = viewModel
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("addPatientPage") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tambah Pasien Baru")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PatientSelectionItem(
    patient: Patient,
    onClick: () -> Unit,
    viewModel: PatientViewModel
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
            .clickable {
                viewModel.setSelectedPatientId(patient.id)
                onClick()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ){
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "Patient Avatar",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = patient.name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${patient.age} tahun",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}