package com.aistudyos.app.data.repository

import com.aistudyos.app.core.common.models.AppException
import com.aistudyos.app.core.common.models.Result
import com.aistudyos.app.core.common.utils.toAppException
import com.aistudyos.app.data.local.prefs.SessionManager
import com.aistudyos.app.data.remote.api.AuthApiService
import com.aistudyos.app.data.remote.api.UserApiService
import com.aistudyos.app.data.remote.dto.request.LoginRequest
import com.aistudyos.app.data.remote.dto.request.RegisterRequest
import com.aistudyos.app.data.remote.mappers.toDomain
import com.aistudyos.app.domain.model.User
import com.aistudyos.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val userApi: UserApiService,
    private val sessionManager: SessionManager,
) : AuthRepository {

    private var cachedUser: User? = null

    override suspend fun login(email: String, password: String): Result<User> = safeCall {
        val response = api.login(LoginRequest(email, password))
        handleAuthResponse(response)
    }

    override suspend fun register(name: String, email: String, password: String): Result<User> = safeCall {
        val response = api.register(
            RegisterRequest(
                fullName = name,
                email = email,
                password = password
            )
        )
        handleAuthResponse(response)
    }

    override suspend fun updateProfile(
        name: String?,
        email: String?,
        phone: String?,
        about: String?
    ): Result<User> {

        return try {
            val body = mutableMapOf<String, Any>()
            name?.let { body["fullName"] = it }
            email?.let { body["email"] = it }
            phone?.let { body["phoneNumber"] = it }
            about?.let { body["bio"] = it }
            val response = userApi.updateProfile(body)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data != null) {
                    val user = apiResponse.data.toDomain()
                    cachedUser = user
                    Result.Success(user)
                } else {
                    Result.Error(
                        AppException.ValidationException(
                            apiResponse?.message ?: "Update failed"
                        )
                    )
                }

            } else {
                Result.Error(
                    AppException.HttpException(
                        code = response.code(),
                        message = response.message()
                    )
                )
            }
        } catch (e: java.io.IOException) {
            Result.Error(AppException.NetworkException())
        } catch (e: Exception) {
            Result.Error(
                AppException.UnknownException(
                    e.message ?: "Profile update failed"
                )
            )
        }
    }

    // 🔥 FIXED UPDATE AVATAR
    override suspend fun updateAvatar(avatarUrl: String): Result<User> {
        return try {
            val response = userApi.updateProfile(
                mapOf("avatarUrl" to avatarUrl)
            )
            if (response.isSuccessful) {
                val userDto = response.body()?.data
                if (userDto != null) {
                    Result.Success(userDto.toDomain())
                } else {
                    Result.Error(AppException.UnknownException("Empty response"))
                }
            } else {
                Result.Error(
                    AppException.HttpException(
                        response.code(),
                        "Update failed"
                    )
                )
            }
        } catch (e: Exception) {
            Result.Error(e.toAppException())
        }
    }

    // 🔥 FIXED GET PROFILE
    override suspend fun getMe(): Result<User> {
        cachedUser?.let {
            return Result.Success(it)
        }
        return try {
            val response = userApi.getMe()
            if (response.isSuccessful) {
                val userDto = response.body()?.data
                if (userDto != null) {
                    val user = userDto.toDomain()
                    cachedUser = user   // 🔥 CACHE SAVE
                    Result.Success(user)
                } else {
                    Result.Error(AppException.UnknownException("Empty response"))
                }
            } else {
                Result.Error(
                    AppException.HttpException(
                        response.code(),
                        "Fetch failed"
                    )
                )
            }
        } catch (e: Exception) {
            Result.Error(e.toAppException())
        }
    }

    override suspend fun updateName(name: String): Result<User> {
        return updateProfile(name = name, email = null, phone = null, about = null)
    }

    override suspend fun updateEmail(email: String): Result<User> {
        return updateProfile(name = null, email = email, phone = null, about = null)
    }

    override suspend fun updatePhone(phone: String): Result<User> {
        return updateProfile(name = null, email = null, phone = phone, about = null)
    }

    override suspend fun updateAbout(about: String): Result<User> {
        return updateProfile(name = null, email = null, phone = null, about = about)
    }

    // 🔥 GOOGLE LOGIN (unchanged - already correct)
    override suspend fun googleLogin(token: String): Result<User> = safeCall {

        val response = api.googleLogin(mapOf("token" to token))

        if (response.isSuccessful && response.body()?.success == true) {

            val body = response.body()!!.data!!

            sessionManager.saveSession(
                token = body.accessToken,
                refreshToken = body.refreshToken,
                userId = body.user.id,
                name = body.user.name,
                email = body.user.email
            )

            Result.Success(body.user.toDomain())

        } else {
            Result.Error(
                AppException.HttpException(
                    response.code(),
                    response.body()?.error ?: "Google login failed"
                )
            )
        }
    }

    override suspend fun forgotPassword(email: String): Result<Unit> = safeCall {
        val response = api.forgotPassword(mapOf("email" to email))
        if (response.isSuccessful) Result.Success(Unit)
        else Result.Error(AppException.HttpException(response.code(), "Failed to send reset email"))
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }

    override suspend fun isLoggedIn(): Boolean =
        sessionManager.isLoggedIn().firstOrNull() == true

    // 🔥 COMMON AUTH HANDLER
    private suspend fun handleAuthResponse(
        response: retrofit2.Response<com.aistudyos.app.data.remote.dto.response.ApiResponse<com.aistudyos.app.data.remote.dto.response.AuthResponse>>
    ): Result<User> {

        return if (response.isSuccessful && response.body()?.success == true) {

            val body = response.body()!!.data!!

            sessionManager.saveSession(
                token = body.accessToken,
                refreshToken = body.refreshToken,
                userId = body.user.id,
                name = body.user.name,
                email = body.user.email
            )

            Result.Success(body.user.toDomain())

        } else {
            Result.Error(
                AppException.HttpException(
                    response.code(),
                    response.body()?.error ?: "Request failed"
                )
            )
        }
    }

    // 🔥 SAFE CALL
    private inline fun <T> safeCall(block: () -> Result<T>): Result<T> =
        try {
            block()
        } catch (e: Exception) {
            Result.Error(e.toAppException())
        }

}