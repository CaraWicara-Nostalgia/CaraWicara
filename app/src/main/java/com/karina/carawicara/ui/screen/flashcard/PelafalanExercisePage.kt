package com.karina.carawicara.ui.screen.flashcard

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ExerciseItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PelafalanExercisePage(
    navController: NavController,
    viewModel: PelafalanExerciseViewModel = viewModel(factory = PelafalanExerciseViewModelFactory(
        application = LocalContext.current.applicationContext as Application,
    ))
){
    val categories by viewModel.categories.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pelafalan",
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedCard (
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.Start),
                shape = RoundedCornerShape(8.dp),
            ){
                Row (
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_exercise),
                        contentDescription = "Exercise Counter",
                        modifier = Modifier.padding(end = 8.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "${categories.size} exercise",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            categories.forEach { category ->
                ExerciseItemCard(
                    title = category.title,
                    progressPercentage = category.progressPercentage,
                    onClick = {
                        val categoryId = category.id
                        Log.d("PelafalanExercisePage", "Category ID: $categoryId")

                        viewModel.setCurrentCategory(categoryId)
                        navController.navigate("pelafalanExerciseDetailPage/$categoryId")
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PelafalanExercisePagePreview() {
    PelafalanExercisePage(navController = rememberNavController())
}