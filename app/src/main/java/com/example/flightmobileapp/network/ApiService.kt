package com.example.flightmobileapp.network

import androidx.lifecycle.MutableLiveData
import com.example.flightmobileapp.Repository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

//private const val BASE_URL = "https://127.0.0.1:44383/"
// private const val BASE_URL = "http://10.0.2.2:44383/"

//private const val BASE_URL = "https://api.themoviedb.org/3/movie/"
//var BASE_URL = "http://10.0.2.2:53411"
//var SERVER_URL = "https://api.themoviedb.org/3/movie/"


/**
 * set connection infrastructure to server with REST API
 */
class ApiService {
    /**
     * interface with REST api's
     */
    interface IApiService {
        //    @GET(BASE_URL + "/api/screenshot")
        //@GET("top_rated?api_key=a921476d861fb36a167704c00cb03bfb&language=en-US&page=1")
        @GET("/api/screenshot")
        fun getImage(/*url: String*/): Deferred<String>

        @Headers("Content-Type: application/json")
        @POST("/api/command")
        fun sendCommand(@Body data: Command): Deferred<String>
       /* @POST(BASE_URL)
        fun connect(): Deferred<String>*/
    }

    object ServerApi {
        private lateinit var url: String
        var status : Repository.ApiStatus = Repository.ApiStatus.DONE

        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        private lateinit var retrofit: Retrofit

        val retrofitService: IApiService by lazy {
            retrofit.create(
                IApiService::class.java
            )
        }

        fun setUrl(url: String) {
            this.url = url

            try {
                this.retrofit = Retrofit.Builder()
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .baseUrl(ServerApi.url)
                    .build()

                status =Repository.ApiStatus.DONE
            }
            catch (e:  Exception){
                status =Repository.ApiStatus.ERROR
            }

        }
    }
}


