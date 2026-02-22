package com.ximena.foodieapp.domain.usecase

import android.app.Activity
import com.ximena.foodieapp.data.repository.AuthRepository
import com.ximena.foodieapp.domain.model.UserInfo
import javax.inject.Inject

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

class IsLoggedInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Boolean = repository.isLoggedIn()
}

class GetCachedUserInfoUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): UserInfo? = repository.getCachedUserInfo()
}
