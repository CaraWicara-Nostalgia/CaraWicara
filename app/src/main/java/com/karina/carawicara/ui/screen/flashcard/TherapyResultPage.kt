package com.karina.carawicara.ui.screen.flashcard

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.karina.carawicara.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TherapyResultPage(
    navController: NavController,
    score: Int = 3,
    totalQuestions: Int = 10,
){
    var withHelp by remember { mutableStateOf(false) }
    var independent by remember { mutableStateOf(false) }
    var needsRepetition by remember { mutableStateOf(false) }
    var fullSpirit by remember { mutableStateOf(false) }

    var selectedMood by remember { mutableStateOf<String?>(null) }

    var additionalNotes by remember { mutableStateOf("") }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = {navController.navigate("flashcardPage")}) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                        )
                    }
                }
            )
        }
    ){ paddingValues ->
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

            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ){
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
                    placeholder = { Text("Tambahkan catatan detail di sini ...")},
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

            OutlinedButton(
                onClick = { },
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

            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Simpan Capaian",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
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

@Preview(showBackground = true)
@Composable
fun TherapyResultScreenPreview() {
    TherapyResultPage(
        navController = rememberNavController(),
        score = 7,
        totalQuestions = 10
    )
}
