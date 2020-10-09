package com.example.flightmobileapp.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //val temp :T = application!! as T
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(
                    application
                ) as T

                //return LoginViewModel(application) as T
                //return LoginViewModel(application!!) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class")

    }

}
