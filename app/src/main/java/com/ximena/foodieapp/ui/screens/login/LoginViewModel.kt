package com.ximena.foodieapp.ui.screens.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.ximena.foodieapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun login(activity: Activity, onSuccess: () -> Unit) {
        _errorMessage.value = null
        _isLoading.value = true

        authRepository.login(
            activity = activity,
            onSuccess = {
                _isLoading.value = false
                onSuccess()
            },
            onError = { error ->
                _isLoading.value = false
                _errorMessage.value = error
            }
        )
    }
}
