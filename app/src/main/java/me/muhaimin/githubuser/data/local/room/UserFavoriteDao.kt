package me.muhaimin.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.muhaimin.githubuser.data.local.entity.UserFavoriteEntity

@Dao
interface UserFavoriteDao {

    @Query("SELECT * FROM user_favorite ORDER BY username ASC")
    fun getUserFavorite(): LiveData<List<UserFavoriteEntity>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserFavorite(user: UserFavoriteEntity)

    @Query("DELETE FROM user_favorite WHERE username = :username")
    suspend fun deleteUserFavoriteByUsername(username: String)

    @Query("SELECT * FROM user_favorite WHERE username = :username")
    suspend fun isFavoriteUser(username: String): UserFavoriteEntity?
}