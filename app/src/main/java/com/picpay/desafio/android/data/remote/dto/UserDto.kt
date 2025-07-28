package com.picpay.desafio.android.data.remote.dto

import com.picpay.desafio.android.data.local.model.UserEntity
import com.picpay.desafio.android.domain.model.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: String,
    val name: String,
    @Json(name = "img")
    val imageUrl: String,
    val username: String
)

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = this.id.toInt(),
        name = this.name,
        username = this.username,
        img = this.imageUrl
    )
}
