package me.muhaimin.githubuser.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.muhaimin.githubuser.component.ModalBottomSettingViewModel
import me.muhaimin.githubuser.data.UserFavoriteRepository
import me.muhaimin.githubuser.injection.Injection
import me.muhaimin.githubuser.ui.detail.DetailViewModel
import me.muhaimin.githubuser.ui.favorite.UserFavoriteViewModel
import me.muhaimin.githubuser.ui.home.HomeViewModel
import me.muhaimin.githubuser.utils.SettingPreferences
import me.muhaimin.githubuser.utils.dataStore

class ViewModelFactory private constructor(
    private val userFavoriteRepository: UserFavoriteRepository,
    private val pref: SettingPreferences
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(userFavoriteRepository) as T
        }
        if (modelClass.isAssignableFrom(UserFavoriteViewModel::class.java)) {
            return UserFavoriteViewModel(userFavoriteRepository) as T
        }
        if (modelClass.isAssignableFrom(ModalBottomSettingViewModel::class.java)) {
            return ModalBottomSettingViewModel(pref) as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                val pref = SettingPreferences.getInstance(context.dataStore)

                instance ?: ViewModelFactory(Injection.provideUserFavoriteRepository(context), pref)
            }.also { instance = it }
    }
}