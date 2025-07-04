package com.karina.carawicara.navigation

import ProfilePage
import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.karina.carawicara.R
import com.karina.carawicara.data.SessionAssessment
import com.karina.carawicara.ui.screen.SplashScreen
import com.karina.carawicara.ui.screen.auth.LoginPage
import com.karina.carawicara.ui.screen.flashcard.FlashcardPage
import com.karina.carawicara.ui.screen.home.HomePage
import com.karina.carawicara.ui.screen.kenaliAku.KenaliAkuPage
import com.karina.carawicara.ui.screen.kenaliAku.KenaliAkuRecordPage
import com.karina.carawicara.ui.screen.kenaliAku.KenaliAkuResultPage
import com.karina.carawicara.ui.screen.auth.OnBoardingPage
import com.karina.carawicara.ui.screen.auth.RegisterPage
import com.karina.carawicara.ui.screen.flashcard.AssessmentViewModel
import com.karina.carawicara.ui.screen.flashcard.AssessmentViewModelFactory
import com.karina.carawicara.ui.screen.flashcard.CardAssessmentPage
import com.karina.carawicara.ui.screen.flashcard.KosakataExerciseDetailPage
import com.karina.carawicara.ui.screen.flashcard.KosakataExercisePage
import com.karina.carawicara.ui.screen.flashcard.KosakataExerciseViewModelFactory
import com.karina.carawicara.ui.screen.flashcard.PelafalanExerciseDetailPage
import com.karina.carawicara.ui.screen.flashcard.PelafalanExercisePage
import com.karina.carawicara.ui.screen.flashcard.PelafalanExerciseViewModelFactory
import com.karina.carawicara.ui.screen.flashcard.SequenceExerciseDetailPage
import com.karina.carawicara.ui.screen.flashcard.SequenceExercisePage
import com.karina.carawicara.ui.screen.therapyHistory.TherapyResultPage
import com.karina.carawicara.ui.screen.paduGambar.PaduGambarPage
import com.karina.carawicara.ui.screen.patient.AddPatientPage
import com.karina.carawicara.ui.screen.patient.DevelopmentDetailPage
import com.karina.carawicara.ui.screen.patient.EditPatientPage
import com.karina.carawicara.ui.screen.patient.LanguageAbilityPage
import com.karina.carawicara.ui.screen.patient.PatientPage
import com.karina.carawicara.ui.screen.patient.PatientProfilePage
import com.karina.carawicara.ui.screen.patient.PatientSelectionPage
import com.karina.carawicara.ui.screen.patient.PatientViewModel
import com.karina.carawicara.ui.screen.patient.PatientViewModelFactory
import com.karina.carawicara.ui.screen.profile.AboutPage
import com.karina.carawicara.ui.screen.therapyHistory.TherapyHistoryDetailPage
import com.karina.carawicara.ui.screen.pustakaWicara.PustakaWicaraDetailPage
import com.karina.carawicara.ui.screen.pustakaWicara.PustakaWicaraPage
import com.karina.carawicara.ui.screen.suaraPintar.SuaraPintarPage
import com.karina.carawicara.ui.screen.suaraPintar.SuaraPintarRecordPage
import com.karina.carawicara.ui.screen.susunKata.SusunKataPage
import com.karina.carawicara.ui.screen.therapyHistory.TherapyHistoryListPage

