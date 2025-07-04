package com.karina.carawicara.ui.screen.flashcard

import android.app.Application
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.karina.carawicara.R
import kotlinx.coroutines.delay
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KosakataExerciseDetailPage(
    navController: NavController,
    category: String? = null,
    viewModel: KosakataExerciseViewModel = viewModel(
        factory = KosakataExerciseViewModelFactory(
            application = LocalContext.current.applicationContext as Application
        )
    )
) {
    val currentFlashcards by viewModel.currentFlashcards.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val isExerciseCompleted by viewModel.isExerciseCompleted.collectAsState()
    val score by viewModel.score.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "card_flip"
    )

    // Reset flip state when index changes
    LaunchedEffect(currentIndex) {
        isFlipped = false
    }

    // Set current category when page is loaded
    LaunchedEffect(category) {
        Log.d("KosakataExerciseDetailPage", "Category from navigation: $category")
        if (!category.isNullOrEmpty() && currentFlashcards.isEmpty()) {
            Log.d("KosakataExerciseDetailPage", "Setting current category: $category")
            viewModel.setCurrentCategory(category)
        } else if (currentFlashcards.isNotEmpty()) {
            viewModel.resetLoadingState()
        }
    }

    LaunchedEffect(Unit) {
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
        savedStateHandle?.get<Boolean>("fromAssessment")?.let { fromAssessment ->
            if (fromAssessment) {
                Log.d("KosakataExerciseDetailPage", "Navigated from Assessment Page")
                viewModel.moveToNextCard()
                viewModel.resetLoadingState()
                savedStateHandle.remove<Boolean>("fromAssessment")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = category ?: "Kosakata") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (currentFlashcards.isNotEmpty()) {
                        IconButton(onClick = { viewModel.shuffleCards() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_refresh),
                                contentDescription = "Shuffle"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Loading indicator
            AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // Error state
            AnimatedVisibility(
                visible = !isLoading && currentFlashcards.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = Color.Red,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Tidak ada flashcard tersedia",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Kategori: ${category ?: "unknown"}",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )

                        if (!errorMessage.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = errorMessage!!,
                                fontSize = 14.sp,
                                color = Color.Red,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if (!category.isNullOrEmpty()) {
                                    viewModel.setCurrentCategory(category)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4285F4)
                            )
                        ) {
                            Text("Coba Lagi")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.logDebugInfo() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray
                            )
                        ) {
                            Text("Debug Info (Dev Only)")
                        }
                    }
                }
            }

            // Content when flashcards are available
            AnimatedVisibility(
                visible = !isLoading && currentFlashcards.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
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
                            text = "${currentIndex + 1}/${currentFlashcards.size}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Get current card safely
                    val currentCard = if (currentFlashcards.isNotEmpty() && currentIndex < currentFlashcards.size) {
                        currentFlashcards[currentIndex]
                    } else {
                        null
                    }

                    if (currentCard != null) {
                        // Kartu yang bisa di-flip
                        Box(
                            modifier = Modifier
                                .weight(1f)
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
                                        val context = LocalContext.current

                                        val bitmap = remember {
                                            try {
                                                val assetManager = context.assets
                                                Log.d("KosakataExerciseDetailPage", "Loading image: ${currentCard.imageRes}")
                                                val inputStream = assetManager.open(currentCard.imageRes)
                                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                                if (bitmap == null) {
                                                    Log.e("KosakataExerciseDetailPage", "Failed to decode bitmap for: ${currentCard.imageRes}")
                                                } else {
                                                    Log.d("KosakataExerciseDetailPage", "Successfully loaded image: ${currentCard.imageRes}")
                                                }
                                                bitmap
                                            } catch (e: IOException) {
                                                Log.e("KosakataExerciseDetailPage", "Error loading image: ${currentCard.imageRes}", e)
                                                null
                                            }
                                        }

                                        bitmap?.let {
                                            Image(
                                                bitmap = it.asImageBitmap(),
                                                contentDescription = "Flashcard image",
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                        } ?: run {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center,
                                                modifier = Modifier.padding(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Warning,
                                                    contentDescription = "Image not found",
                                                    modifier = Modifier.size(64.dp),
                                                    tint = Color.Gray
                                                )

                                                Spacer(modifier = Modifier.height(16.dp))

                                                Text(
                                                    text = "Gambar tidak ditemukan",
                                                    fontSize = 16.sp,
                                                    color = Color.Gray,
                                                    textAlign = TextAlign.Center
                                                )

                                                Spacer(modifier = Modifier.height(8.dp))

                                                Text(
                                                    text = currentCard.imageRes,
                                                    fontSize = 12.sp,
                                                    color = Color.Gray,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    // Tampilan belakang (Teks)
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
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(24.dp)
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

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Tombol BENAR
                            OutlinedButton(
                                onClick = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("fromAssessment", true)
                                    val currentCards = currentFlashcards[currentIndex]
                                    navController.navigate(
                                        "cardAssessmentPage/${currentCards.word}/$currentIndex/${currentFlashcards.size}/true"
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
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
                                    navController.currentBackStackEntry?.savedStateHandle?.set("fromAssessment", true)
                                    val currentCards = currentFlashcards[currentIndex]
                                    navController.navigate(
                                        "cardAssessmentPage/${currentCards.word}/$currentIndex/${currentFlashcards.size}/false"
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
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
        }
    }
}