package com.karina.carawicara.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.karina.carawicara.R
import com.karina.carawicara.ui.screen.Profile.ProfilePage
import com.karina.carawicara.ui.screen.SplashScreen
import com.karina.carawicara.ui.screen.auth.LoginPage
import com.karina.carawicara.ui.screen.flashcard.FlashcardPage
import com.karina.carawicara.ui.screen.home.HomePage
import com.karina.carawicara.ui.screen.kenaliAku.KenaliAkuPage
import com.karina.carawicara.ui.screen.kenaliAku.KenaliAkuRecordPage
import com.karina.carawicara.ui.screen.kenaliAku.KenaliAkuResultPage
import com.karina.carawicara.ui.screen.auth.OnBoardingPage
import com.karina.carawicara.ui.screen.auth.RegisterPage
import com.karina.carawicara.ui.screen.flashcard.KosakataExerciseDetailPage
import com.karina.carawicara.ui.screen.flashcard.KosakataExercisePage
import com.karina.carawicara.ui.screen.flashcard.PelafalanExerciseDetailPage
import com.karina.carawicara.ui.screen.flashcard.PelafalanExercisePage
import com.karina.carawicara.ui.screen.flashcard.SequenceExercisePage
import com.karina.carawicara.ui.screen.flashcard.TherapyResultPage
import com.karina.carawicara.ui.screen.paduGambar.PaduGambarPage
import com.karina.carawicara.ui.screen.patient.PatientPage
import com.karina.carawicara.ui.screen.pustakaWicara.PustakaWicaraDetailPage
import com.karina.carawicara.ui.screen.pustakaWicara.PustakaWicaraPage
import com.karina.carawicara.ui.screen.suaraPintar.SuaraPintarPage
import com.karina.carawicara.ui.screen.suaraPintar.SuaraPintarRecordPage
import com.karina.carawicara.ui.screen.susunKata.SusunKataPage

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "splashScreen") {
        composable("splashScreen") { SplashScreen(navController) }

        composable("homePage") { HomePage(navController) }

        composable("patientPage") { PatientPage(navController) }

        composable("profilePage") { ProfilePage(navController) }

        composable("flashcardPage") { FlashcardPage(navController) }

        composable("pelafalanExercisePage") { PelafalanExercisePage(navController) }

        composable("pelafalanExerciseDetailPage") { PelafalanExerciseDetailPage(navController) }

        composable("kosakataExercisePage") { KosakataExercisePage(navController) }

        composable(
            route = "kosakataExerciseDetailPage/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            KosakataExerciseDetailPage(
                navController = navController,
                category = category
            )
        }

        composable(route = "kosakataExerciseDetailPage") {
            KosakataExerciseDetailPage(
                navController = navController
            )
        }

        composable("sequenceExercisePage") { SequenceExercisePage(navController) }

        composable("therapyResultPage") { TherapyResultPage(navController) }

        composable(
            "therapyResultPage/{score}/{totalQuestions}",
            arguments = listOf(
                navArgument("score") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument("totalQuestions") {
                    type = NavType.IntType
                    defaultValue = 10
                }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val totalQuestions = backStackEntry.arguments?.getInt("totalQuestions") ?: 10

            TherapyResultPage(
                navController = navController,
                score = score,
                totalQuestions = totalQuestions
            )
        }

        composable(
            "susunKataPage/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            SusunKataPage(index, navController)
        }
        composable("onboardingPage") { OnBoardingPage(navController) }

        composable("registerPage") { RegisterPage(navController) }

        composable("loginPage") { LoginPage(navController) }

        composable("suaraPintarPage") { SuaraPintarPage(R.drawable.kucing_2, navController) }

        composable("suaraPintarRecordPage") { SuaraPintarRecordPage(navController) }

        composable("paduGambarPage") { PaduGambarPage(navController) }

        composable("pustakaWicaraPage") { PustakaWicaraPage(navController) }

        composable("pustakaWicaraDetailPage/{selectedLetter}") { backStackEntry ->
            val selectedLetter = backStackEntry.arguments?.getString("selectedLetter") ?: "A"
            PustakaWicaraDetailPage(navController, selectedLetter)
        }

        composable(
            "kenaliAkuPage/{message}",
            arguments = listOf(navArgument("message") { type = NavType.StringType })
        ) { backStackEntry ->
            KenaliAkuPage(
                navController,
                backStackEntry.arguments?.getString("message") ?: "No message"
            )
        }

        composable("kenaliAkuRecordPage") { KenaliAkuRecordPage(navController) }

        composable(
            "kenaliAkuResultPage/{accuracy}",
            arguments = listOf(navArgument("accuracy") { type = NavType.StringType })
        ) { backStackEntry ->
            KenaliAkuResultPage(
                navHostController = navController,
                backStackEntry = backStackEntry
            )
        }
    }
}