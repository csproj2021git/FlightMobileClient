package com.example.flightmobileapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseEntities.Server::class], version = 1)
abstract class ServersDatabase : RoomDatabase() {
    abstract val serversDao: ServersDao
}

private lateinit var INSTANCE: ServersDatabase

fun getDatabase(context: Context): ServersDatabase {
    synchronized(ServersDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                ServersDatabase::class.java,
                "servers"
            ).build()
        }
    }
    return INSTANCE
}