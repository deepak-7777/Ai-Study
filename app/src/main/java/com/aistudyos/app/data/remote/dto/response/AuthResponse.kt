package com.aistudyos.app.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("expiresIn") val expiresIn: Long,
    @SerializedName("user") val user: UserDto
)

data class UserDto(

    @SerializedName("id")
    val id: String,

    @SerializedName("fullName")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phoneNumber")
    val phone: String?,

    @SerializedName("bio")
    val about: String?,

    @SerializedName("avatarUrl")
    val avatarUrl: String?,

    @SerializedName("createdAt")
    val createdAt: String?
)