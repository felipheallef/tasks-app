package com.felipheallef.tasks.data.dao

import androidx.room.*
import com.felipheallef.tasks.data.entity.Task

@Dao
interface TasksDao {

    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Insert
    fun insert(task: Task): Long

    @Delete
    fun delete(task: Task): Int

    @Delete
    fun delete(vararg task: Task): Int

    @Update
    fun update(task: Task)
}