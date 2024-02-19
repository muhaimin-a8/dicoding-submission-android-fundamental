package me.muhaimin.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import me.muhaimin.githubuser.data.UserFavoriteRepository

class UserFavoriteViewModel(private val userFavoriteRepository: UserFavoriteRepository) :
    ViewModel() {
    fun getUserFavorite() = userFavoriteRepository.getUserFavorites()
}