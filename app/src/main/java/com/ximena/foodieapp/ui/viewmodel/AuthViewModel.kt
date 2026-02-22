package com.ximena.foodieapp.ui.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.UiState
import com.ximena.foodieapp.domain.model.UserInfo
import com.ximena.foodieapp.domain.usecase.GetCachedUserInfoUseCase
import com.ximena.foodieapp.domain.usecase.IsLoggedInUseCase
import com.ximena.foodieapp.domain.usecase.LoginUseCase
import com.ximena.foodieapp.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val getCachedUserInfoUseCase: GetCachedUserInfoUseCase
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(isLoggedInUseCase())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _loginState = MutableStateFlow<UiState<UserInfo>?>(null)
    val loginState: StateFlow<UiState<UserInfo>?> = _loginState.asStateFlow()

    private val _userInfo = MutableStateFlow<UserInfo?>(getCachedUserInfoUseCase())
    val userInfo: StateFlow<UserInfo?> = _userInfo.asStateFlow()

    fun login(activity: Activity) {
        viewModelScope.launch {
            _loginState.value = UiState.Loading
            loginUseCase(activity).fold(
                onSuccess = { user ->
                    _userInfo.value = user
                    _isLoggedIn.value = true
                    _loginState.value = UiState.Success(user)
                },
                onFailure = {
                    _loginState.value = UiState.Error(it.message ?: "Error al iniciar sesión")
                }
            )
        }
    }

    fun logout(activity: Activity) {
        viewModelScope.launch {
            logoutUseCase(activity)
            _isLoggedIn.value = false
            _userInfo.value = null
            _loginState.value = null
        }
    }
}
