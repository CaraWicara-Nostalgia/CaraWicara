package com.karina.carawicara.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.karina.carawicara.ui.screen.library.LibraryDetailPage
import com.karina.carawicara.ui.screen.library.LibraryPage

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "libraryPage") {
        composable("libraryPage") { LibraryPage(navController) }
        composable("libraryDetailPage") { LibraryDetailPage(navController) }
    }
}