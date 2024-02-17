package me.muhaimin.githubuser.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.muhaimin.githubuser.data.remote.response.ResponseUserDetail
import me.muhaimin.githubuser.data.remote.retrofit.ApiConfig
import me.muhaimin.githubuser.model.UserDetail
import me.muhaimin.githubuser.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _userDetail = MutableLiveData<UserDetail>()
    val userDetail: LiveData<UserDetail> = _userDetail

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    companion object {
        val TAG = "UserDetailViewModel"
    }

    fun getDetailUser() {
        _isLoading.postValue(true)

        val client = ApiConfig.getApiService().getUserDetail(username.value.toString())
        client.enqueue(object : Callback<ResponseUserDetail> {
            override fun onResponse(
                call: Call<ResponseUserDetail>,
                response: Response<ResponseUserDetail>
            ) {
                _isLoading.postValue(false)
                if(response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        _userDetail.postValue(UserDetail(
                            it.name,
                            it.login,
                            it.avatarUrl,
                            it.followers,
                            it.following,
                        ))
                    }
                } else {
                    _errorMessage.postValue(Event(response.message()))
                }
            }

            override fun onFailure(call: Call<ResponseUserDetail>, t: Throwable) {
                _isLoading.postValue(false)
                _errorMessage.postValue(Event("failed to fetch data"))
                Log.d(TAG, t.message.toString())
            }

        })
    }

    fun updateUsername(username: String) {
        _username.value = username
    }
}