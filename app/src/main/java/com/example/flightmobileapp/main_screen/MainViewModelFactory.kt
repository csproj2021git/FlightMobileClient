package com.example.flightmobileapp.main_screen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flightmobileapp.login.LoginViewModel

class MainViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //val temp :T = application!! as T
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                application
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")

    }

}
