package com.karina.carawicara.ui.screen.susunKata

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonAlphabet
import com.karina.carawicara.ui.component.ButtonNav
import com.karina.carawicara.ui.component.StageBox
import androidx.compose.ui.window.Dialog
import com.karina.carawicara.ui.component.PopupOverview

@Composable
fun SusunKataPage(
    image: Int,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val selectedLetters = remember { mutableStateListOf<String>() }
    val isCorrectAnswer = remember { mutableStateOf(false) }
    val isPopupVisible = remember { mutableStateOf(false) }

    fun validateAnswer(letters: List<String>) {
        if (letters.joinToString("") == "KUCING") {
            isCorrectAnswer.value = true
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
                ButtonNav(
                    onClick = { navHostController.popBackStack() },
                    icon = R.drawable.ic_x,
                    iconColor = Color.White.toArgb(),
                    borderColor = MaterialTheme.colorScheme.errorContainer.toArgb(),
                    backgroundColor = MaterialTheme.colorScheme.error.toArgb(),
                    enabled = true
                )
                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                    StageBox(stage = 0, onClick = { /* Handle click here */ })
                    StageBox(stage = 0, onClick = { /* Handle click here */ })
                    StageBox(stage = 0, onClick = { /* Handle click here */ })
                    StageBox(stage = 0, onClick = { /* Handle click here */ })
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color(0xFFFEEFD4))
                    .border(2.dp, Color(0xFFFEE4B7))
                    .padding(4.dp)
            ) {
                Text(
                    text = "susunlah huruf dibawah untuk menjawab",
                    fontSize = 20.sp,
                    color = Color(0xFFFCB028),
                    textAlign = TextAlign.Center
                )
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
                    painter = painterResource(image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(279.dp, 300.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row (
                modifier = Modifier.padding(8.dp)
            ){
                ButtonAlphabet(onClick = { /*TODO*/ }, text = selectedLetters.getOrNull(0) ?: "")
                Spacer(modifier = Modifier.width(4.dp))
                ButtonAlphabet(onClick = { /*TODO*/ }, text = selectedLetters.getOrNull(1) ?: "")
                Spacer(modifier = Modifier.width(4.dp))
                ButtonAlphabet(onClick = { /*TODO*/ }, text = selectedLetters.getOrNull(2) ?: "")
                Spacer(modifier = Modifier.width(4.dp))
                ButtonAlphabet(onClick = { /*TODO*/ }, text = selectedLetters.getOrNull(3) ?: "")
                Spacer(modifier = Modifier.width(4.dp))
                ButtonAlphabet(onClick = { /*TODO*/ }, text = selectedLetters.getOrNull(4) ?: "")
                Spacer(modifier = Modifier.width(4.dp))
                ButtonAlphabet(onClick = { /*TODO*/ }, text = selectedLetters.getOrNull(5) ?: "")
            }
            Spacer(modifier = Modifier.weight(1f))
            Divider()
            Spacer(modifier = Modifier.weight(1f))
            Row {
                Column {
                    Row (
                        modifier = Modifier.padding(8.dp)
                    ){
                        ButtonAlphabet(onClick = { selectedLetters.add("U") }, text = "U", enabled = !selectedLetters.contains("U"))
                        Spacer(modifier = Modifier.width(8.dp))
                        ButtonAlphabet(onClick = { selectedLetters.add("K") }, text = "K", enabled = !selectedLetters.contains("K"))
                        Spacer(modifier = Modifier.width(8.dp))
                        ButtonAlphabet(onClick = { selectedLetters.add("N") }, text = "N", enabled = !selectedLetters.contains("N"))
                        Spacer(modifier = Modifier.width(8.dp))
                        ButtonAlphabet(onClick = { selectedLetters.add("D") }, text = "D", enabled = !selectedLetters.contains("D"))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row (
                        modifier = Modifier.padding(8.dp)
                    ){
                        ButtonAlphabet(onClick = { selectedLetters.add("C") }, text = "C", enabled = !selectedLetters.contains("C"))
                        Spacer(modifier = Modifier.width(8.dp))
                        ButtonAlphabet(onClick = { selectedLetters.add("G") }, text = "G", enabled = !selectedLetters.contains("G"))
                        Spacer(modifier = Modifier.width(8.dp))
                        ButtonAlphabet(onClick = { selectedLetters.add("I") }, text = "I", enabled = !selectedLetters.contains("I"))
                        Spacer(modifier = Modifier.width(8.dp))
                        ButtonAlphabet(onClick = { selectedLetters.add("A") }, text = "A", enabled = !selectedLetters.contains("A"))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column (
                    modifier = Modifier.padding(8.dp)
                ){
                    ButtonNav(
                        onClick = { selectedLetters.clear() },
                        icon = R.drawable.ic_restart,
                        iconColor = Color.Black.toArgb(),
                        borderColor = Color.DarkGray.toArgb(),
                        backgroundColor = Color.White.toArgb(),
                        enabled = selectedLetters.isNotEmpty()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ButtonNav(
                        onClick = { validateAnswer(selectedLetters) },
                        icon = R.drawable.ic_arrow_forward,
                        iconColor = Color.White.toArgb(),
                        borderColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                        backgroundColor = MaterialTheme.colorScheme.primary.toArgb(),
                        enabled = selectedLetters.size == 6
                    )
                }
            }
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
                        navHostController.popBackStack()
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
}
