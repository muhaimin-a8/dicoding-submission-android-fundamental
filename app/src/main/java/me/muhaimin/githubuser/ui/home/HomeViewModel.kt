package me.muhaimin.githubuser.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import me.muhaimin.githubuser.data.remote.response.ResponseUserSearch
import me.muhaimin.githubuser.data.remote.retrofit.ApiConfig
import me.muhaimin.githubuser.model.User
import me.muhaimin.githubuser.utils.Event
import me.muhaimin.githubuser.utils.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val pref: SettingPreferences) : ViewModel() {
    private val _listUser = MutableLiveData<List<User>>()
    val listUser: LiveData<List<User>> = _listUser

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun searchUser(query: String) {
        _isLoading.postValue(true)

        val client = ApiConfig.getApiService().searchUser(query)
        client.enqueue(object : Callback<ResponseUserSearch> {
            override fun onResponse(
                call: Call<ResponseUserSearch>,
                response: Response<ResponseUserSearch>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {

                    _listUser.postValue(response.body()?.items?.map {
                        User(
                            it?.login,
                            it?.avatarUrl
                        )
                    })
                } else {
                    _errorMessage.postValue(Event(response.message()))
                }
            }

            override fun onFailure(call: Call<ResponseUserSearch>, t: Throwable) {
                _isLoading.postValue(false)
                _errorMessage.postValue(Event("failed to fetch data"))
            }

        })
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
}