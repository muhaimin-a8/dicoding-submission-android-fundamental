package me.muhaimin.githubuser.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.muhaimin.githubuser.utils.SettingPreferences

class ModalBottomSettingViewModel(private val pref: SettingPreferences): ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActivate: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActivate)
        }
    }
}