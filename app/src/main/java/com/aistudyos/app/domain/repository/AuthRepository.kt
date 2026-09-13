package com.aistudyos.app.domain.repository

import com.aistudyos.app.core.common.models.Result
import com.aistudyos.app.domain.model.User

interface AuthRepository {

    suspend fun login(email: String, password: String): Result<User>

    suspend fun register(
        fullName: String,
        email: String,
        password: String
    ): Result<User>

    suspend fun updateProfile(
        name: String?,
        email: String?,
        phone: String?,
        about: String?
    ): Result<User>

    suspend fun forgotPassword(email: String): Result<Unit>

    suspend fun googleLogin(token: String): Result<User>

    suspend fun logout()

    suspend fun isLoggedIn(): Boolean

    suspend fun updateAvatar(avatarUrl: String): Result<User>

    suspend fun getMe(): Result<User>
    suspend fun updateName(name: String): Result<User>
    suspend fun updateEmail(email: String): Result<User>
    suspend fun updatePhone(phone: String): Result<User>
    suspend fun updateAbout(about: String): Result<User>
}