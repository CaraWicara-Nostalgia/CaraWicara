package com.karina.carawicara.ui.screen.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevelopmentDetailPage(
    navController: NavController,
    patientId: String,
    viewModel: PatientViewModel
){
    var monthExpanded by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf("Januari") }

    val pelafalanData = remember {
        List(4) { Random.nextDouble(25.0, 75.0).toFloat()}
    }
    val kosakataData = remember {
        List(4) { Random.nextDouble(25.0, 75.0).toFloat()}
    }
    val sequenceData = remember {
        List(4) { Random.nextDouble(25.0, 75.0).toFloat()}
    }

    val weeklySessionData = remember {
        listOf(
            WeeklySession(
                date = LocalDate.of(2025, 12, 12),
                week = 1,
                expanded = true,
                results = mapOf(
                    "Pelafalan" to "6/10",
                    "Kosakata" to "0/10",
                    "Sequence" to "0/10"
                )
            ),
            WeeklySession(
                date = LocalDate.of(2025, 12, 19),
                week = 2,
                expanded = false,
                results = mapOf(
                    "Pelafalan" to "6/10",
                    "Kosakata" to "0/10",
                    "Sequence" to "0/10"
                )
            ),
            WeeklySession(
                date = LocalDate.of(2025, 12, 26),
                week = 3,
                expanded = false,
                results = mapOf(
                    "Pelafalan" to "6/10",
                    "Kosakata" to "0/10",
                    "Sequence" to "0/10"
                )
            ),
        )
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Perkembangan",
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
                    Box{
                        TextButton(
                            onClick = {monthExpanded = !monthExpanded},
                        ) {
                            Text(selectedMonth)
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Select Month"
                            )
                        }

                        DropdownMenu(
                            expanded = monthExpanded,
                            onDismissRequest = {monthExpanded = false}
                        ) {
                            listOf("Januari", "Februari", "Maret", "April", "Mei", "Juni").forEach {month ->
                                DropdownMenuItem(
                                    text = { Text(month) },
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
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            ProgressChartDetail(
                pelafalanData = pelafalanData,
                kosakataData = kosakataData,
                sequenceData = sequenceData,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFB7E4C7)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.White, CircleShape)
                                .border(1.dp, Color.LightGray, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Selected",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Tanggal",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFB7E4C7)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Box (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "Waktu",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            weeklySessionData.forEach { session ->
                WeeklySessionItem (
                    session = session,
                    onToggleExpand = {

                    }
                )

                Divider(thickness = 1.dp, color = Color.LightGray)
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Session",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "12/12/2025",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklySessionItem(
    session: WeeklySession,
    onToggleExpand: (WeeklySession) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Session header (date and week)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Expand/collapse icon
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = if (session.expanded) Color.Gray else MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .clickable { onToggleExpand(session) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (session.expanded) Icons.Default.Delete else Icons.Default.Add,
                    contentDescription = if (session.expanded) "Collapse" else "Expand",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Date
            Text(
                text = session.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            // Week label
            Box(
                modifier = Modifier
                    .background(Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Minggu ke-${session.week}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        // Show results if expanded
        if (session.expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, top = 8.dp)
            ) {
                session.results.forEach { (type, score) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = type,
                            fontSize = 14.sp
                        )

                        Text(
                            text = score,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressChartDetail(
    pelafalanData: List<Float>,
    kosakataData: List<Float>,
    sequenceData: List<Float>,
) {
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
                val height = size.height

                // Draw horizontal grid lines
                val gridLines = 5
                for (i in 0..gridLines) {
                    val y = height * (1 - i.toFloat() / gridLines)
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1f
                    )

                    // Draw percentage labels
                    // Not implementing actual text drawing as it requires custom implementation
                }

                // Draw konsonan_m line chart
                if (pelafalanData.isNotEmpty()) {
                    val path = Path()
                    val pointsPerWeek = width / (pelafalanData.size - 1)

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
                        color = Color.Black,
                        style = Stroke(width = 2f, cap = StrokeCap.Round)
                    )
                }

                // Draw kosakata line chart
                if (kosakataData.isNotEmpty()) {
                    val path = Path()
                    val pointsPerWeek = width / (kosakataData.size - 1)

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
                        color = Color.DarkGray,
                        style = Stroke(width = 2f, cap = StrokeCap.Round)
                    )
                }

                // Draw sequence line chart
                if (sequenceData.isNotEmpty()) {
                    val path = Path()
                    val pointsPerWeek = width / (sequenceData.size - 1)

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
                        color = Color.Gray,
                        style = Stroke(width = 2f, cap = StrokeCap.Round)
                    )
                }
            }

            // Draw legend at the bottom
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LegendItemDetail(color = Color.Black, text = "Pelafalan")
                LegendItemDetail(color = Color.DarkGray, text = "Kosakata")
                LegendItemDetail(color = Color.Gray, text = "Sequence")
            }

            // Week labels at the bottom
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Minggu 1", fontSize = 10.sp, color = Color.Gray)
                Text(text = "Minggu 2", fontSize = 10.sp, color = Color.Gray)
                Text(text = "Minggu 3", fontSize = 10.sp, color = Color.Gray)
                Text(text = "Minggu 4", fontSize = 10.sp, color = Color.Gray)
            }

            // Percentage labels on the left
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "100", fontSize = 10.sp, color = Color.Gray)
                Text(text = "75", fontSize = 10.sp, color = Color.Gray)
                Text(text = "50", fontSize = 10.sp, color = Color.Gray)
                Text(text = "25", fontSize = 10.sp, color = Color.Gray)
                Text(text = "0", fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun LegendItemDetail(color: Color, text: String) {
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

// Data class for weekly sessions
data class WeeklySession(
    val date: LocalDate,
    val week: Int,
    val expanded: Boolean = false,
    val results: Map<String, String> = emptyMap()
)