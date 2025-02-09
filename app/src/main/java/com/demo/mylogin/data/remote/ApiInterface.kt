package com.demo.mylogin.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface ApiInterface {

    @POST("/api/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse
}