package com.karina.carawicara.ui.screen.susunKata

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonAlphabet
import com.karina.carawicara.ui.component.StageBox
import androidx.compose.ui.window.Dialog
import com.karina.carawicara.ui.component.PopupOverview

data class SoalSusunKata(
    val image: Int,
    val correctAnswer: String
)

@Composable
fun SusunKataPage(
    index: Int,
    navHostController: NavHostController
) {
    val currentSoalIndex = remember { mutableStateOf(0) }
    val selectedLetters = remember { mutableStateListOf<String>() }
    val letterUsage = remember { mutableStateMapOf<Char, Int>() }
    val isCorrectAnswer = remember { mutableStateOf(false) }
    val isPopupVisible = remember { mutableStateOf(false) }
    val stageBoxStatus = remember { mutableStateOf(0) }
    val isEndPopupVisible = remember { mutableStateOf(false) }

    val soalList = listOf(
        SoalSusunKata(R.drawable.kucing_2, "KUCING"),
        SoalSusunKata(R.drawable.anjing_2, "ANJING"),
        SoalSusunKata(R.drawable.ikan_2, "IKAN"),
        SoalSusunKata(R.drawable.sapi_3, "SAPI")
    )

    fun nextSoal() {
        if (currentSoalIndex.value < soalList.size - 1) {
            currentSoalIndex.value += 1
            stageBoxStatus.value += 1
        } else {
            isEndPopupVisible.value = true
        }
    }

    val currentSoal = soalList[index]

    // Generate additional random letters and shuffle them once
    val allLetters = remember {
        val additionalLetters = ('A'..'Z').filterNot { it in currentSoal.correctAnswer }.shuffled().take(8 - currentSoal.correctAnswer.length)
        (currentSoal.correctAnswer.toList() + additionalLetters).shuffled()
    }

    // Initialize letter usage count
    currentSoal.correctAnswer.forEach { char ->
        letterUsage[char] = letterUsage.getOrDefault(char, 0) + 1
    }
    allLetters.forEach { char ->
        if (char !in letterUsage) {
            letterUsage[char] = 1
        }
    }

    fun validateAnswer(letters: List<String>) {
        if (letters.joinToString("") == currentSoal.correctAnswer) {
            isCorrectAnswer.value = true
            stageBoxStatus.value += 1
            if (index < soalList.size - 1) {
                navHostController.navigate("susunKataPage/${index + 1}")
            } else {
                isEndPopupVisible.value = true
            }
        } else {
            isCorrectAnswer.value = false
        }
        isPopupVisible.value = true
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(31.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cancel),
                    contentDescription = "Cancel",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            navHostController.popBackStack()
                        }
                        .padding(4.dp)
                )
                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                    for (i in 0 until 4) {
                        StageBox(stage = if (i < stageBoxStatus.value) 1 else 0, onClick = { /* Handle click here */ })
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(311.dp, 332.dp)
                    .border(
                        2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(currentSoal.image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(279.dp, 300.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Susunlah huruf dibawah untuk menjawab!",
                fontSize = 24.sp,
                color = Color(0xFFFCB028),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                repeat(currentSoal.correctAnswer.length) { index ->
                    ButtonAlphabet(
                        onClick = { /*TODO*/ },
                        text = selectedLetters.getOrNull(index) ?: ""
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Divider()
            Spacer(modifier = Modifier.weight(1f))
            Row (
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Column {
                    Row(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        allLetters.subList(0, allLetters.size / 2).forEach { letter ->
                            ButtonAlphabet(
                                onClick = {
                                    selectedLetters.add(letter.toString())
                                    letterUsage[letter] = letterUsage.getOrDefault(letter, 0) - 1
                                },
                                text = letter.toString(),
                                enabled = (letterUsage[letter] ?: 0) > 0
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        allLetters.subList(allLetters.size / 2, allLetters.size).forEach { letter ->
                            ButtonAlphabet(
                                onClick = {
                                    selectedLetters.add(letter.toString())
                                    letterUsage[letter] = letterUsage.getOrDefault(letter, 0) - 1
                                },
                                text = letter.toString(),
                                enabled = (letterUsage[letter] ?: 0) > 0
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_erase),
                        contentDescription = "Hapus",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                if (selectedLetters.isNotEmpty()) {
                                    val lastLetter = selectedLetters.removeLast()
                                    letterUsage[lastLetter.single()] =
                                        letterUsage.getOrDefault(lastLetter.single(), 0) + 1
                                }
                            }
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_restart),
                        contentDescription = "Ulang",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                selectedLetters.clear()
                                // Reset letter usage
                                currentSoal.correctAnswer.forEach { char ->
                                    letterUsage[char] = letterUsage.getOrDefault(char, 0) + 1
                                }
                            }
                            .padding(4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Kembali",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        validateAnswer(selectedLetters)
                    }
                    .padding(4.dp)
            )
        }
    }

    if (isPopupVisible.value) {
        Dialog(
            onDismissRequest = { isPopupVisible.value = false }
        ) {
            PopupOverview(
                onClick = {
                    isPopupVisible.value = false
                    if (isCorrectAnswer.value) {
                        nextSoal()
                    }
                },
                border = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                background = MaterialTheme.colorScheme.primary.toArgb(),
                text = if (isCorrectAnswer.value) "Yey, kamu berhasil menjawab dengan benar" else "Yah, kamu belum berhasil menjawab dengan benar",
                image = if (isCorrectAnswer.value) R.drawable.boy_2 else R.drawable.boy_4,
                message = if (isCorrectAnswer.value) "Lanjutkan" else "Coba lagi"
            )
        }
    }

    if (isEndPopupVisible.value) {
        Dialog(
            onDismissRequest = { isEndPopupVisible.value = false }
        ) {
            PopupOverview(
                onClick = {
                    isEndPopupVisible.value = false
                    navHostController.popBackStack() // Kembali ke layar sebelumnya
                },
                border = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                background = MaterialTheme.colorScheme.primary.toArgb(),
                text = "Yey, kamu telah menyelesaikan semua soal dengan baik!",
                image = R.drawable.boy_2,
                message = "Selesai"
            )
        }
    }
}
