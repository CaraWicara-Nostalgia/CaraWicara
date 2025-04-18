package com.karina.carawicara.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.karina.carawicara.R
import com.karina.carawicara.ui.screen.profile.ProfilePage
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
import com.karina.carawicara.ui.screen.flashcard.SequenceExerciseDetailPage
import com.karina.carawicara.ui.screen.flashcard.SequenceExercisePage
import com.karina.carawicara.ui.screen.flashcard.TherapyResultPage
import com.karina.carawicara.ui.screen.paduGambar.PaduGambarPage
import com.karina.carawicara.ui.screen.patient.AddPatientPage
import com.karina.carawicara.ui.screen.patient.LanguageAbilityPage
import com.karina.carawicara.ui.screen.patient.PatientPage
import com.karina.carawicara.ui.screen.patient.PatientViewModel
import com.karina.carawicara.ui.screen.patient.PatientViewModelFactory
import com.karina.carawicara.ui.screen.pustakaWicara.PustakaWicaraDetailPage
import com.karina.carawicara.ui.screen.pustakaWicara.PustakaWicaraPage
import com.karina.carawicara.ui.screen.suaraPintar.SuaraPintarPage
import com.karina.carawicara.ui.screen.suaraPintar.SuaraPintarRecordPage
import com.karina.carawicara.ui.screen.susunKata.SusunKataPage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController) {

    val patientViewModel: PatientViewModel = viewModel(factory = PatientViewModelFactory())

    NavHost(navController, startDestination = "splashScreen") {

        // ----- Splashscreen Routes -----

        composable("splashScreen") { SplashScreen(navController) }

        // ----- Home Routes -----

        composable("homePage") { HomePage(navController) }

        // ----- Patient Routes -----

        composable("patientPage") {
            PatientPage(
                navController = navController,
                viewModel = patientViewModel)
        }

        composable("addPatientPage") {
            AddPatientPage(
                navController = navController,
                viewModel = patientViewModel)
        }

        composable("languageAbilityPage") {
            LanguageAbilityPage(
                navController = navController,
                viewModel = patientViewModel)
        }

        composable(
            route = "patientProfilePage/{patientId}",
            arguments = listOf(
                navArgument("patientId") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val patientId = entry.arguments?.getString("patientId") ?: ""
        }

        // ----- Profile Routes -----

        composable("profilePage") { ProfilePage(navController) }

        // ----- Flashcard Routes -----

        composable("flashcardPage") { FlashcardPage(navController) }

        composable("pelafalanExercisePage") { PelafalanExercisePage(navController) }

        composable(
            route = "pelafalanExerciseDetailPage/{categoryTitle}",
            arguments = listOf(
                navArgument("categoryTitle") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { entry ->
            val category = entry.arguments?.getString("categoryTitle")
            PelafalanExerciseDetailPage(
                navController = navController,
                category = category
            )
        }

        composable("pelafalanExerciseDetailPage") {
            PelafalanExerciseDetailPage(
                navController = navController
            )
        }

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

        composable(
            route = "sequenceExerciseDetailPage/{categoryTitle}",
            arguments = listOf(
                navArgument("categoryTitle") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { entry ->
            val category = entry.arguments?.getString("categoryTitle")
            SequenceExerciseDetailPage(
                navController = navController,
                categoryTitle = category
            )
        }

        composable("sequenceExerciseDetailPage") {
            SequenceExerciseDetailPage(
                navController = navController
            )
        }

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

        // ----- Onboarding Routes -----

        composable("onboardingPage") { OnBoardingPage(navController) }

        // ----- Register Routes -----

        composable("registerPage") { RegisterPage(navController) }

        // ----- Login Routes -----

        composable("loginPage") { LoginPage(navController) }

        // ----- Suara Pintar Routes -----

        composable("suaraPintarPage") { SuaraPintarPage(R.drawable.kucing_2, navController) }

        composable("suaraPintarRecordPage") { SuaraPintarRecordPage(navController) }

        // ----- Padu Gambar Routes -----

        composable("paduGambarPage") { PaduGambarPage(navController) }

        // ----- Pustaka Wicara Routes -----

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

        // ----- Kenali Aku Routes -----

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