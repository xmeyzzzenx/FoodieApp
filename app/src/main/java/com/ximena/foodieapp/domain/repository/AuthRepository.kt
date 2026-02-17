package com.ximena.foodieapp.domain.repository

import android.app.Activity
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val isLoggedIn: StateFlow<Boolean>
    val userName: StateFlow<String>
    val userEmail: StateFlow<String>

    fun checkSession()

    fun login(
        activity: Activity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    fun logout(activity: Activity)

    fun refreshUserProfile()
    fun clearSession()
}
