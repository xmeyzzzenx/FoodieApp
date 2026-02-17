package com.ximena.foodieapp.data.repository

import android.app.Activity
import com.ximena.foodieapp.auth.Auth0Manager
import com.ximena.foodieapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth0Manager: Auth0Manager
) : AuthRepository {

    private val _isLoggedIn = MutableStateFlow(false)
    override val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userName = MutableStateFlow("")
    override val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow("")
    override val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    override fun checkSession() {
        _isLoggedIn.value = auth0Manager.hasSession()
        if (_isLoggedIn.value) {
            refreshUserProfile()
        } else {
            _userName.value = ""
            _userEmail.value = ""
        }
    }

    override fun login(
        activity: Activity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth0Manager.login(
            activity = activity,
            onSuccess = {
                _isLoggedIn.value = true
                refreshUserProfile()
                onSuccess()
            },
            onError = { msg ->
                onError(msg)
            }
        )
    }

    override fun logout(activity: Activity) {
        auth0Manager.logout(activity) {
            clearSession()
        }
    }

    override fun refreshUserProfile() {
        auth0Manager.getUserProfile(
            onSuccess = { name, email ->
                _userName.value = name
                _userEmail.value = email
            },
            onError = {
                _userName.value = "Usuario"
                _userEmail.value = ""
            }
        )
    }

    override fun clearSession() {
        _isLoggedIn.value = false
        _userName.value = ""
        _userEmail.value = ""
    }
}
