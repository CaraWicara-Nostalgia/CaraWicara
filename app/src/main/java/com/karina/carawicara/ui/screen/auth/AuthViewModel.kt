package com.karina.carawicara.ui.screen.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.karina.carawicara.data.entity.UserEntity
import com.karina.carawicara.data.repository.UserRepository
import com.karina.carawicara.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.core.content.edit

sealed class AuthState{
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val userId: Long, val user: UserEntity? = null) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val saveUserId = getSavedUserId()
            if (saveUserId > 0) {
                val user = userRepository.getUserById(saveUserId)
                if (user != null) {
                    _currentUser.value = user
                    _isLoggedIn.value = true
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            if (email.isBlank() || password.isBlank()) {
                _authState.value = AuthState.Error("Email dan password tidak boleh kosong")
                return@launch
            }

            if (!isValidEmail(email)) {
                _authState.value = AuthState.Error("Email tidak valid")
                return@launch
            }

            userRepository.loginUser(email, password)
                .onSuccess { user ->
                    _currentUser.value = user
                    _isLoggedIn.value = true
                    saveUserId(user.id)
                    _authState.value = AuthState.Success(user.id, user)
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Login gagal")
                }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                _authState.value = AuthState.Error("Email dan password tidak boleh kosong")
                return@launch
            }

            if (!isValidEmail(email)) {
                _authState.value = AuthState.Error("Email tidak valid")
                return@launch
            }

            if (password != confirmPassword) {
                _authState.value = AuthState.Error("Password tidak cocok")
                return@launch
            }

            if (password.length < 6) {
                _authState.value = AuthState.Error("Password minimal 6 karakter")
                return@launch
            }

            userRepository.registerUser(email, password)
                .onSuccess { userId ->
                    _authState.value = AuthState.Success(userId)
                    login(email, password)
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Registrasi gagal")
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                userRepository.logoutUser(user.id)
            }
            _currentUser.value = null
            _isLoggedIn.value = false
            clearSavedUserId()
            _authState.value = AuthState.Idle
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun saveUserId(userId: Long) {
        val sharedPref = getApplication<Application>().getSharedPreferences("user_pref", Application.MODE_PRIVATE)
        sharedPref.edit { putLong("user_id", userId) }
    }

    private fun getSavedUserId(): Long {
        val sharedPref = getApplication<Application>().getSharedPreferences("user_pref", Application.MODE_PRIVATE)
        return sharedPref.getLong("user_id", -1)
    }

    private fun clearSavedUserId() {
        val sharedPref = getApplication<Application>().getSharedPreferences("user_pref", Application.MODE_PRIVATE)
        sharedPref.edit { remove("user_id") }
    }
}

class AuthViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(
                application,
                AppModule.provideUserRepository(application)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}