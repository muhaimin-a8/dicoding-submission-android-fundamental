package me.muhaimin.githubuser.data

import android.util.Log
import androidx.lifecycle.LiveData
import me.muhaimin.githubuser.data.local.entity.UserFavoriteEntity
import me.muhaimin.githubuser.data.local.room.UserFavoriteDao

class UserFavoriteRepository private constructor(
    private val userFavoriteDao: UserFavoriteDao
){
    fun getUserFavorites(): LiveData<List<UserFavoriteEntity>> {
        return userFavoriteDao.getUserFavorite()
    }

    suspend fun insertUserFavorite(user: UserFavoriteEntity) {
        userFavoriteDao.insertUserFavorite(user)
    }

    suspend fun deleteUserFavorite(username: String) {
        userFavoriteDao.deleteUserFavoriteByUsername(username)
    }

    suspend fun isFavoriteUser(username: String): Boolean {
        Log.d("ROOOOOM", "$username : ${userFavoriteDao.isFavoriteUser(username)}")
        if (userFavoriteDao.isFavoriteUser(username) == null) {
            return false
        }

        return true
    }


    companion object {
        @Volatile
        private var instance: UserFavoriteRepository? = null

        fun getInstance(userFavoriteDao: UserFavoriteDao): UserFavoriteRepository =
            instance ?: synchronized(this) {
                instance ?: UserFavoriteRepository((userFavoriteDao))
            }.also { instance = it }
    }
}