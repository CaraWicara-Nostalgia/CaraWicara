package com.karina.carawicara.ui.screen.flashcard

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.karina.carawicara.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PelafalanExerciseDetailPage(
    navController: NavHostController,
    category: String? = null,
    viewModel: PelafalanExerciseViewModel = viewModel(
        factory = PelafalanExerciseViewModelFactory()
    )
) {
    val flashcards by viewModel.flashcards.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()

    var isFlipped by remember { mutableStateOf(false) }

    val isExerciseCompleted by viewModel.isExerciseCompleted.collectAsState()
    val score by viewModel.score.collectAsState()
    val totalCards = flashcards.size
    var completedAnswers by remember { mutableIntStateOf(0) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    )

    LaunchedEffect(currentIndex) {
        isFlipped = false
    }

    LaunchedEffect(isExerciseCompleted) {
        if (isExerciseCompleted) {
            navController.navigate("therapyResultPage/${score}/${totalCards}"){
                popUpTo("pelafalanExerciseDetailPage/{initialCardindex}") {
                    inclusive = true
                }
            }

            viewModel.resetExercise()
        }
    }

    LaunchedEffect(category) {
        if (!category.isNullOrEmpty()) {
            println("Setting category from navigation: $category")
            viewModel.setCurrentCategory(category)
        }
    }

    if (flashcards.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Tidak ada flashcard tersedia")
        }
        return
    }

    val currentCard = flashcards[currentIndex]

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
//                    Text(
//                        text = "${currentIndex + 1}/${flashcards.size}",
//                        fontWeight = FontWeight.Medium
//                    )
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
                    IconButton(onClick = { viewModel.shuffleCards() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_refresh),
                            contentDescription = "Shuffle"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentIndex + 1}/${flashcards.size}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Kartu yang bisa di-flip
            Box(
                modifier = Modifier
                    .aspectRatio(0.9f)
                    .fillMaxWidth()
                    .clickable { isFlipped = !isFlipped }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                        },
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                ) {
                    // Sisi depan dan belakang kartu
                    if (rotation < 90f) {
                        // Tampilan depan (Gambar)
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = currentCard.imageRes),
                                contentDescription = "Flashcard image",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(200.dp)
                                    .padding(24.dp)
                            )
                        }
                    } else {
                        // Tampilan belakang (Teks)
                        // Gunakan graphicsLayer untuk membalik teks agar tidak terbalik
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    rotationY = 180f
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = currentCard.pronunciation,
                                    fontSize = 24.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = currentCard.word,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            Box(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tombol BENAR
                OutlinedButton(
                    onClick = {
                        viewModel.handleCorrectAnswer()
                        completedAnswers++

                        if (completedAnswers >= totalCards) {
                            navController.navigate("therapyResultPage/${score}/${totalCards}")
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "BENAR",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Tombol SALAH
                OutlinedButton(
                    onClick = {
                        viewModel.handleWrongAnswer()
                        completedAnswers++

                        if (completedAnswers >= totalCards) {
                            navController.navigate("therapyResultPage/${score}/${totalCards}")
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "SALAH",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PelafalanExerciseDetailPagePreview(){
    PelafalanExerciseDetailPage(
        navController = rememberNavController()
    )
}