package com.karina.carawicara.ui.screen.flashcard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SequenceExerciseDetailPage(
    navController: NavController,
    categoryTitle: String? = null,
    viewModel: SequenceExerciseViewModel = viewModel(factory = SequenceExerciseViewModelFactory())
){
    val currentCategory by viewModel.currentCategory.collectAsState()
    val sequenceItems by viewModel.sequenceItems.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val selectedImages by viewModel.selectedImages.collectAsState()
    val isExerciseCompleted by viewModel.isExerciseCompleted.collectAsState()
    val score by viewModel.score.collectAsState()

    // Handle lifecycle events to ensure data is loaded
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // Check if we need to load category
                if (currentCategory.isEmpty() && !categoryTitle.isNullOrEmpty()) {
                    viewModel.setCurrentCategory(categoryTitle)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(categoryTitle) {
        if (!categoryTitle.isNullOrEmpty() && categoryTitle != currentCategory) {
            viewModel.setCurrentCategory(categoryTitle)
        }
    }

    LaunchedEffect(isExerciseCompleted) {
        if (isExerciseCompleted) {
            // Navigate to result page
            navController.navigate("therapyResultPage/$score/${sequenceItems.size}")
            viewModel.resetExercise()
        }
    }

    if (sequenceItems.isEmpty() || currentIndex >= sequenceItems.size) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Tidak ada soal tersedia")
        }
        return
    }

    val currentItem = sequenceItems[currentIndex]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Empty title area */ },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress counter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentIndex + 1}/${sequenceItems.size}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Instruction text
            Text(
                text = currentItem.title,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Grid of image options
            val imageSize = 160.dp

            // 2x2 grid of images to choose from
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // First row of images
                Column {
                    // Image 1
                    ImageOption(
                        imageRes = currentItem.images[0],
                        isSelected = selectedImages.contains(0),
                        onSelect = { viewModel.selectImage(0) },
                        modifier = Modifier.size(imageSize)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Image 2
                    ImageOption(
                        imageRes = currentItem.images[1],
                        isSelected = selectedImages.contains(1),
                        onSelect = { viewModel.selectImage(1) },
                        modifier = Modifier.size(imageSize)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Second row of images
                Column {
                    // Image 3
                    ImageOption(
                        imageRes = currentItem.images[2],
                        isSelected = selectedImages.contains(2),
                        onSelect = { viewModel.selectImage(2) },
                        modifier = Modifier.size(imageSize)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Image 4
                    ImageOption(
                        imageRes = currentItem.images[3],
                        isSelected = selectedImages.contains(3),
                        onSelect = { viewModel.selectImage(3) },
                        modifier = Modifier.size(imageSize)
                    )
                }
            }

            // Row with numbers and selected images
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until 4) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Number
                        Text(
                            text = "${i + 1}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Selected image or empty slot
                        SelectedImageSlot(
                            selectedImageIndex = selectedImages[i],
                            images = currentItem.images,
                            onClear = { viewModel.clearSelection(i) },
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            }

            // Row with action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Reset button
                OutlinedButton(
                    onClick = { viewModel.resetSelections() },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Reset",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Next button
                OutlinedButton(
                    onClick = {
                        // Periksa apakah semua slot sudah terisi
                        if (!selectedImages.contains(null)) {
                            // Check if the order is correct and move to the next question
                            if (viewModel.checkOrder()) {
                                viewModel.handleCorrectAnswer()
                            } else {
                                viewModel.handleWrongAnswer()
                            }
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
                        text = "Next",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun ImageOption(
    imageRes: Int,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color.LightGray.copy(alpha = 0.5f) else Color.White)
            .clickable(enabled = !isSelected) { if (!isSelected) onSelect() }
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Sequence image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .alpha(if (isSelected) 0.5f else 1f)
        )
    }
}

@Composable
fun SelectedImageSlot(
    selectedImageIndex: Int?,
    images: List<Int>,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(4.dp)
            )
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageIndex != null && selectedImageIndex < images.size) {
            // Show selected image
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = images[selectedImageIndex]),
                    contentDescription = "Selected image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )

                // Clear button in top-right corner
                IconButton(
                    onClick = onClear,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear selection",
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

fun Modifier.alpha(alpha: Float): Modifier {
    return this.then(Modifier.graphicsLayer(alpha = alpha))
}

@Preview(showBackground = true)
@Composable
fun SequenceExerciseDetailPagePreview() {
    // Preview with dummy data
    val navController = NavController(context = LocalContext.current)
    SequenceExerciseDetailPage(
        navController = navController,
        categoryTitle = "Dummy Category"
    )
}
