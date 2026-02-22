package com.ximena.foodieapp.data.repository

import android.app.Activity
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.ximena.foodieapp.domain.model.UserInfo
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// Repositorio de autenticación con Auth0
@Singleton
class AuthRepository @Inject constructor(
    private val auth0: Auth0
) {
    // Credenciales y datos del usuario en memoria (se pierden al cerrar la app)
    private var cachedCredentials: Credentials? = null
    private var cachedUserInfo: UserInfo? = null

    private val authScheme = "com.ximena.foodieapp" // Scheme del redirect URI de Auth0

    // Si hay credenciales en memoria = hay sesión activa
    fun isLoggedIn(): Boolean = cachedCredentials != null

    fun getCachedUserInfo(): UserInfo? = cachedUserInfo

    // Devuelve el ID único del usuario (el "sub" del token de Auth0)
    fun getCurrentUserId(): String = cachedUserInfo?.userId ?: "anonymous"

    // Abre el navegador para hacer login con Auth0
    // suspendCancellableCoroutine convierte el callback de Auth0 a corrutina
    suspend fun login(activity: Activity): Result<UserInfo> {
        return try {
            val credentials = suspendCancellableCoroutine { continuation ->
                WebAuthProvider.login(auth0)
                    .withScheme(authScheme)
                    .withScope("openid profile email") // Permisos que se piden al usuario
                    .start(activity, object : Callback<Credentials, AuthenticationException> {
                        override fun onSuccess(result: Credentials) {
                            if (continuation.isActive) continuation.resume(result)
                        }
                        override fun onFailure(error: AuthenticationException) {
                            if (continuation.isActive) continuation.resumeWithException(error)
                        }
                    })
            }
            cachedCredentials = credentials
            val accessToken = credentials.accessToken
            // Con el token se piden los datos del perfil del usuario
            val userInfo = if (!accessToken.isNullOrBlank()) {
                fetchUserProfile(accessToken)
            } else {
                UserInfo("Usuario", "", null, "anonymous")
            }
            cachedUserInfo = userInfo
            Result.success(userInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Abre el navegador para cerrar sesión en Auth0 y limpia la caché local
    suspend fun logout(activity: Activity): Result<Unit> {
        return try {
            suspendCancellableCoroutine { continuation ->
                WebAuthProvider.logout(auth0)
                    .withScheme(authScheme)
                    .start(activity, object : Callback<Void?, AuthenticationException> {
                        override fun onSuccess(result: Void?) {
                            if (continuation.isActive) continuation.resume(Unit)
                        }
                        override fun onFailure(error: AuthenticationException) {
                            // Aunque falle, se limpia la sesión local igualmente
                            if (continuation.isActive) continuation.resume(Unit)
                        }
                    })
            }
            cachedCredentials = null
            cachedUserInfo = null
            Result.success(Unit)
        } catch (e: Exception) {
            cachedCredentials = null
            cachedUserInfo = null
            Result.success(Unit)
        }
    }

    // Llama a la API de Auth0 para obtener nombre, email y foto del usuario
    private suspend fun fetchUserProfile(accessToken: String): UserInfo {
        return try {
            suspendCancellableCoroutine { continuation ->
                AuthenticationAPIClient(auth0)
                    .userInfo(accessToken)
                    .start(object : Callback<UserProfile, AuthenticationException> {
                        override fun onSuccess(result: UserProfile) {
                            val userInfo = UserInfo(
                                userId = result.getId() ?: accessToken.take(20),
                                name = result.name ?: result.nickname ?: "Usuario",
                                email = result.email ?: "",
                                pictureUrl = result.pictureURL?.toString()
                            )
                            if (continuation.isActive) continuation.resume(userInfo)
                        }
                        override fun onFailure(error: AuthenticationException) {
                            // Si falla, se devuelve un usuario genérico con el token como ID
                            if (continuation.isActive) continuation.resume(
                                UserInfo("Usuario", "", null, accessToken.take(20))
                            )
                        }
                    })
            }
        } catch (e: Exception) {
            UserInfo("Usuario", "", null, "anonymous")
        }
    }
}