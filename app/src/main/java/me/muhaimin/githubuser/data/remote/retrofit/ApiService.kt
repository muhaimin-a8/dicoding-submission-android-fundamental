package me.muhaimin.githubuser.data.remote.retrofit

import me.muhaimin.githubuser.data.remote.response.ResponseUser
import me.muhaimin.githubuser.data.remote.response.ResponseUserDetail
import me.muhaimin.githubuser.data.remote.response.ResponseUserSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun searchUser(
        @Query("q") username: String
    ): Call<ResponseUserSearch>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<ResponseUserDetail>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<ResponseUser>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<ResponseUser>>
}