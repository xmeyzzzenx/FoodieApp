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

@Singleton
class AuthRepository @Inject constructor(
    private val auth0: Auth0
) {
    private var cachedCredentials: Credentials? = null
    private var cachedUserInfo: UserInfo? = null

    private val authScheme = "com.ximena.foodieapp"

    fun isLoggedIn(): Boolean = cachedCredentials != null

    fun getCachedUserInfo(): UserInfo? = cachedUserInfo

    // ✅ userId único por usuario Auth0 (sub del token)
    fun getCurrentUserId(): String = cachedUserInfo?.userId ?: "anonymous"

    suspend fun login(activity: Activity): Result<UserInfo> {
        return try {
            val credentials = suspendCancellableCoroutine { continuation ->
                WebAuthProvider.login(auth0)
                    .withScheme(authScheme)
                    .withScope("openid profile email")
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