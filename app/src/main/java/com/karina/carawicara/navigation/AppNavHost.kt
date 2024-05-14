package com.karina.carawicara.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.karina.carawicara.R
import com.karina.carawicara.ui.screen.completeTheWord.CompleteTheWordPage
import com.karina.carawicara.ui.screen.imageMatching.ImageMathcing
import com.karina.carawicara.ui.screen.library.LibraryDetailPage
import com.karina.carawicara.ui.screen.library.LibraryPage
import com.karina.carawicara.ui.screen.onboarding.OnBoardingPage
import com.karina.carawicara.ui.screen.voiceRecognition.VoiceRecognitionPage

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "CompleteTheWordPage") {
        composable("CompleteTheWordPage") { CompleteTheWordPage(R.drawable.kucing_2) }
        composable("OnBoardingPage") { OnBoardingPage(navController) }
        composable("VoiceRecognitionPage") { VoiceRecognitionPage(R.drawable.kucing_2) }
        composable("ImageMatching") { ImageMathcing(navController) }
        composable("libraryPage") { LibraryPage(navController) }
        composable("libraryDetailPage") { LibraryDetailPage(navController) }
    }
}