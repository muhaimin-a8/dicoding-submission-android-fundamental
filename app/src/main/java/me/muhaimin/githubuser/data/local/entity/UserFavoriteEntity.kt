package me.muhaimin.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_favorite")
class UserFavoriteEntity (
    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    val username: String,

    @field:ColumnInfo(name = "avatar_url")
    val avatarUrl: String,
)