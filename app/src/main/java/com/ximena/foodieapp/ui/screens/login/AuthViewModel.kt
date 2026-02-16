package com.ximena.foodieapp.ui.screens.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.result.UserProfile
import com.ximena.foodieapp.data.auth.AuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authManager: AuthManager
) : ViewModel() {

    sealed class UiState {
        data object Idle : UiState()
        data object Loading : UiState()
        data class LoggedIn(val profile: UserProfile?) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun checkSession() {
        viewModelScope.launch {
            if (authManager.isLoggedIn()) {
                _state.value = UiState.Loading
                val profile = authManager.getProfile()
                _state.value = UiState.LoggedIn(profile)
            } else {
                _state.value = UiState.Idle
            }
        }
    }

    fun login(activity: Activity) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                authManager.login(activity)
                val profile = authManager.getProfile()
                _state.value = UiState.LoggedIn(profile)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Error en login")
            }
        }
    }

    fun logout(activity: Activity) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                authManager.logout(activity)
                _state.value = UiState.Idle
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Error en logout")
            }
        }
    }
}
