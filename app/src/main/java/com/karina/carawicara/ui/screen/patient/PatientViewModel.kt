package com.karina.carawicara.ui.screen.patient

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.karina.carawicara.data.LanguageAbility
import com.karina.carawicara.data.Patient
import com.karina.carawicara.data.TherapyHistory
import com.karina.carawicara.data.repository.PatientRepository
import com.karina.carawicara.di.AppModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.util.UUID

class PatientViewModel(
    application: Application,
    private val repository: PatientRepository
) : AndroidViewModel(application) {

    val patients = repository.allPatients
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    private val therapyHistoriesCache = mutableMapOf<String, Flow<List<TherapyHistory>>>()

    private val _newPatientName = MutableStateFlow("")
    val newPatientName: StateFlow<String> = _newPatientName

    private val _newPatientBirthDate = MutableStateFlow<LocalDate?>(null)
    val newPatientBirthDate: StateFlow<LocalDate?> = _newPatientBirthDate

    private val _newPatientAddress = MutableStateFlow("")
    val newPatientAddress: StateFlow<String> = _newPatientAddress

    private val _newPatientAge = MutableStateFlow(0)
    val newPatientAge: StateFlow<Int> = _newPatientAge

    private val _newPatientAgeMonths = MutableStateFlow(0)
    val newPatientAgeMonths: StateFlow<Int> = _newPatientAgeMonths

    private val _languageAbilities = MutableStateFlow<List<LanguageAbility>>(emptyList())
    val languageAbilities: StateFlow<List<LanguageAbility>> = _languageAbilities

    private val _selectedPatientId = MutableStateFlow<String?>(null)
    val selectedPatientId: StateFlow<String?> = _selectedPatientId

    private val _selectedPatient = MutableStateFlow<Patient?>(null)
    val selectedPatient: StateFlow<Patient?> = _selectedPatient

    private val _isActiveSession = MutableStateFlow(false)
    val isActiveSession: StateFlow<Boolean> = _isActiveSession

    private val _allTherapyHistories = MutableStateFlow<List<TherapyHistory>>(emptyList())

    init {
        // Load all therapy histories when the view model is initialized
        viewModelScope.launch {
            try {
                repository.getAllTherapyHistories().collect { histories ->
                    _allTherapyHistories.value = histories
                }
            } catch (e: Exception) {
                Log.e("PatientViewModel", "Error loading therapy histories: ${e.message}", e)
            }
        }
    }

    fun updateNewPatientName(name: String) {
        _newPatientName.value = name
    }

    fun updateNewPatientBirthDate(date: LocalDate) {
        _newPatientBirthDate.value = date

        val (years, months) = calculateAgeInYearsAndMonths(date)
        _newPatientAge.value = years
        _newPatientAgeMonths.value = months

        updateLanguageAbilitiesBasedOnAge(date)
    }

    fun updateNewPatientAddress(address: String) {
        _newPatientAddress.value = address
    }

    fun toggleLanguageAbility(id: String) {
        val updatedList = _languageAbilities.value.map { ability ->
            if (ability.id == id) {
                ability.copy(isSelected = !ability.isSelected)
            } else {
                ability
            }
        }
        _languageAbilities.value = updatedList
    }

    fun updateLanguageAbilitiesBasedOnAge(birthDate: LocalDate) {
        viewModelScope.launch {
            val (years, months) = calculateAgeInYearsAndMonths(birthDate)
            val abilities = repository.generateLanguageAbilitiesByAge(years, months)
            _languageAbilities.value = abilities
        }
    }

    fun calculateAge(birthDate: LocalDate): Int {
        return Period.between(birthDate, LocalDate.now()).years
    }

    private fun calculateAgeInYearsAndMonths(birthDate: LocalDate): Pair<Int, Int> {
        val period = Period.between(birthDate, LocalDate.now())
        return Pair(period.years, period.months)
    }

    fun addNewPatient() {
        viewModelScope.launch {
            val name = _newPatientName.value
            val birthDate = _newPatientBirthDate.value ?: return@launch
            val address = _newPatientAddress.value

            val selectedAbilities = _languageAbilities.value.filter { it.isSelected }

            val newPatient = Patient(
                id = UUID.randomUUID().toString(),
                name = name,
                birthDate = birthDate,
                age = calculateAge(birthDate),
                address = address,
                languageAbilities = selectedAbilities
            )

            repository.insertPatient(newPatient)

            resetNewPatientForm()
        }
    }

    fun deletePatient(patientId: String) {
        viewModelScope.launch {
            try {
                // Delete all therapy histories for this patient first
                repository.deleteTherapyHistoriesForPatient(patientId)

                // Then delete the patient
                repository.deletePatient(patientId)

                // Clear the cache for this patient
                therapyHistoriesCache.remove(patientId)

                Log.d("PatientViewModel", "Patient and their therapy histories deleted successfully")
            } catch (e: Exception) {
                Log.e("PatientViewModel", "Error deleting patient: ${e.message}", e)
            }
        }
    }

    private fun resetNewPatientForm() {
        _newPatientName.value = ""
        _newPatientBirthDate.value = null
        _newPatientAddress.value = ""
        _newPatientAge.value = 0
        _newPatientAgeMonths.value = 0
        _languageAbilities.value = emptyList()
    }

    fun getAgeDescription(years: Int, months: Int): String {
        return when {
            years == 0 && months < 12 -> "$months bulan"
            years > 0 && months == 0 -> "$years tahun"
            else -> "$years tahun $months bulan"
        }
    }

    fun addTherapyHistory(therapyHistory: TherapyHistory) {
        viewModelScope.launch {
            try {
                repository.insertTherapyHistory(therapyHistory)

                // Update the cache
                val patientId = therapyHistory.patientId
                therapyHistoriesCache.remove(patientId)

                Log.d("PatientViewModel", "Therapy history added successfully")
            } catch (e: Exception) {
                Log.e("PatientViewModel", "Error adding therapy history: ${e.message}", e)
            }
        }
    }

    fun getTherapyHistoriesForPatient(patientId: String): Flow<List<TherapyHistory>> {
        // Check cache first
        if (therapyHistoriesCache.containsKey(patientId)) {
            return therapyHistoriesCache[patientId]!!
        }

        // Not in cache, fetch from repository
        val historiesFlow = repository.getTherapyHistoriesForPatient(patientId)
        therapyHistoriesCache[patientId] = historiesFlow
        return historiesFlow
    }

    fun setSelectedPatientId(patientId: String) {
        _selectedPatientId.value = patientId
        _isActiveSession.value = true

        viewModelScope.launch {
            try {
                val patient = repository.getPatientById(patientId)
                _selectedPatient.value = patient
                Log.d("PatientViewModel", "Selected patient: ${patient?.name}")
            } catch (e: Exception) {
                Log.e("PatientViewModel", "Error loading selected patient: ${e.message}", e)
            }
        }
    }

    fun getSelectedPatient(): Patient? {
        return _selectedPatient.value
    }

    suspend fun getTherapyHistoryById(historyId: String): TherapyHistory? {
        return repository.getTherapyHistoryById(historyId)
    }

    fun deleteTherapyHistory(historyId: String) {
        viewModelScope.launch {
            try {
                repository.deleteTherapyHistory(historyId)

                // Clear all cache since we don't know which patient this belongs to
                // In a real app, you would maintain a more efficient cache invalidation strategy
                therapyHistoriesCache.clear()

                Log.d("PatientViewModel", "Therapy history deleted successfully")
            } catch (e: Exception) {
                Log.e("PatientViewModel", "Error deleting therapy history: ${e.message}", e)
            }
        }
    }

    fun resetSelectedPatient() {
        _selectedPatientId.value = null
        _selectedPatient.value = null
        _isActiveSession.value = false
        Log.d("PatientViewModel", "Selected patient has been reset")
    }

    fun endTherapySession() {
        _isActiveSession.value = false
        Log.d("PatientViewModel", "Therapy session ended")
    }

    suspend fun refreshTherapyHistories(patientId: String) {
        viewModelScope.launch {
            val freshData = repository.getTherapyHistoriesForPatient(patientId)
        }
    }
}

class PatientViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientViewModel::class.java)) {
            return PatientViewModel(
                application,
                AppModule.providePatientRepository(application)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}