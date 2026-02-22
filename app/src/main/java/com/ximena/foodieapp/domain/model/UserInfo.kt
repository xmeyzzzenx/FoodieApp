package com.ximena.foodieapp.domain.model

data class UserInfo(
    val name: String,
    val email: String,
    val pictureUrl: String?,
    val userId: String = "anonymous"
)