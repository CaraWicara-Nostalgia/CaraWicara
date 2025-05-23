package com.karina.carawicara.ui.screen.therapyHistory

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karina.carawicara.ui.screen.patient.PatientViewModel
import com.karina.carawicara.ui.screen.patient.TherapyHistoryItem
import com.karina.carawicara.utils.ExportUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TherapyHistoryListPage(
    navController: NavController,
    patientId: String,
    viewModel: PatientViewModel
) {
    val therapyHistories by viewModel.getTherapyHistoriesForPatient(patientId).collectAsState(initial = emptyList())
    val patients by viewModel.patients.collectAsState()
    val patient = patients.find { it.id == patientId }
    val context = LocalContext.current

    val isLoading by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Riwayat Terapi ${patient?.name ?: ""}",
                        fontSize = 16.sp,
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
                    IconButton(onClick = { showMenu = true}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More"
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("Export CSV") },
                            onClick = {
                                showMenu = false
                                if (therapyHistories.isNotEmpty() && patient != null) {
                                    val uri = ExportUtils.exportTherapyHistoriesToCsv(
                                        context,
                                        therapyHistories,
                                        patient.name
                                    )
                                    if (uri != null) {
                                        ExportUtils.shareFile(
                                            context,
                                            uri,
                                            "text/csv",
                                            "Riwayat_Terapi_${patient.name.replace(" ", "_")}.csv"
                                        )
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Gagal membuat file CSV",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Tidak ada data untuk diexport",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (therapyHistories.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Belum ada riwayat terapi",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Semua Riwayat Terapi",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Tampilkan semua riwayat terapi dalam daftar
                items(
                    items = therapyHistories.sortedByDescending { it.date },
                    key = { it.id }
                ) { history ->
                    TherapyHistoryItem(
                        history = history,
                        onClick = {
                            navController.navigate("therapyHistoryDetailPage/${history.id}")
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}