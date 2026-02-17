package com.ximena.foodieapp.auth

import android.app.Activity
import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.ximena.foodieapp.BuildConfig

class Auth0Manager(
    context: Context,
    private val sessionStore: SecureSessionStore
) {
    private val account: Auth0 = Auth0(
        BuildConfig.AUTH0_CLIENT_ID,
        BuildConfig.AUTH0_DOMAIN
    )

    private val authClient: AuthenticationAPIClient = AuthenticationAPIClient(account)

    fun hasSession(): Boolean {
        return sessionStore.hasSession()
    }

    fun login(
        activity: Activity,
        onSuccess: (Credentials) -> Unit,
        onError: (String) -> Unit
    ) {
        WebAuthProvider.login(account)
            .withScheme("demo")
            .withScope("openid profile email")
            .start(activity, object : Callback<Credentials, com.auth0.android.authentication.AuthenticationException> {
                override fun onFailure(error: com.auth0.android.authentication.AuthenticationException) {
                    onError(error.getDescription() ?: "Error de login")
                }

                override fun onSuccess(result: Credentials) {
                    sessionStore.saveTokens(result.accessToken, result.idToken)
                    onSuccess(result)
                }
            })
    }

    fun logout(
        activity: Activity,
        onDone: () -> Unit
    ) {
        WebAuthProvider.logout(account)
            .withScheme("demo")
            .start(activity, object : Callback<Void?, com.auth0.android.authentication.AuthenticationException> {
                override fun onFailure(error: com.auth0.android.authentication.AuthenticationException) {
                    sessionStore.clear()
                    onDone()
                }

                override fun onSuccess(result: Void?) {
                    sessionStore.clear()
                    onDone()
                }
            })
    }

    fun getUserProfile(
        onSuccess: (name: String, email: String) -> Unit,
        onError: (String) -> Unit
    ) {
        val accessToken = sessionStore.getAccessToken()
        if (accessToken.isNullOrBlank()) {
            onError("No hay token")
            return
        }

        authClient.userInfo(accessToken)
            .start(object : Callback<com.auth0.android.result.UserProfile, com.auth0.android.authentication.AuthenticationException> {
                override fun onFailure(error: com.auth0.android.authentication.AuthenticationException) {
                    onError(error.getDescription() ?: "Error obteniendo perfil")
                }

                override fun onSuccess(result: com.auth0.android.result.UserProfile) {
                    val name = result.name ?: "Usuario"
                    val email = result.email ?: ""
                    onSuccess(name, email)
                }
            })
    }
}
