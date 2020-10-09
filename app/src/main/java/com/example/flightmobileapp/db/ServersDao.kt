package com.example.flightmobileapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ServersDao {
    // https://localhost:44383/

    @Query("select url from servers_table order by id desc limit 5")
    fun getLastFive(): LiveData<List<String>>

    @Insert
    fun addServer(server: DatabaseEntities.Server)
}