package com.example.flightmobileapp.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.flightmobileapp.Repository
import com.example.flightmobileapp.db.DatabaseEntities
import com.example.flightmobileapp.db.getDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * View Model Class of Login fragment
 */
class LoginViewModel(application: Application) : ViewModel() {


    private val database = getDatabase(application)
    private val repository = Repository(database)

    /**
     * save url's from user input as arraylist of String and  bind it to RecyclerView
     */
    //private val _urls = MutableLiveData<ArrayList<String>>()
    private val _urls = repository.urls
    val urls: LiveData<List<String>>
        get() {
            return _urls
        }

    private val _status = repository.status
    val status: LiveData<Repository.ApiStatus>
        get() {
            return _status
        }

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _image = repository.image
 /*   val image: LiveData<String>
        get() {
            return _image
        }*/

    val image: LiveData<String>
        get() {
            return _image
        }

    /**
     * add new url after user has submitted
     */
    fun addUrl(url: String) {

       // val id: UUID = UUID.randomUUID()
        coroutineScope.launch {
            repository.AddServer(DatabaseEntities.Server(url))
        }

    }

    fun connect() {
        coroutineScope.launch {
//            repository.getImage()
        }
    }

    fun setUrlForNetwork(url: String) {
        repository.addUrlForNetwork(url)
    }
    // TODO: Implement the ViewModel
}
