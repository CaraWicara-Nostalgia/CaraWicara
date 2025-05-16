package com.karina.carawicara.ui.screen.patient

import android.app.Application
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.karina.carawicara.data.LanguageAbility

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageAbilityPage(
    navController: NavController,
    viewModel: PatientViewModel = viewModel(
        factory = PatientViewModelFactory(
            application = LocalContext.current.applicationContext as Application
        )
    )
){
    val languageAbilities by viewModel.languageAbilities.collectAsState()
    val newPatientAge by viewModel.newPatientAge.collectAsState()
    val newPatientAgeMonths by viewModel.newPatientAgeMonths.collectAsState()
    val newPatientBirthDate by viewModel.newPatientBirthDate.collectAsState()

    LaunchedEffect(Unit) {
        newPatientBirthDate?.let { birthDate ->
            viewModel.updateLanguageAbilitiesBasedOnAge(birthDate)
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kemampuan Bahasa",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                }
            )
        },
        bottomBar = {
            // Tombol Simpan sebagai bottom bar agar selalu terlihat
            Button(
                onClick = {
                    viewModel.addNewPatient()
                    navController.navigate("patientPage") {
                        // Pop up to patientPage agar tidak menumpuk halaman
                        popUpTo("patientPage") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Simpan",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ){
            Text(
                text = "Kemampuan Bahasa untuk Usia ${viewModel.getAgeDescription(newPatientAge, newPatientAgeMonths)}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Usia: $newPatientAge tahun $newPatientAgeMonths bulan",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Jumlah kemampuan bahasa: ${languageAbilities.size}",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Divider(thickness = 1.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            if (languageAbilities.isEmpty()) {
                Text(
                    text = "Tidak ada data kemampuan bahasa untuk usia ini.",
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                // Group header untuk kategori ini
                val ageRangeText = when {
                    newPatientAge == 0 && newPatientAgeMonths <= 6 -> "Kemampuan Bahasa (0-6 bulan)"
                    newPatientAge == 0 && newPatientAgeMonths <= 12 -> "Kemampuan Bahasa (7-12 bulan)"
                    newPatientAge == 1 && newPatientAgeMonths <= 6 -> "Kemampuan Bahasa (13-18 bulan)"
                    newPatientAge == 1 && newPatientAgeMonths <= 12 -> "Kemampuan Bahasa (19-24 bulan)"
                    newPatientAge == 2 -> "Kemampuan Bahasa (2-3 tahun)"
                    newPatientAge == 3 -> "Kemampuan Bahasa (3-4 tahun)"
                    newPatientAge == 4 -> "Kemampuan Bahasa (4-5 tahun)"
                    newPatientAge == 5 -> "Kemampuan Bahasa (5-6 tahun)"
                    newPatientAge == 6 -> "Kemampuan Bahasa (6-7 tahun)"
                    else -> "Kemampuan Bahasa (${newPatientAge}+ tahun)"
                }

                Text(
                    text = ageRangeText,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "Pilih kemampuan bahasa yang sudah dikuasai:",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                languageAbilities.forEach { ability ->
                    LanguageAbilityItem(
                        ability = ability,
                        onChecked = { viewModel.toggleLanguageAbility(ability.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun LanguageAbilityItem(
    ability: LanguageAbility,
    onChecked: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = ability.isSelected,
            onCheckedChange = onChecked
        )

        Text(
            text = ability.description,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageAbilityPagePreview() {
    val navController = NavController(context = LocalContext.current)
    LanguageAbilityPage(navController = navController)
}