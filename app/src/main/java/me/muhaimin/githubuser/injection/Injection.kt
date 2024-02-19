package me.muhaimin.githubuser.injection

import android.content.Context
import me.muhaimin.githubuser.data.UserFavoriteRepository
import me.muhaimin.githubuser.data.local.room.UserFavoriteDatabase

object Injection {
    fun provideUserFavoriteRepository(context: Context): UserFavoriteRepository {
        val database = UserFavoriteDatabase.getInstance(context)

        return UserFavoriteRepository.getInstance(database.userFavoriteDao())
    }
}