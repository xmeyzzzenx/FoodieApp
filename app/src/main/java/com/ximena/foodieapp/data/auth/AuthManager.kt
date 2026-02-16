package com.ximena.foodieapp.data.auth

import android.app.Activity
import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.ximena.foodieapp.BuildConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthManager(context: Context) {

    private val appContext = context.applicationContext

    private val account: Auth0 = Auth0(
        clientId = BuildConfig.AUTH0_CLIENT_ID,
        domain = BuildConfig.AUTH0_DOMAIN
    )
    private val apiClient = AuthenticationAPIClient(account)

    private val credentialsManager = SecureCredentialsManager(
        appContext,
        apiClient,
        SharedPreferencesStorage(appContext)
    )

    fun isLoggedIn(): Boolean = credentialsManager.hasValidCredentials()

    suspend fun login(activity: Activity): Credentials =
        suspendCancellableCoroutine { cont ->
            WebAuthProvider.login(account)
                .withScheme("com.ximena.foodieapp")
                .withScope("openid profile email")
                .start(activity, object : Callback<Credentials, AuthenticationException> {

                    override fun onFailure(error: AuthenticationException) {
                        if (cont.isActive) cont.resumeWithException(error)
                    }

                    override fun onSuccess(result: Credentials) {
                        credentialsManager.saveCredentials(result)
                        if (cont.isActive) cont.resume(result)
                    }
                })
        }

    suspend fun logout(activity: Activity) =
        suspendCancellableCoroutine { cont ->
            WebAuthProvider.logout(account)
                .withScheme("com.ximena.foodieapp")
                .start(activity, object : Callback<Void?, AuthenticationException> {

                    override fun onFailure(error: AuthenticationException) {
                        if (cont.isActive) cont.resumeWithException(error)
                    }

                    override fun onSuccess(result: Void?) {
                        credentialsManager.clearCredentials()
                        if (cont.isActive) cont.resume(Unit)
                    }
                })
        }

    suspend fun getProfile(): UserProfile? =
        suspendCancellableCoroutine { cont ->
            credentialsManager.getCredentials(object :
                Callback<Credentials, CredentialsManagerException> {

                override fun onFailure(error: CredentialsManagerException) {
                    if (cont.isActive) cont.resume(null)
                }

                override fun onSuccess(result: Credentials) {
                    val token = result.accessToken
                    if (token.isNullOrBlank()) {
                        if (cont.isActive) cont.resume(null)
                        return
                    }

                    apiClient.userInfo(token)
                        .start(object : Callback<UserProfile, AuthenticationException> {

                            override fun onFailure(error: AuthenticationException) {
                                if (cont.isActive) cont.resume(null)
                            }

                            override fun onSuccess(result: UserProfile) {
                                if (cont.isActive) cont.resume(result)
                            }
                        })
                }
            })
        }

    fun clearSessionLocal() {
        // Útil si quieres limpiar sin pasar por logout web
        credentialsManager.clearCredentials()
    }
}
