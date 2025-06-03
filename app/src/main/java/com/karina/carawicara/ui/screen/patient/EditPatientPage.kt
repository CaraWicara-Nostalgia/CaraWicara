package com.karina.carawicara.ui.screen.patient


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karina.carawicara.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPatientPage(
    navController: NavController,
    patientId: String,
    viewModel: PatientViewModel
) {
    val patients by viewModel.patients.collectAsState()
    val patient = patients.find { it.id == patientId }

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Inisialisasi form dengan data patient
    LaunchedEffect(patient) {
        patient?.let {
            name = it.name
            age = it.age.toString()
            address = it.address
        }
    }

    // Validasi form
    val isFormValid = name.isNotBlank() &&
            age.isNotBlank() &&
            age.toIntOrNull() != null &&
            age.toInt() > 0 &&
            address.isNotBlank()

    if (patient == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Pasien tidak ditemukan")
        }
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Pasien",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isFormValid && !isLoading) {
                                isLoading = true
                                val updatedPatient = patient.copy(
                                    name = name.trim(),
                                    age = age.toInt(),
                                    address = address.trim()
                                )

                                try {
                                    viewModel.updatePatient(updatedPatient)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Data pasien berhasil diperbarui")
                                    }
                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Gagal memperbarui data pasien")
                                    }
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        enabled = isFormValid && !isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save",
                            tint = if (isFormValid && !isLoading) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.Gray
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Profile Photo
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Profile Photo",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Form Fields
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Nama Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Lengkap") },
                    placeholder = { Text("Masukkan nama lengkap") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = name.isBlank() && name.isNotEmpty()
                )

                // Umur Field
                OutlinedTextField(
                    value = age,
                    onValueChange = {
                        // Hanya terima input angka
                        if (it.all { char -> char.isDigit() } && it.length <= 3) {
                            age = it
                        }
                    },
                    label = { Text("Umur") },
                    placeholder = { Text("Masukkan umur") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    isError = age.isNotBlank() && (age.toIntOrNull() == null || age.toInt() <= 0),
                    supportingText = {
                        if (age.isNotBlank() && (age.toIntOrNull() == null || age.toInt() <= 0)) {
                            Text(
                                text = "Umur harus berupa angka yang valid",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    }
                )

                // Alamat Field
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Alamat") },
                    placeholder = { Text("Masukkan alamat lengkap") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2,
                    maxLines = 4,
                    isError = address.isBlank() && address.isNotEmpty()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    Text(
                        text = "Batal",
                        fontSize = 16.sp
                    )
                }

                Button(
                    onClick = {
                        if (isFormValid && !isLoading) {
                            isLoading = true
                            val updatedPatient = patient.copy(
                                name = name.trim(),
                                age = age.toInt(),
                                address = address.trim()
                            )

                            try {
                                viewModel.updatePatient(updatedPatient)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Data pasien berhasil diperbarui")
                                }
                                navController.popBackStack()
                            } catch (e: Exception) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Gagal memperbarui data pasien")
                                }
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = isFormValid && !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = Color.Gray
                    )
                ) {
                    Text(
                        text = if (isLoading) "Menyimpan..." else "Simpan",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Info Text
            Text(
                text = "Pastikan semua data yang dimasukkan sudah benar sebelum menyimpan",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}