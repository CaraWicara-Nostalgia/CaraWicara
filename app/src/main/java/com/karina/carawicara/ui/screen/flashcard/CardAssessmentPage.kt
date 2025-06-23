package com.karina.carawicara.ui.screen.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karina.carawicara.data.CardAssessment
import com.karina.carawicara.ui.screen.therapyHistory.CheckboxItem
import com.karina.carawicara.ui.screen.therapyHistory.MoodOption
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardAssessmentPage(
    navController: NavController,
    cardWord: String,
    cardIndex: Int,
    totalCards: Int,
    isCorrect: Boolean,
    onAssessmentComplete: (CardAssessment) -> Unit
) {
    var selectedMood by remember { mutableStateOf<String?>(null) }
    var withHelp by remember { mutableStateOf(false) }
    var independent by remember { mutableStateOf(false) }
    var needsRepetition by remember { mutableStateOf(false) }
    var fullSpirit by remember { mutableStateOf(false) }

    var showValidationError by remember { mutableStateOf(false) }
    var validationMessage by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    fun validateFields(): Boolean {
        val quickNotesSelected = withHelp || independent || needsRepetition || fullSpirit

        return when {
            selectedMood == null -> {
                validationMessage = "Silakan pilih mood anak untuk card ini."
                false
            }
            !quickNotesSelected -> {
                validationMessage = "Silakan pilih setidaknya satu opsi di Quick Notes."
                false
            }
            else -> true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Penilaian Card ${cardIndex + 1}/${totalCards}",
                        fontSize = 16.sp
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCorrect) Color(0xFFE8F5E8) else Color(0xFFFFEBEE)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = cardWord,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isCorrect) "✓ BENAR" else "✗ SALAH",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFE57373)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedCard (
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (showValidationError && !(withHelp || independent || needsRepetition || fullSpirit)) {
                        Color(0xFFFFEBEE)
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Quick Notes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "*",
                            fontSize = 16.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Silakan pilih setidaknya satu opsi",
                            fontSize = 12.sp,
                            color = Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    CheckboxItem(
                        text = "Dapat melafalkan dengan bantuan",
                        checked = withHelp,
                        onCheckedChange = {
                            withHelp = it
                            showValidationError = false
                        }
                    )

                    CheckboxItem(
                        text = "Dapat melafalkan mandiri",
                        checked = independent,
                        onCheckedChange = {
                            independent = it
                            showValidationError = false
                        }
                    )

                    CheckboxItem(
                        text = "Penuh Semangat",
                        checked = fullSpirit,
                        onCheckedChange = {
                            fullSpirit = it
                            showValidationError = false
                        }
                    )

                    if (showValidationError && !(withHelp || independent || needsRepetition || fullSpirit)) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Validation Error",
                                tint = Color.Red,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Pilih minimal satu opsi",
                                fontSize = 12.sp,
                                color = Color.Red
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (showValidationError && selectedMood == null) {
                        Color(0xFFFFEBEE)
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Mood Anak",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "*",
                            fontSize = 16.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Silakan pilih mood anak untuk card ini",
                            fontSize = 12.sp,
                            color = Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        MoodOption(
                            label = "Senang",
                            selected = selectedMood == "Senang",
                            onClick = {
                                selectedMood = "Senang"
                                showValidationError = false }
                        )

                        MoodOption(
                            label = "Biasa",
                            selected = selectedMood == "Biasa",
                            onClick = {
                                selectedMood = "Biasa"
                                showValidationError = false
                            }
                        )

                        MoodOption(
                            label = "Tidak Fokus",
                            selected = selectedMood == "Tidak Fokus",
                            onClick = {
                                selectedMood = "Tidak Fokus"
                                showValidationError = false
                            }
                        )
                    }

                    if (showValidationError && selectedMood == null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Validation Error",
                                tint = Color.Red,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Silakan pilih mood anak",
                                fontSize = 12.sp,
                                color = Color.Red,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (validateFields()) {
                        val quickNotes = mutableListOf<String>()
                        if (withHelp) quickNotes.add("Dapat melafalkan dengan bantuan")
                        if (independent) quickNotes.add("Dapat melafalkan mandiri")
                        if (needsRepetition) quickNotes.add("Butuh pengulangan >3x")
                        if (fullSpirit) quickNotes.add("Penuh Semangat")

                        val assessment = CardAssessment(
                            cardIndex = cardIndex,
                            cardWord = cardWord,
                            isCorrect = isCorrect,
                            mood = selectedMood,
                            quickNotes = quickNotes
                        )

                        onAssessmentComplete(assessment)
                    } else {
                        showValidationError = true
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(validationMessage)
                        }
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
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = if (cardIndex + 1 < totalCards) "Lanjut ke Card Berikutnya" else "Selesai",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}