@Composable
fun AppNavHost(navController: NavHostController) {

    val patientViewModel: PatientViewModel = viewModel(
        factory = PatientViewModelFactory(
            application = LocalContext.current.applicationContext as Application
        )
    )

    val assessmentViewModel: AssessmentViewModel = viewModel(
        factory = AssessmentViewModelFactory(
            application = LocalContext.current.applicationContext as Application
        )
    )

    NavHost(navController, startDestination = "splashScreen") {

        // ----- Splashscreen Routes -----

        composable("splashScreen") { SplashScreen(navController) }

        // ----- Home Routes -----

        composable("homePage") {
            HomePage(
                navController = navController,
                patientViewModel = patientViewModel)
        }

        // ----- Patient Routes -----

        composable("patientPage") {
            PatientPage(
                navController = navController,
                viewModel = patientViewModel)
        }

        composable("addPatientPage") {
            AddPatientPage(
                navController = navController,
                viewModel = patientViewModel
            )
        }

        composable("languageAbilityPage") {
            LanguageAbilityPage(
                navController = navController,
                viewModel = patientViewModel
            )
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
            PatientProfilePage(
                navController = navController,
                patientId = patientId,
                viewModel = patientViewModel
            )
        }

        composable("editPatientPage/{patientId}") { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            EditPatientPage(
                navController = navController,
                patientId = patientId,
                viewModel = patientViewModel
            )
        }

        composable(
            route = "developmentDetailPage/{patientId}",
            arguments = listOf(
                navArgument("patientId") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val patientId = entry.arguments?.getString("patientId") ?: ""
            DevelopmentDetailPage(
                navController = navController,
                patientId = patientId,
                viewModel = patientViewModel
            )
        }

        composable("patientSelectionForTherapy/{nextRoute}") { backStackEntry ->
            val nextRoute = backStackEntry.arguments?.getString("nextRoute") ?: "homePage"
            PatientSelectionPage(
                navController = navController,
                viewModel = patientViewModel,
                nextRoute = nextRoute
            )
        }

        composable(
            route = "therapyHistoryDetailPage/{historyId}",
            arguments = listOf(navArgument("historyId") { type = NavType.StringType })
        ) { entry ->
            val historyId = entry.arguments?.getString("historyId") ?: ""
            TherapyHistoryDetailPage(
                navController = navController,
                therapyHistoryId = historyId,
                viewModel = patientViewModel
            )
        }

        composable(
            route = "therapyHistoryListPage/{patientId}",
            arguments = listOf(
                navArgument("patientId") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val patientId = entry.arguments?.getString("patientId") ?: ""
            TherapyHistoryListPage(
                navController = navController,
                patientId = patientId,
                viewModel = patientViewModel
            )
        }

        // ----- Profile Routes -----

        composable("profilePage") { ProfilePage(navController) }

        composable("aboutPage") { AboutPage(navController) }

        // ----- Flashcard Routes -----

        composable("flashcardPage") { FlashcardPage(navController = navController, patientViewModel = patientViewModel) }

        composable("pelafalanExercisePage") { PelafalanExercisePage(navController) }

        composable(
            route = "pelafalanExerciseDetailPage/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            val context = LocalContext.current
            PelafalanExerciseDetailPage(
                navController = navController,
                category = category,
                viewModel = viewModel(
                    factory = PelafalanExerciseViewModelFactory(
                        application = context.applicationContext as Application,
                    )
                )
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
            val context = LocalContext.current
            KosakataExerciseDetailPage(
                navController = navController,
                category = category,
                viewModel = viewModel(
                    factory = KosakataExerciseViewModelFactory(
                        application = context.applicationContext as Application,
                    )
                )
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

        composable("therapyResultPage") {
            val sessionAssessment by assessmentViewModel.sessionAssessment.collectAsState()

            TherapyResultPage(
                navController = navController,
                sessionAssessment = sessionAssessment,
                patientViewModel = patientViewModel
            )
        }

        composable("therapyResultPage/{score}/{totalQuestions}",
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val totalQuestions = backStackEntry.arguments?.getInt("totalQuestions") ?: 10

            val simpleAssessment = remember {
                SessionAssessment(
                    cardAssessments = emptyList(),
                    totalCorrect = score,
                    totalCards = totalQuestions
                )
            }

            TherapyResultPage(
                navController = navController,
                sessionAssessment = simpleAssessment,
                patientViewModel = patientViewModel
            )
        }

        composable(
            "cardAssessmentPage/{cardWord}/{cardIndex}/{totalCards}/{isCorrect}",
            arguments = listOf(
                navArgument("cardWord") { type = NavType.StringType },
                navArgument("cardIndex") { type = NavType.IntType },
                navArgument("totalCards") { type = NavType.IntType },
                navArgument("isCorrect") { type = NavType.BoolType }
            )
        ) { navBackStackEntry ->
            val cardWord = navBackStackEntry.arguments?.getString("cardWord") ?: ""
            val cardIndex = navBackStackEntry.arguments?.getInt("cardIndex") ?: 0
            val totalCards = navBackStackEntry.arguments?.getInt("totalCards") ?: 0
            val isCorrect = navBackStackEntry.arguments?.getBoolean("isCorrect") ?: false

            CardAssessmentPage(
                navController = navController,
                cardWord = cardWord,
                cardIndex = cardIndex,
                totalCards = totalCards,
                isCorrect = isCorrect,
                onAssessmentComplete = { assessment ->
                    assessmentViewModel.addCardAssessment(assessment)

                    if (cardIndex + 1 < totalCards) {
                        val categoryRoute = navController.previousBackStackEntry?.destination?.route
                        if (categoryRoute?.contains("kosakataExerciseDetailPage") == true) {
                            navController.popBackStack()
                        } else if ( categoryRoute?.contains("pelafalanExerciseDetailPage") == true) {
                            navController.popBackStack()
                        }
                    } else {
                        navController.navigate("therapyResultPage") {
                            popUpTo("cardAssessmentPage") { inclusive = true }
                        }
                    }
                }
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