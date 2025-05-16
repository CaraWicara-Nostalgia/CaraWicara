package com.karina.carawicara.ui.screen.patient

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karina.carawicara.R
import com.karina.carawicara.data.TherapyHistory
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.Month

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientProfilePage(
    navController: NavController,
    patientId: String,
    viewModel: PatientViewModel
) {
    val patients by viewModel.patients.collectAsState()
    val therapyHistories by viewModel.getTherapyHistoriesForPatient(patientId).collectAsState(initial = emptyList())

    var selectedMonth by remember { mutableStateOf(LocalDate.now().month) }

    val patient = patients.find { it.id == patientId }

    val filteredHistories = therapyHistories.filter {
        it.date.month == selectedMonth
    }.sortedByDescending { it.date }

    if (patient == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Pasien tidak ditemukan")
        }
        return
    }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus pasien ini? Tindakan ini tidak dapat dibatalkan.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        viewModel.deletePatient(patientId)
                        navController.navigate("patientPage") {
                            popUpTo("patientPage") { inclusive = true }
                        }
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
                title = { /* Empty title */ },
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
                            showDeleteConfirmDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                    IconButton(onClick = { /* Edit patient profile */ }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_profile),
                        contentDescription = "Profile Photo",
                        modifier = Modifier.size(60.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = patient.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${patient.age} Tahun",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = patient.address,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kemampuan Bahasa",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (patient.languageAbilities.isEmpty()) {
                    Text(
                        text = "Belum ada data kemampuan bahasa",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    Column {
                        patient.languageAbilities.forEach { ability ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "•",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = ability.description,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Perkembangan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    var monthDropdownExpanded by remember { mutableStateOf(false) }

                    TextButton(
                        onClick = { navController.navigate("developmentDetailPage/$patientId") }
                    ) {
                        Text("Detail")
                    }

                    TextButton(
                        onClick = { monthDropdownExpanded = true }
                    ) {
                        Text(formatMonth(selectedMonth))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Select Month",
                            modifier = Modifier.size(16.dp)
                        )

                        DropdownMenu(
                            expanded = monthDropdownExpanded,
                            onDismissRequest = { monthDropdownExpanded = false }
                        ) {
                            Month.entries.forEach { month ->
                                DropdownMenuItem(
                                    text = { Text(formatMonth(month)) },
                                    onClick = {
                                        selectedMonth = month
                                        monthDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (therapyHistories.isNotEmpty()) {
                    val kosakataData = calculateProgressData(therapyHistories, "Kosakata")
                    val pelafalanData = calculateProgressData(therapyHistories, "Pelafalan")
                    val sequenceData = calculateProgressData(therapyHistories, "Sequence")

                    ProgressChart(
                        pelafalanData = pelafalanData,
                        kosakataData = kosakataData,
                        sequenceData = sequenceData
                    )
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Belum ada data perkembangan",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Riwayat Terapi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(
                        onClick = {
                            navController.navigate("therapyHistoryListPage/$patientId")
                        }
                    ) {
                        Text("Lihat semua")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            if (filteredHistories.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada riwayat terapi",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                items(filteredHistories.take(3)) { history ->
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

            item {
                Button(
                    onClick = {
                        viewModel.setSelectedPatientId(patientId)

                        navController.navigate("homePage") {
                            popUpTo("patientPage") { inclusive = false }
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
                        text = "Terapi Sekarang",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun TherapyHistoryItem(
    history: TherapyHistory,
    onClick: () -> Unit = {}
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = history.date.dayOfMonth.toString(),
                    fontSize = 12.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = history.date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = history.therapyType,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "Score: ${history.score}/${history.totalQuestions} (${history.progressPercentage}%)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = history.notes,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp),
                    lineHeight = 16.sp
                )
            }
        }

        if (history.showLine) {
            Box(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .width(2.dp)
                    .height(24.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            )
        }
    }
}

@Composable
fun ProgressChart(
    pelafalanData: List<Float>,
    kosakataData: List<Float>,
    sequenceData: List<Float>
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height - 40f

                val gridLines = 5
                for (i in 0..gridLines) {
                    val y = height * (1 - i.toFloat() / gridLines)
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1f
                    )
                }

                if (pelafalanData.isNotEmpty()) {
                    val path = Path()
                    val pointsPerWeek = width / (pelafalanData.size - 1).coerceAtLeast(1)

                    pelafalanData.forEachIndexed { index, value ->
                        val x = index * pointsPerWeek
                        val y = height * (1 - value / 100f)

                        if (index == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = Color.Blue,
                        style = Stroke(width = 2f, cap = StrokeCap.Round)
                    )
                }

                if (kosakataData.isNotEmpty()) {
                    val path = Path()
                    val pointsPerWeek = width / (kosakataData.size - 1).coerceAtLeast(1)

                    kosakataData.forEachIndexed { index, value ->
                        val x = index * pointsPerWeek
                        val y = height * (1 - value / 100f)

                        if (index == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = Color.Green,
                        style = Stroke(width = 2f, cap = StrokeCap.Round)
                    )
                }

                if (sequenceData.isNotEmpty()) {
                    val path = Path()
                    val pointsPerWeek = width / (sequenceData.size - 1).coerceAtLeast(1)

                    sequenceData.forEachIndexed { index, value ->
                        val x = index * pointsPerWeek
                        val y = height * (1 - value / 100f)

                        if (index == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = Color.Red,
                        style = Stroke(width = 2f, cap = StrokeCap.Round)
                    )
                }
            }

            Row (
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                LegendItem(
                    color = Color.Blue,
                    text = "Pelafalan"
                )
                LegendItem(
                    color = Color.Green,
                    text = "Kosakata"
                )
                LegendItem(
                    color = Color.Red,
                    text = "Sequence"
                )
            }

            Row (
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = "Minggu 1", fontSize = 10.sp, color = Color.Gray)
                Text(text = "Minggu 2", fontSize = 10.sp, color = Color.Gray)
                Text(text = "Minggu 3", fontSize = 10.sp, color = Color.Gray)
                Text(text = "Minggu 4", fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .width(16.dp)
                .height(2.dp),
            color = color
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            fontSize = 10.sp,
            color = Color.Gray
        )
    }
}

fun formatMonth(month: Month): String {
    return when (month) {
        Month.JANUARY -> "Januari"
        Month.FEBRUARY -> "Februari"
        Month.MARCH -> "Maret"
        Month.APRIL -> "April"
        Month.MAY -> "Mei"
        Month.JUNE -> "Juni"
        Month.JULY -> "Juli"
        Month.AUGUST -> "Agustus"
        Month.SEPTEMBER -> "September"
        Month.OCTOBER -> "Oktober"
        Month.NOVEMBER -> "November"
        Month.DECEMBER -> "Desember"
    }
}

fun calculateProgressData(histories: List<TherapyHistory>, therapyType: String): List<Float> {
    val filteredHistories = histories.filter { it.therapyType.contains(therapyType, ignoreCase = true) }

    if (filteredHistories.isEmpty()) {
        return emptyList()
    }

    val weeklyData = Array(4) { 0f }.toMutableList()
    val weeklyCount = Array(4) { 0 }.toMutableList()

    val today = LocalDate.now()

    filteredHistories.forEach { history ->
        if (history.date.month == today.month) {
            val dayDifference = history.date.dayOfMonth - 1
            val weekIndex = (dayDifference / 7).coerceIn(0, 3)

            weeklyData[weekIndex] = weeklyData[weekIndex] + history.progressPercentage
            weeklyCount[weekIndex] = weeklyCount[weekIndex] + 1
        }
    }

    return weeklyData.mapIndexed { index, sum ->
        if (weeklyCount[index] > 0) sum / weeklyCount[index] else 0f
    }
}