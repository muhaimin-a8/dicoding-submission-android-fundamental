package me.muhaimin.githubuser.model

data class User(
    val username: String?,
    val avatarUrl: String?,
)

data class UserDetail(
    val fullName: String?,
    val username: String?,
    val avatarUrl: String?,
    val followersCount: Int?,
    val followingCount: Int?,
)