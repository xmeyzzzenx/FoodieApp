package com.ximena.foodieapp.utils

object Validators {

    fun isNotBlank(value: String): Boolean = value.trim().isNotEmpty()

    fun isPositiveInt(value: String): Boolean {
        return value.toIntOrNull()?.let { it > 0 } ?: false
    }
}
