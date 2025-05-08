package com.karina.carawicara.ui.screen.flashcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.karina.carawicara.ui.component.ExerciseCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardPage(
    navController: NavController
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Flashcard",
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ){
           Spacer(modifier = Modifier.height(16.dp))

           ExerciseCard(
               title = "Pelafalan",
               description = "Belajar mengucap dengan benar",
               progress = "2/5 exercise",
               progressValue = 0.4f,
               onClick = {
                   navController.navigate("pelafalanExercisePage")
               }
           )

            Spacer(modifier = Modifier.height(16.dp))

            ExerciseCard(
                title = "Kosakata",
                description = "Belajar kata yang baru",
                progress = "3/5 exercise",
                progressValue = 0.6f,
                onClick = {
                    navController.navigate("kosakataExercisePage")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExerciseCard(
                title = "Sequence",
                description = "Belajar urutan aktivitas",
                progress = "1/5 exercise",
                progressValue = 0.2f,
                onClick = {
                    navController.navigate("sequenceExercisePage")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlashCardPagePreview(){
    FlashcardPage(navController = rememberNavController())
}