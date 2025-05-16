package com.karina.carawicara.ui.screen.patient

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karina.carawicara.data.TherapyHistory
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevelopmentDetailPage(
    navController: NavController,
    patientId: String,
    viewModel: PatientViewModel
){
    val therapyHistories by viewModel.getTherapyHistoriesForPatient(patientId).collectAsState(initial = emptyList())
    val patients by viewModel.patients.collectAsState()
    val patient = patients.find { it.id == patientId }

    val kosakataData = calculateProgressData(therapyHistories, "Kosakata")
    val pelafalanData = calculateProgressData(therapyHistories, "Pelafalan")
    val sequenceData = calculateProgressData(therapyHistories, "Sequence")

    var monthExpanded by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf(LocalDate.now().month) }
    var expandedSessionId by remember { mutableStateOf<Int?>(null) }

    val weeklySessionData = remember(therapyHistories, selectedMonth) {
        convertTherapyHistoriesToWeeklySessions(therapyHistories.filter {
            it.date.month == selectedMonth
        })
    }

    Scaffold(
        modifier = Modifier.padding(8.dp),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Perkembangan",
                            fontWeight = FontWeight.Medium
                        )
                        patient?.let {
                            Text(
                                text = it.name,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
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
                    Box {
                        TextButton(
                            onClick = { monthExpanded = !monthExpanded },
                        ) {
                            Text(formatMonth(selectedMonth))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Select Month"
                            )
                        }

                        DropdownMenu(
                            expanded = monthExpanded,
                            onDismissRequest = { monthExpanded = false }
                        ) {
                            Month.entries.forEach { month ->
                                DropdownMenuItem(
                                    text = { Text(formatMonth(month)) },
                                    onClick = {
                                        selectedMonth = month
                                        monthExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // Ringkasan
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Ringkasan Perkembangan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Statistik total terapi
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatisticItem(
                            label = "Total Terapi",
                            value = therapyHistories.size.toString(),
                            color = MaterialTheme.colorScheme.primary
                        )

                        StatisticItem(
                            label = "Minggu Ini",
                            value = countTherapiesInCurrentWeek(therapyHistories).toString(),
                            color = Color(0xFF4CAF50)
                        )

                        StatisticItem(
                            label = "Bulan Ini",
                            value = therapyHistories.count { it.date.month == LocalDate.now().month }.toString(),
                            color = Color(0xFF2196F3)
                        )
                    }
                }
            }

            // Legend at the bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                LegendItemDetail(color = MaterialTheme.colorScheme.primary, text = "Pelafalan")
                LegendItemDetail(color = Color(0xFF4CAF50), text = "Kosakata")
                LegendItemDetail(color = Color(0xFF2196F3), text = "Sequence")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Grafik perkembangan - lebih bersih
            Text(
                text = "Grafik Perkembangan",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ImprovedProgressChart(
                pelafalanData = pelafalanData,
                kosakataData = kosakataData,
                sequenceData = sequenceData
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Riwayat Terapi Mingguan
            Text(
                text = "Riwayat Terapi Mingguan",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (weeklySessionData.isEmpty()) {
                EmptyStateCard(
                    message = "Belum ada data terapi untuk bulan ${formatMonth(selectedMonth)}"
                )
            } else {
                // Daftar sesi mingguan yang diperbarui
                weeklySessionData.forEach { session ->
                    val isExpanded = session.week == expandedSessionId

                    ImprovedWeeklySessionItem(
                        session = session.copy(expanded = isExpanded),
                        onToggleExpand = { toggledSession ->
                            expandedSessionId = if (isExpanded) null else toggledSession.week

                        }
                    )

                    Divider(
                        thickness = 1.dp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StatisticItem(label: String, value: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = color
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun EmptyStateCard(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ImprovedWeeklySessionItem(
    session: WeeklySession,
    onToggleExpand: (WeeklySession) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onToggleExpand(session) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Date and Week
                Column {
                    Text(
                        text = session.date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "Minggu ke-${session.week}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // Expand/Collapse icon
                Icon(
                    imageVector = if (session.expanded) Icons.Default.KeyboardArrowDown else Icons.Default.Add,
                    contentDescription = if (session.expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Results (if expanded)
            if (session.expanded && session.results.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                Divider(color = Color.LightGray)

                Spacer(modifier = Modifier.height(12.dp))

                // Results in grid layout
                session.results.entries.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowItems.forEach { (type, score) ->
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = type,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )

                                Text(
                                    text = score,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Add empty space if odd number of items
                        if (rowItems.size % 2 != 0) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ImprovedProgressChart(
    pelafalanData: List<Float>,
    kosakataData: List<Float>,
    sequenceData: List<Float>
) {

    val primaryColor = MaterialTheme.colorScheme.primary
    val greenColor = Color(0xFF4CAF50)
    val blueColor = Color(0xFF2196F3)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Percentage labels on the left (y-axis)
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "100%", fontSize = 10.sp, color = Color.Gray)
                    Text(text = "75%", fontSize = 10.sp, color = Color.Gray)
                    Text(text = "50%", fontSize = 10.sp, color = Color.Gray)
                    Text(text = "25%", fontSize = 10.sp, color = Color.Gray)
                    Text(text = "0%", fontSize = 10.sp, color = Color.Gray)
                }

                // Main chart area
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, bottom = 20.dp)
                ) {
                    val width = size.width
                    val height = size.height

                    // Draw horizontal grid lines
                    val gridLines = 4
                    for (i in 0..gridLines) {
                        val y = height * (1 - i.toFloat() / gridLines)
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1f
                        )
                    }

                    // Draw Pelafalan line chart (Primary color)
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

                            // Add dots at data points
                            drawCircle(
                                color = primaryColor,
                                radius = 4f,
                                center = Offset(x, y)
                            )
                        }

                        drawPath(
                            path = path,
                            color = primaryColor,
                            style = Stroke(width = 2f, cap = StrokeCap.Round)
                        )
                    }

                    // Draw Kosakata line chart (Green)
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

                            // Add dots at data points
                            drawCircle(
                                color = greenColor, // Green
                                radius = 4f,
                                center = Offset(x, y)
                            )
                        }

                        drawPath(
                            path = path,
                            color = greenColor, // Green
                            style = Stroke(width = 2f, cap = StrokeCap.Round)
                        )
                    }

                    // Draw Sequence line chart (Blue)
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

                            // Add dots at data points
                            drawCircle(
                                color = blueColor, // Blue
                                radius = 4f,
                                center = Offset(x, y)
                            )
                        }

                        drawPath(
                            path = path,
                            color = blueColor, // Blue
                            style = Stroke(width = 2f, cap = StrokeCap.Round)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WeekLabel(1)
                    WeekLabel(2)
                    WeekLabel(3)
                    WeekLabel(4)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

private fun countTherapiesInCurrentWeek(histories: List<TherapyHistory>): Int {
    val now = LocalDate.now()
    val startOfWeek = now.minusDays(now.dayOfWeek.value - 1L)
    val endOfWeek = startOfWeek.plusDays(6)

    return histories.count { history ->
        history.date.isAfter(startOfWeek.minusDays(1)) &&
                history.date.isBefore(endOfWeek.plusDays(1))
    }
}

@Composable
fun LegendItemDetail(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(2.dp))
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
    }
}

private fun convertTherapyHistoriesToWeeklySessions(histories: List<TherapyHistory>): List<WeeklySession> {
    val groupedByWeek = histories.groupBy { history ->
        (history.date.dayOfMonth - 1) / 7 + 1
    }

    return groupedByWeek.map { (week, weekHistories) ->
        val latestDate = weekHistories.maxByOrNull { it.date }?.date ?: LocalDate.now()

        val results = weekHistories.associate { history ->
            history.therapyType to "${history.score}/${history.totalQuestions}"
        }

        WeeklySession(
            date = latestDate,
            week = week,
            expanded = false,
            results = results
        )
    }.sortedBy { it.week }
}

@Composable
fun WeekLabel(week: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = week.toString(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = "Minggu",
            fontSize = 8.sp,
            color = Color.Gray
        )
    }
}

// Data class for weekly sessions
data class WeeklySession(
    val date: LocalDate,
    val week: Int,
    val expanded: Boolean = false,
    val results: Map<String, String> = emptyMap()
)
