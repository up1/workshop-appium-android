package com.demo.mylogin.repository

import com.demo.mylogin.data.remote.ApiInterface
import com.demo.mylogin.data.remote.LoginRequest
import com.demo.mylogin.data.remote.LoginResponse
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class UserRepository @Inject constructor(
    private val apiInterface: ApiInterface
) {

    suspend fun login(request: LoginRequest): Resource<LoginResponse> {
        val response = try {
            apiInterface.loginUser(request)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured: ${e.localizedMessage}")
        }

        return Resource.Success(response)
    }
}