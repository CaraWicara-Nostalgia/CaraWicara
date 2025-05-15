package com.karina.carawicara.ui.screen.flashcard

import android.app.Application
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.karina.carawicara.ui.component.ExerciseCard
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardPage(
    navController: NavController,
    flashcardViewModel: FlashcardViewModel = viewModel(
        factory = FlashcardViewModelFactory(
            application = LocalContext.current.applicationContext as Application
        )
    )
) {

    val pelafalanProgress by flashcardViewModel.pelafalanProgress.collectAsState()
    val kosakataProgress by flashcardViewModel.kosakataProgress.collectAsState()
    val sequenceProgress by flashcardViewModel.sequenceProgress.collectAsState()

    LaunchedEffect(Unit) {
        flashcardViewModel.loadAllProgress()
    }

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
                    IconButton(onClick = {navController.navigate("homePage")}) {
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
               progress = "${pelafalanProgress.completed}/${pelafalanProgress.total} exercise",
               progressValue = pelafalanProgress.ratio,
               onClick = {
                   navController.navigate("pelafalanExercisePage")
               }
           )

            Spacer(modifier = Modifier.height(16.dp))

            ExerciseCard(
                title = "Kosakata",
                description = "Belajar kata yang baru",
                progress = "${kosakataProgress.completed}/${kosakataProgress.total} exercise",
                progressValue = kosakataProgress.ratio,
                onClick = {
                    navController.navigate("kosakataExercisePage")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExerciseCard(
                title = "Sequence",
                description = "Belajar urutan aktivitas",
                progress = "${sequenceProgress.completed}/${sequenceProgress.total} exercise",
                progressValue = sequenceProgress.ratio,
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