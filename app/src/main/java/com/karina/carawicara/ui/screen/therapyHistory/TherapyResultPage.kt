package com.karina.carawicara.ui.screen.therapyHistory

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karina.carawicara.R
import com.karina.carawicara.data.TherapyHistory
import com.karina.carawicara.ui.screen.patient.PatientViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TherapyResultPage(
    navController: NavController,
    score: Int = 0,
    totalQuestions: Int = 10,
    patientViewModel: PatientViewModel
) {
    val previousRoute = remember {
        navController.previousBackStackEntry?.destination?.route ?: ""
    }

    var withHelp by remember { mutableStateOf(false) }
    var independent by remember { mutableStateOf(false) }
    var needsRepetition by remember { mutableStateOf(false) }
    var fullSpirit by remember { mutableStateOf(false) }

    var selectedMood by remember { mutableStateOf<String?>(null) }

    var additionalNotes by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val selectedPatient by patientViewModel.selectedPatient.collectAsState()

    // Get category ID from previous route
    val categoryId = remember {
        when {
            previousRoute.contains("kosakataExerciseDetailPage") -> {
                navController.previousBackStackEntry?.arguments?.getString("category") ?: ""
            }
            previousRoute.contains("pelafalanExerciseDetailPage") -> {
                navController.previousBackStackEntry?.arguments?.getString("category") ?: ""
            }
            previousRoute.contains("sequenceExerciseDetailPage") -> {
                navController.previousBackStackEntry?.arguments?.getString("categoryTitle") ?: ""
            }
            else -> ""
        }
    }

    val therapyTpye = remember {
        when {
            previousRoute.contains("kosakataExerciseDetailPage") -> "Kosakata"
            previousRoute.contains("pelafalanExerciseDetailPage") -> "Pelafalan"
            previousRoute.contains("sequenceExerciseDetailPage") -> "Urutan"
            else -> ""
        }
    }

    val therapyName = remember {
        when {
            therapyTpye == "Kosakata" -> "Latihan Kosakata: ${getCategoryName(categoryId)}"
            therapyTpye == "Pelafalan" -> "Latihan Pelafalan: ${getCategoryName(categoryId)}"
            therapyTpye == "Urutan" -> "Latihan Urutan: ${getCategoryName(categoryId)}"
            else -> "Terapi: $categoryId"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
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
                text = "Correct",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "$score/$totalQuestions",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = score.toFloat() / totalQuestions,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.LightGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Quick Notes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                CheckboxItem(
                    text = "Dapat melafalkan dengan bantuan",
                    checked = withHelp,
                    onCheckedChange = { withHelp = it }
                )

                CheckboxItem(
                    text = "Dapat melafalkan mandiri",
                    checked = independent,
                    onCheckedChange = { independent = it }
                )

                CheckboxItem(
                    text = "Butuh pengulangan >3x",
                    checked = needsRepetition,
                    onCheckedChange = { needsRepetition = it }
                )

                CheckboxItem(
                    text = "Penuh semangat",
                    checked = fullSpirit,
                    onCheckedChange = { fullSpirit = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Mood Anak",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MoodOption(
                        label = "Senang",
                        selected = selectedMood == "Senang",
                        onClick = { selectedMood = "Senang" }
                    )

                    MoodOption(
                        label = "Biasa",
                        selected = selectedMood == "Biasa",
                        onClick = { selectedMood = "Biasa" }
                    )

                    MoodOption(
                        label = "Tidak Fokus",
                        selected = selectedMood == "Tidak Fokus",
                        onClick = { selectedMood = "Tidak Fokus" }
                    )
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
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Gray,
                        focusedBorderColor = Color.Gray
                    )
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            selectedPatient?.let { patient ->
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Pasien: ${patient.name}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedButton(
                        onClick = { navController.navigate("patientSelectionForTherapy/flashcardPage")},
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("Ganti")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            } ?: run {
                OutlinedButton(
                    onClick = { navController.navigate("patientSelectionForTherapy/flashcardPage")},
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Pilih Pasien",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            OutlinedButton(
                onClick = {
                    when {
                        previousRoute.contains("kosakataExerciseDetailPage") -> {
                            navController.navigate("kosakataExerciseDetailPage/$categoryId") {
                                popUpTo("therapyResultPage") { inclusive = true }
                            }
                        }
                        previousRoute.contains("pelafalanExerciseDetailPage") -> {
                            navController.navigate("pelafalanExerciseDetailPage/$categoryId") {
                                popUpTo("therapyResultPage") { inclusive = true }
                            }
                        }
                        previousRoute.contains("sequenceExerciseDetailPage") -> {
                            navController.navigate("sequenceExerciseDetailPage/$categoryId") {
                                popUpTo("therapyResultPage") { inclusive = true }
                            }
                        }
                        else -> {
                            navController.navigate("flashcardPage") {
                                popUpTo("therapyResultPage") { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Ulangi Terapi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (selectedPatient == null) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Pilih pasien terlebih dahulu")
                        }
                        return@Button
                    }

                    val notesBuilder = StringBuilder()
                    if (withHelp) notesBuilder.append("Dapat melafalkan dengan bantuan\n")
                    if (independent) notesBuilder.append("Dapat melafalkan mandiri\n")
                    if (needsRepetition) notesBuilder.append("Butuh pengulangan >3x\n")
                    if (fullSpirit) notesBuilder.append("Penuh semangat\n")

                    if (selectedMood != null) {
                        notesBuilder.append("Mood Anak: $selectedMood\n")
                    }

                    if (additionalNotes.isNotBlank()) {
                        notesBuilder.append("Catatan Tambahan: $additionalNotes\n")
                    }

                    val progressPercentage = (score * 100) / totalQuestions

                    val therapyHistory = TherapyHistory(
                        id = UUID.randomUUID().toString(),
                        patientId = selectedPatient!!.id,
                        therapyType = therapyTpye,
                        date = LocalDate.now(),
                        score = score,
                        totalQuestions = totalQuestions,
                        progressPercentage = progressPercentage,
                        notes = notesBuilder.toString(),
                        categoryId = categoryId,
                        showLine = true
                    )

                    patientViewModel.addTherapyHistory(therapyHistory)

                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Hasil terapi berhasil disimpan")
                    }

                    navController.navigate("flashcardPage") {
                        popUpTo("therapyResultPage") { inclusive = true }
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

@OptIn(ExperimentalMaterial3Api::class)
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
            border = BorderStroke(1.dp, if (selected) Color.Black else Color.Gray),
            color = if (selected) Color.LightGray else Color.White,
            modifier = Modifier
                .size(60.dp)
                .padding(4.dp),
            onClick = onClick
        ) {
            Box(contentAlignment = Alignment.Center) {
                val iconRes = when (label) {
                    "Senang" -> R.drawable.ic_senang
                    "Biasa" -> R.drawable.ic_biasa
                    "Tidak Fokus" -> R.drawable.ic_tidak_fokus
                    else -> null
                }

                if (iconRes != null) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = "Mood $label",
                        tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Text(
            text = label,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

fun getCategoryName(categoryId: String): String {
    return when (categoryId) {
        "buah" -> "Buah-buahan"
        "hewan" -> "Hewan"
        "pakaian" -> "Pakaian"
        "aktivitas" -> "Aktivitas"
        "konsonan_m" -> "Konsonan M"
        "konsonan_b" -> "Konsonan B"
        "aktivitas_urutan" -> "Urutan Aktivitas"
        else -> categoryId.replaceFirstChar { it.uppercase() }
    }
}