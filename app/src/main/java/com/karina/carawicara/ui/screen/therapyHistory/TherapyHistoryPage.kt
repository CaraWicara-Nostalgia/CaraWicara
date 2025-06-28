package com.karina.carawicara.ui.screen.therapyHistory

import android.widget.Space
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karina.carawicara.data.TherapyHistory
import com.karina.carawicara.ui.screen.patient.PatientViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TherapyHistoryDetailPage(
    navController: NavController,
    therapyHistoryId: String,
    viewModel: PatientViewModel
) {
    var therapyHistory by remember { mutableStateOf<TherapyHistory?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    // Load the therapy history when the page is loaded
    LaunchedEffect(therapyHistoryId) {
        val history = viewModel.getTherapyHistoryById(therapyHistoryId)
        therapyHistory = history
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus riwayat terapi ini?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        viewModel.deleteTherapyHistory(therapyHistoryId)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Hapus", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteConfirmDialog = false }
                ) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Terapi") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteConfirmDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (therapyHistory != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Date and therapy type
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = therapyHistory!!.date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = therapyHistory!!.therapyType,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Score and progress
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Score",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${therapyHistory!!.score}/${therapyHistory!!.totalQuestions}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LinearProgressIndicator(
                            progress = therapyHistory!!.progressPercentage / 100f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = Color.LightGray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${therapyHistory!!.progressPercentage}%",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (therapyHistory!!.notes.isNotEmpty()) {
                    val notesData = parseTherapyNotes(therapyHistory!!.notes)

                    // Notes section
                    if (therapyHistory!!.notes.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Face,
                                        contentDescription = null,
                                        tint = Color(0xFF7B1FA2),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(
                                        text = "Rangkuman Mood",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF7B1FA2)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Mood Dominan: ${notesData.dominantMood}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                                notesData.moodSummary.forEach { (mood, count) ->
                                    Text(
                                        text = "$mood: $count card",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (notesData.quickNotesSummary.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8)),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Face,
                                        contentDescription = null,
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(
                                        text = "Rangkuman Quick Notes",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4CAF50)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                notesData.quickNotesSummary.forEach { (note, count) ->
                                    Text(
                                        text = "$note: $count",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (notesData.additionalNotes.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(
                                        text = "Catatan Tambahan",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = notesData.additionalNotes,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    if (notesData.moodSummary.isEmpty() && notesData.quickNotesSummary.isEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Catatan",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                therapyHistory!!.notes.split("\n").forEach { line ->
                                    if (line.isNotEmpty()) {
                                        Text(
                                            text = line,
                                            fontSize = 14.sp,
                                            lineHeight = 20.sp,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Repeat therapy button
                Button(
                    onClick = {
                        // Navigate to the therapy page based on the type and category
                        val route = when {
                            therapyHistory!!.therapyType.contains("Kosakata", ignoreCase = true) ->
                                "kosakataExerciseDetailPage/${therapyHistory!!.categoryId}"
                            therapyHistory!!.therapyType.contains("Pelafalan", ignoreCase = true) ->
                                "pelafalanExerciseDetailPage/${therapyHistory!!.categoryId}"
                            therapyHistory!!.therapyType.contains("Sequence", ignoreCase = true) ->
                                "sequenceExerciseDetailPage/${therapyHistory!!.categoryId}"
                            else -> "flashcardPage"
                        }
                        navController.navigate(route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Ulangi Terapi Ini")
                }
            }
        } else {
            // Loading or not found state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Memuat data terapi...",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

data class ParsedTherapyNotes(
    val moodSummary: Map<String, Int> = emptyMap(),
    val quickNotesSummary: Map<String, Int> = emptyMap(),
    val dominantMood: String = "Tidak diketahui",
    val additionalNotes: String = ""
)

fun parseTherapyNotes(notes: String): ParsedTherapyNotes {
    val lines = notes.split("\n")
    var dominantMood = "Tidak diketahui"
    val moodSummary = mutableMapOf<String, Int>()
    val quickNotesSummary = mutableMapOf<String, Int>()
    var additionalNotes = ""

    lines.forEach { line ->
        when {
            line.startsWith("Mood Dominan:") -> {
                dominantMood = line.substringAfter("Mood Dominan:").trim()
            }
            line.startsWith("Detail Mood:") -> {
                // Parse format: {Senang=3, Biasa=2}
                val moodData = line.substringAfter("Detail Mood:").trim()
                    .removeSurrounding("{", "}")
                    .split(", ")

                moodData.forEach { pair ->
                    val (mood, count) = pair.split("=")
                    moodSummary[mood.trim()] = count.trim().toIntOrNull() ?: 0
                }
            }
            line.startsWith("Quick Notes Summary:") -> {
                // Parse format: {Dapat melafalkan mandiri=5, Penuh Semangat=8}
                val notesData = line.substringAfter("Quick Notes Summary:").trim()
                    .removeSurrounding("{", "}")
                    .split(", ")

                notesData.forEach { pair ->
                    if (pair.contains("=")) {
                        val parts = pair.split("=")
                        if (parts.size == 2) {
                            val note = parts[0].trim()
                            val count = parts[1].trim().toIntOrNull() ?: 0
                            quickNotesSummary[note] = count
                        }
                    }
                }
            }
            line.startsWith("Catatan Tambahan:") -> {
                additionalNotes = line.substringAfter("Catatan Tambahan:").trim()
            }
        }
    }

    return ParsedTherapyNotes(
        dominantMood = dominantMood,
        moodSummary = moodSummary,
        quickNotesSummary = quickNotesSummary,
        additionalNotes = additionalNotes
    )
}