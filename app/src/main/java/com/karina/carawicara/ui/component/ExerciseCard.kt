package com.karina.carawicara.ui.component

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.karina.carawicara.R
import com.karina.carawicara.ui.screen.flashcard.PelafalanExerciseViewModel
import com.karina.carawicara.ui.screen.flashcard.PelafalanExerciseViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCard(
    title: String,
    description: String,
    totalExercise: String,
//    progress: String,
//    progressValue: Float,
    onClick: () -> Unit = {},
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = onClick
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (
                modifier = Modifier.weight(1f)
            ){
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = totalExercise,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
//                Text(
//                    text = progress,
//                    fontSize = 12.sp,
//                    color = Color.Black
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                LinearProgressIndicator(
//                    progress = progressValue,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(6.dp),
//                    color = Color(0xFF4A73B9), // Warna biru
//                    trackColor = Color.LightGray,
//                )
            }
            Spacer(modifier = Modifier.width(32.dp))
            Image(
                painter = painterResource(R.drawable.boy_3),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseCardPreview(){
    ExerciseCard(
        title = "Pelafalan",
        description = "Belajar mengucap dengan benar",
        totalExercise = "5 Exercise",
    )
}