package com.felipheallef.tasks.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.felipheallef.tasks.data.dao.TasksDao
import com.felipheallef.tasks.data.entity.Task

@Database(entities = [Task::class], version =  1)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao

}