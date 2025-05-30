package com.karina.carawicara.ui.screen.patient

import android.app.Application
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.karina.carawicara.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPatientPage(
    navController: NavController,
    viewModel: PatientViewModel = viewModel(
        factory = PatientViewModelFactory(
            application = LocalContext.current.applicationContext as Application
        )
    )
) {
    val newPatientName by viewModel.newPatientName.collectAsState()
    val newPatientBirthDate by viewModel.newPatientBirthDate.collectAsState()
    val newPatientAddress by viewModel.newPatientAddress.collectAsState()

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    newPatientBirthDate?.let {
        calendar.set(it.year, it.monthValue - 1, it.dayOfMonth)
    }
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            viewModel.updateNewPatientBirthDate(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    var dateText = newPatientBirthDate?.format(DateTimeFormatter.ofPattern("dd MM yyyy")) ?: ""

    val isFormValid by remember {
        derivedStateOf {
            newPatientName.isNotBlank() &&
            newPatientBirthDate != null &&
            newPatientAddress.isNotBlank()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Data Pasien Baru",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Nama *",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newPatientName,
                onValueChange = { viewModel.updateNewPatientName(it) },
                placeholder = { Text("Nama") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                isError = newPatientName.isBlank() && newPatientName.isNotEmpty()
            )
            if (newPatientName.isBlank() && newPatientName.isNotEmpty()) {
                Text(
                    text = "Nama tidak boleh kosong",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tanggal Lahir *",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = dateText,
                onValueChange = { newText ->
                    dateText = newText
                    try {
                        val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
                        val date = LocalDate.parse(newText, formatter)
                        viewModel.updateNewPatientBirthDate(date)
                    } catch (_: Exception) {
                    }
                },
                placeholder = { Text("DD MM YYYY") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = "Pilih Tanggal",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                isError = newPatientBirthDate == null && dateText.isNotEmpty()
            )
            if (newPatientBirthDate == null && dateText.isNotEmpty()) {
                Text(
                    text = "Tanggal lahir harus diisi",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Usia",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = if (newPatientBirthDate != null) "${viewModel.calculateAge(newPatientBirthDate!!)} tahun" else "",
                onValueChange = { },
                placeholder = { Text("Umur") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                enabled = false,
                readOnly = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Alamat",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newPatientAddress,
                onValueChange = { viewModel.updateNewPatientAddress(it) },
                placeholder = { Text("Alamat lengkap") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                maxLines = 4,
                isError = newPatientAddress.isBlank() && newPatientAddress.isNotEmpty()
            )
            if (newPatientAddress.isBlank() && newPatientAddress.isNotEmpty()) {
                Text(
                    text = "Alamat tidak boleh kosong",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.navigate("languageAbilityPage")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    contentColor = Color.White
                ),
                enabled = isFormValid
            ) {
                Text(
                    text = if (isFormValid) "Selanjutnya" else "Lengkapi Data Terlebih Dahulu",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}