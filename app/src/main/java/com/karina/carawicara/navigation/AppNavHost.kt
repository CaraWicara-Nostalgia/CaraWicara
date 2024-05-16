package com.karina.carawicara.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.karina.carawicara.R
import com.karina.carawicara.ui.screen.susunKata.SusunKataPage
import com.karina.carawicara.ui.screen.home.HomePage
import com.karina.carawicara.ui.screen.pustakaWicara.PustakaWicaraDetailPage
import com.karina.carawicara.ui.screen.pustakaWicara.PustakaWicaraPage
import com.karina.carawicara.ui.screen.onboarding.OnBoardingPage
import com.karina.carawicara.ui.screen.paduGambar.PaduGambarPage
import com.karina.carawicara.ui.screen.suaraPintar.SuaraPintarPage

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "homePage") {
        composable("homePage") { HomePage(navController) }
        composable("susunKataPage") { SusunKataPage(R.drawable.kucing_2, navController) }
        composable("onboardingPage") { OnBoardingPage(navController) }
        composable("suaraPintarPage") { SuaraPintarPage(R.drawable.kucing_2, navController) }
        composable("paduGambarPage") { PaduGambarPage(navController) }
        composable("pustakaWicaraPage") { PustakaWicaraPage(navController) }
        composable("pustakaWicaraDetailPage") { PustakaWicaraDetailPage(navController) }
    }
}