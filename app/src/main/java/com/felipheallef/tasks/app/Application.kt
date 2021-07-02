package com.felipheallef.tasks.app

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.felipheallef.tasks.data.TasksDatabase

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, TasksDatabase::class.java, "database")
            .allowMainThreadQueries().build()

        Log.i(TAG, "Application started.")
    }

    companion object {
        const val TAG = "TasksApplication"

        var database: TasksDatabase? = null
    }

}