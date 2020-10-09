package com.example.flightmobileapp.main_screen

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.flightmobileapp.Repository
import com.example.flightmobileapp.db.getDatabase
import com.example.flightmobileapp.network.Command
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.abs


class MainViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val repository = Repository(database)

    private val _status = repository.status
    val status: LiveData<Repository.ApiStatus>
        get() {
            return _status
        }

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _image = repository.image
    private val bitmap: LiveData<Bitmap> =
        Transformations.map(repository.image) {
            it.asBitmap()
        }
    val image: LiveData<Bitmap>
        get() {
            return bitmap
        }


    var aileronValue: Double = 0.0
        set(value) {
            if (abs(value - field) > 0.01) {
                field = value
                updateCommand(aileron = field)
            }

        }
    var rudderValue: Double = 0.0
        set(value) {
            if (abs(value - field) > 0.01) {
                field = value
                updateCommand(rudder = field)
            }

        }
    var throttleValue: Double = 0.0
        set(value) {
            if (abs(value - field) > 0.01) {
                field = value
                updateCommand(throttle = field)
            }

        }
    var elevatorValue: Double = 0.0
        set(value) {
            if (abs(value - field) > 0.01) {
                field = value
                updateCommand(elevator = field)
            }

        }


    fun updateCommand(aileron:Double = aileronValue,
                      rudder:Double = rudderValue,
                      throttle:Double = throttleValue,
                      elevator:Double = elevatorValue){
        coroutineScope.launch {
            repository.uploadCommand(
                Command(aileron, rudder, elevator, throttle)
            )
        }
    }

    fun connect() {
        coroutineScope.launch {
            repository.getImage()
        }
    }
}

private fun String.asBitmap(): Bitmap {
    val imageBytes = Base64.decode(this, 0)
    val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    return image
}
