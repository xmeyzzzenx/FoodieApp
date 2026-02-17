package com.ximena.foodieapp.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.ximena.foodieapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun isLoggedIn(): Boolean {
        authRepository.checkSession()
        return authRepository.isLoggedIn.value
    }
}
