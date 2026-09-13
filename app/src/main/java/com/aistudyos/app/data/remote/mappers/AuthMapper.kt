package com.aistudyos.app.data.remote.mappers

import com.aistudyos.app.data.remote.dto.response.UserDto
import com.aistudyos.app.domain.model.User

fun UserDto.toDomain() = User(
    id = id,
    name = name,
    email = email,
    phone = phone,
    about = about,
    avatarUrl = avatarUrl
)
