package com.karina.carawicara.ui.screen.flashcard

import android.graphics.BitmapFactory
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.karina.carawicara.data.SequenceExerciseItem
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SequenceExerciseDetailPage(
    navController: NavController,
    categoryTitle: String? = null,
    viewModel: SequenceExerciseViewModel = viewModel(
        factory = SequenceExerciseViewModelFactory(
            application = LocalContext.current.applicationContext as android.app.Application
        )
    )
){
    val currentCategory by viewModel.currentCategory.collectAsState()
    val sequenceItems by viewModel.sequenceItems.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val selectedImages by viewModel.selectedImages.collectAsState()
    val isExerciseCompleted by viewModel.isExerciseCompleted.collectAsState()
    val score by viewModel.score.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Get context here, outside of any lambda
    val context = LocalContext.current
    val assetManager = context.assets

    var isLoading by remember { mutableStateOf(true) }

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
        isLoading = true
        if (!categoryTitle.isNullOrEmpty() && categoryTitle != currentCategory) {
            viewModel.setCurrentCategory(categoryTitle)
        }
    }

    LaunchedEffect(sequenceItems) {
        if (sequenceItems.isNotEmpty()) {
            isLoading = false
        }
    }

    LaunchedEffect(currentIndex) {
        if (sequenceItems.isNotEmpty() && currentIndex < sequenceItems.size) {
            // Fix images if needed before shuffling
            viewModel.fixCurrentSequenceImagesIfNeeded()
            viewModel.shuffleCurrentSequence()
        }
    }

    LaunchedEffect(isExerciseCompleted) {
        if (isExerciseCompleted) {
            // Navigate to result page
            navController.navigate("therapyResultPage/$score/${sequenceItems.size}")
            viewModel.resetExercise()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = categoryTitle ?: "Sequence Exercise") },
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Loading state
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            // Error state
            else if (sequenceItems.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = Color.Red,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Tidak ada sequence tersedia",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    if (!errorMessage.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = errorMessage!!,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (!categoryTitle.isNullOrEmpty()) {
                                viewModel.setCurrentCategory(categoryTitle)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4285F4)
                        )
                    ) {
                        Text("Coba Lagi")
                    }

                    if (true) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.logDebugInfo() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray
                            )
                        ) {
                            Text("Debug Info (Dev Only)")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                try {
                                    Log.d("DEBUG", "Root assets: ${assetManager.list("")?.joinToString()}")

                                    if (assetManager.list("")?.contains("images") == true) {
                                        Log.d("DEBUG", "Images directory: ${assetManager.list("images")?.joinToString()}")

                                        if (assetManager.list("images")?.contains("sequence") == true) {
                                            Log.d("DEBUG", "Sequence images: ${assetManager.list("images/sequence")?.joinToString()}")
                                        }
                                    }

                                    if (assetManager.list("")?.contains("database") == true) {
                                        Log.d("DEBUG", "Database directory: ${assetManager.list("database")?.joinToString()}")

                                        if (assetManager.list("database")?.contains("sequence.json") == true) {
                                            val json = assetManager.open("database/sequence.json").bufferedReader().use { it.readText() }
                                            Log.d("DEBUG", "Sequence.json content: $json")
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("DEBUG", "Error listing assets", e)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Blue
                            )
                        ) {
                            Text("Debug Assets")
                        }
                    }
                }
            }
            else if (currentIndex < sequenceItems.size) {
                val currentItem = sequenceItems[currentIndex]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (true) {
                        OutlinedButton(
                            onClick = {
                                viewModel.debugImagePaths()
                                viewModel.fixCurrentSequenceImagesIfNeeded()
                                viewModel.shuffleCurrentSequence()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red
                            )
                        ) {
                            Text("Force Refresh Images")
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(Color.LightGray.copy(alpha = 0.2f))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Debug Info",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )

                            Text(
                                text = "Current item: ${currentItem.title}",
                                fontSize = 10.sp
                            )

                            Text(
                                text = "Image paths:",
                                fontSize = 10.sp
                            )

                            currentItem.images.forEachIndexed { index, path ->
                                Text(
                                    text = "[$index] $path",
                                    fontSize = 8.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

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

                    // Grid of images options - we'll use a fixed 2x2 grid for 4 images
                    val imageSize = 140.dp

                    // First row of images
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Use the corrected image paths
                        val correctedPaths = viewModel.getCorrectImagePathsForTitle(currentItem.title)

                        // First image (top-left)
                        if (correctedPaths.size > 0) {
                            ImageOptionAsset(
                                imagePath = correctedPaths[0],
                                isSelected = selectedImages.contains(0),
                                onSelect = { viewModel.selectImage(0) },
                                modifier = Modifier.size(imageSize)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Second image (top-right)
                        if (correctedPaths.size > 1) {
                            ImageOptionAsset(
                                imagePath = correctedPaths[1],
                                isSelected = selectedImages.contains(1),
                                onSelect = { viewModel.selectImage(1) },
                                modifier = Modifier.size(imageSize)
                            )
                        }
                    }

                    // Second row of images
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val correctedPaths = viewModel.getCorrectImagePathsForTitle(currentItem.title)

                        // Third image (bottom-left)
                        if (correctedPaths.size > 2) {
                            ImageOptionAsset(
                                imagePath = correctedPaths[2],
                                isSelected = selectedImages.contains(2),
                                onSelect = { viewModel.selectImage(2) },
                                modifier = Modifier.size(imageSize)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Fourth image (bottom-right)
                        if (correctedPaths.size > 3) {
                            ImageOptionAsset(
                                imagePath = correctedPaths[3],
                                isSelected = selectedImages.contains(3),
                                onSelect = { viewModel.selectImage(3) },
                                modifier = Modifier.size(imageSize)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Row with numbers and selected images
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0 until currentItem.images.size) {
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

                                // Selected image slot or empty slot
                                SequenceSlot(
                                    selectedIndex = selectedImages.getOrNull(i),
                                    currentItem = currentItem,
                                    viewModel = viewModel,
                                    onClear = { viewModel.clearSelection(i) },
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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
                                // Check if all slots are filled
                                val allSlotsFilled = selectedImages.none { it == null }
                                if (allSlotsFilled) {
                                    // Check if the order is correct and move to the next question
                                    viewModel.submitAnswer()
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
            } else {
                // No more items
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tidak ada soal tersedia")
                }
            }
        }
    }
}

@Composable
fun ImageOptionAsset(
    imagePath: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Use key to force recomposition when the image path changes
    key(imagePath) {
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
                .clickable(enabled = !isSelected) { if (!isSelected) onSelect() },
            contentAlignment = Alignment.Center
        ) {
            val bitmap = remember(imagePath) {
                try {
                    val assetManager = context.assets
                    Log.d("SequenceExerciseDetailPage", "Loading image: $imagePath")
                    val inputStream = assetManager.open(imagePath)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    if (bitmap == null) {
                        Log.e("SequenceExerciseDetailPage", "Failed to decode bitmap for: $imagePath")
                    } else {
                        Log.d("SequenceExerciseDetailPage", "Successfully loaded image: $imagePath")
                    }
                    bitmap
                } catch (e: IOException) {
                    Log.e("SequenceExerciseDetailPage", "Error loading image: $imagePath", e)

                    // Try with simplified path
                    try {
                        val simplePath = imagePath.substringAfterLast("/")
                        Log.d("SequenceExerciseDetailPage", "Trying with simpler path: $simplePath")
                        val inputStream = context.assets.open(simplePath)
                        BitmapFactory.decodeStream(inputStream)
                    } catch (e2: IOException) {
                        Log.e("SequenceExerciseDetailPage", "Also failed with simpler path", e2)
                        null
                    }
                }
            }

            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Sequence image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .alpha(if (isSelected) 0.5f else 1f)
                )
            } ?: run {
                // Fallback if image couldn't be loaded
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Image not found",
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Gambar tidak tersedia",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun SequenceSlot(
    selectedIndex: Int?,
    currentItem: SequenceExerciseItem,
    viewModel: SequenceExerciseViewModel,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
        if (selectedIndex == null) {
            return@Box
        }

        // Use corrected image paths
        val correctedPaths = viewModel.getCorrectImagePathsForTitle(currentItem.title)

        // Validate the index is within range
        if (selectedIndex < 0 || selectedIndex >= correctedPaths.size) {
            Log.e("SequenceSlot", "Invalid image index: $selectedIndex, max: ${correctedPaths.size-1}")
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .background(Color.Red.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error",
                    fontSize = 10.sp,
                    color = Color.White
                )
            }
            return@Box
        }

        // Get the correct image path for the selected index
        val imagePath = correctedPaths[selectedIndex]
        Log.d("SequenceSlot", "Showing selected image at index $selectedIndex with path: $imagePath")

        // Try to load the bitmap
        var bitmap: android.graphics.Bitmap? = null
        try {
            val assetManager = context.assets
            val inputStream = assetManager.open(imagePath)
            bitmap = BitmapFactory.decodeStream(inputStream)
            if (bitmap == null) {
                Log.e("SequenceSlot", "Failed to decode bitmap for: $imagePath")
            } else {
                Log.d("SequenceSlot", "Successfully loaded image: $imagePath")
            }
        } catch (e: IOException) {
            Log.e("SequenceSlot", "Error loading image: $imagePath", e)

            // Try with simplified path
            try {
                val simplePath = imagePath.substringAfterLast("/")
                Log.d("SequenceSlot", "Trying with simpler path: $simplePath")
                val inputStream = context.assets.open(simplePath)
                bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap != null) {
                    Log.d("SequenceSlot", "Successfully loaded with simpler path: $simplePath")
                }
            } catch (e2: IOException) {
                Log.e("SequenceSlot", "Also failed with simpler path", e2)
            }
        }

        // Display the bitmap or fallback text
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Selected image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = imagePath.substringAfterLast('/'),
                        fontSize = 8.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Clear button
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

fun Modifier.alpha(alpha: Float): Modifier {
    return this.then(Modifier.graphicsLayer(alpha = alpha))
}