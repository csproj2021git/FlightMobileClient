package com.example.flightmobileapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flightmobileapp.db.DatabaseEntities
import com.example.flightmobileapp.db.ServersDatabase
import com.example.flightmobileapp.network.ApiService
import com.example.flightmobileapp.network.Command
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class Repository(private val database: ServersDatabase) {

    enum class ApiStatus { ERROR, DONE }

    val status = MutableLiveData<ApiStatus>()



/*
    private val _image = MutableLiveData<String>()
    val image: LiveData<String>
        get() = _image
*/

    private val _image = MutableLiveData<String>()
    val image: LiveData<String>
        get() = _image

    val urls: LiveData<List<String>> = database.serversDao.getLastFive()

    /**
     * async method - called from GET command , get the picture from server
     */
    suspend fun getImage() {
        withContext(Dispatchers.IO) {
            try {
                var getImageDeffered = ApiService.ServerApi.retrofitService.getImage(/*url*/)
                var getImageWaited = getImageDeffered.await()
                _image.postValue(getImageWaited)
                status.postValue(ApiStatus.DONE)
            } catch (e: Exception){
                status.postValue(ApiStatus.ERROR)
            }
        }
    }

    /**
     * async method - called with POST command , post controllers data to server
     */
    suspend fun uploadCommand(command: Command) {
        withContext(Dispatchers.IO) {
            try {
                ApiService.ServerApi.retrofitService.sendCommand(command)
                status.postValue(ApiStatus.DONE)
            } catch (e: Exception) {
                status.postValue(ApiStatus.ERROR)
            }
        }
    }

    fun addUrlForNetwork(url: String) {
       //SERVER_URL = url
        ApiService.ServerApi.setUrl(url)
        status.value = ApiService.ServerApi.status
    }

    /**
     * bind url that user has entered to DB
     */
    suspend fun AddServer(server: DatabaseEntities.Server) {
        withContext(Dispatchers.IO) {
            database.serversDao.addServer(server)
        }
    }
}