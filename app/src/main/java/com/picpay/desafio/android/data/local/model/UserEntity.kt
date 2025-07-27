package com.picpay.desafio.android.data.local.model

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.picpay.desafio.android.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo("id") val id: Int,
    @ColumnInfo("img") val img: String?,
    @ColumnInfo("name") val name: String?,
    @ColumnInfo("username") val username: String?
)

fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        name = this.name ?:"",
        username = this.username ?:"",
        img = this.img ?: ""
    )
}
