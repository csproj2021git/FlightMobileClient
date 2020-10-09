package com.example.flightmobileapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

class DatabaseEntities {

/*    @Entity(tableName = "servers_table")
    data class Server(

        @PrimaryKey(autoGenerate = true)
        val id:String,

        val url: String
    )*/

    @Entity(tableName = "servers_table")
     class Server(
        val url: String
    )
    {
        @PrimaryKey(autoGenerate = true)
        var id:Int = 0
    }

}