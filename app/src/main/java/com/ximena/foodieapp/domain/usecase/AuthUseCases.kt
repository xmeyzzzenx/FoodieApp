package com.ximena.foodieapp.domain.usecase

import android.app.Activity
import com.ximena.foodieapp.data.repository.AuthRepository
import com.ximena.foodieapp.domain.model.UserInfo
import javax.inject.Inject

// Casos de uso de autenticación: cada clase hace una sola cosa
// invoke() permite llamarlos como si fueran funciones: loginUseCase(activity)

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(activity: Activity): Result<UserInfo> = repository.login(activity)
}

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(activity: Activity): Result<Unit> = repository.logout(activity)
}

// Comprueba si hay sesión activa (mira si hay credenciales en memoria)
class IsLoggedInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Boolean = repository.isLoggedIn()
}

// Devuelve los datos del usuario ya cacheados (sin llamar a la red)
class GetCachedUserInfoUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): UserInfo? = repository.getCachedUserInfo()
}