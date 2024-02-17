package me.muhaimin.githubuser.ui.detail.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.muhaimin.githubuser.data.remote.response.ResponseUser
import me.muhaimin.githubuser.data.remote.retrofit.ApiConfig
import me.muhaimin.githubuser.model.User
import me.muhaimin.githubuser.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _listFollow = MutableLiveData<List<User>>()
    val listFollow: LiveData<List<User>> = _listFollow

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int> = _position

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    companion object {
        val TAG = "FollowViewModel"
    }

    fun fetchFollow(username: String, type: TypeFollow) {
        _isLoading.postValue(true)
        Log.d("USER DETAIL" , "TYPE: $type")

        val client = if (type == TypeFollow.FOLLOWERS) {
            ApiConfig.getApiService().getUserFollowers(username)
        } else {
            ApiConfig.getApiService().getUserFollowing(username)
        }
        client.enqueue(object : Callback<List<ResponseUser>> {
            override fun onResponse(
                call: Call<List<ResponseUser>>,
                response: Response<List<ResponseUser>>
            ) {
                _isLoading.postValue(false)
                if(response.isSuccessful) {

                    _listFollow.postValue(response.body()?.map { User(it.login, it.avatarUrl) })
                } else {
                    _errorMessage.postValue(Event(response.message()))
                    Log.d(TAG, response.message())
                }
            }

            override fun onFailure(call: Call<List<ResponseUser>>, t: Throwable) {
                _isLoading.postValue(false)
                _errorMessage.postValue(Event("failed to fetch data"))
                Log.d(TAG, t.message.toString())
            }

        })
    }

    fun updateUsername(username: String) {
        _username.value = username
    }

    fun updatePosition(position: Int) {
        _position.value = position
    }
}

enum class TypeFollow {
    FOLLOWERS, FOLLOWING
}