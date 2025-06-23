package com.karina.carawicara.ui.screen.therapyHistory

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karina.carawicara.data.SessionAssessment
import com.karina.carawicara.data.TherapyHistory
import com.karina.carawicara.ui.screen.patient.PatientViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TherapyResultPage(
    navController: NavController,
    sessionAssessment: SessionAssessment,
    patientViewModel: PatientViewModel
) {
    val previousRoute = remember {
        navController.previousBackStackEntry?.destination?.route ?: ""
    }

    var additionalNotes by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val selectedPatient by patientViewModel.selectedPatient.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hasil Terapi") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("flashcardPage") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hasil Akhir",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${sessionAssessment.totalCorrect}/${sessionAssessment.totalCards}",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = sessionAssessment.totalCards.toFloat() / sessionAssessment.totalCards,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.LightGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Rangkuman Mood Anak",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Mood Dominan: ${sessionAssessment.getDominantMood()}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    sessionAssessment.getMoodSummary().forEach { (mood, count) ->
                        Text(
                            text = "$mood: $count card",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Rangkuman Quick Notes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    sessionAssessment.getQuickNotesSummary().forEach { (note, count) ->
                        Text(
                            text = "$note: $count card",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Catatan Tambahan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = additionalNotes,
                    onValueChange = { additionalNotes = it },
                    placeholder = { Text("Tambahkan catatan detail di sini ...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                    )
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (selectedPatient == null) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Pilih pasien terlebih dahulu")
                        }
                        return@Button
                    }

                    val comprehensiveNotes = StringBuilder()

                    comprehensiveNotes.append("Mood Dominan: ${sessionAssessment.getDominantMood()}\n")
                    comprehensiveNotes.append("Detail Mood: ${sessionAssessment.getMoodSummary()}\n")

                    comprehensiveNotes.append("Quick Notes Summary: ${sessionAssessment.getQuickNotesSummary()}\n")

                    if (additionalNotes.isNotBlank()) {
                        comprehensiveNotes.append("Catatan Tambahan: $additionalNotes\n")
                    }

                    val progressPercentage = (sessionAssessment.totalCorrect * 100) / sessionAssessment.totalCards

                    val therapyHistory = TherapyHistory(
                        id = UUID.randomUUID().toString(),
                        patientId = selectedPatient!!.id,
                        therapyType = "Kosakata",
                        date = LocalDate.now(),
                        score = sessionAssessment.totalCorrect,
                        totalQuestions = sessionAssessment.totalCards,
                        progressPercentage = progressPercentage,
                        notes = comprehensiveNotes.toString(),
                        categoryId = "",
                        showLine = true
                    )

                    patientViewModel.addTherapyHistory(therapyHistory)

                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Hasil terapi berhasil disimpan")
                    }

                    navController.navigate("flashcardPage") {
                        popUpTo(previousRoute) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Simpan Hasil Terapi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CheckboxItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Black,
                uncheckedColor = Color.Gray
            )
        )
        Text(
            text = text,
            fontSize = 14.sp
        )
    }
}

@Composable
fun MoodOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            border = BorderStroke(
                width = 2.dp,
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
            ),
            color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.White,
            modifier = Modifier
                .size(60.dp)
                .padding(4.dp),
            onClick = onClick
        ) {
            Box(contentAlignment = Alignment.Center) {
                val emoji = when (label) {
                    "Senang" -> "😊"
                    "Biasa" -> "😐"
                    "Tidak Fokus" -> "😕"
                    else -> "❓"
                }

                Text(
                    text = emoji,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Text(
            text = label,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}