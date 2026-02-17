package com.ximena.foodieapp.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureSessionStore(context: Context) {

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_session",
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTokens(accessToken: String?, idToken: String?) {
        prefs.edit()
            .putString(KEY_ACCESS, accessToken ?: "")
            .putString(KEY_ID, idToken ?: "")
            .apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS, null)?.ifBlank { null }
    }

    fun getIdToken(): String? {
        return prefs.getString(KEY_ID, null)?.ifBlank { null }
    }

    fun hasSession(): Boolean {
        return !getIdToken().isNullOrBlank()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_ACCESS = "access_token"
        private const val KEY_ID = "id_token"
    }
}
