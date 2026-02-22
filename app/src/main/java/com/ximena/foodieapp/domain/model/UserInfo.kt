package com.ximena.foodieapp.domain.model

// Datos del usuario autenticado con Auth0
data class UserInfo(
    val name: String,
    val email: String,
    val pictureUrl: String?,
    val userId: String = "anonymous" // El "sub" del token de Auth0
)