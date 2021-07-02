package com.felipheallef.tasks.ui.view

import android.view.View
import android.app.Application
import com.felipheallef.tasks.app.Application as TasksApplication
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.felipheallef.tasks.data.entity.Task

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = TasksApplication.database?.tasksDao()

    val taskList = MutableLiveData<List<Task>>()
    val emptyStateVisibility = MutableLiveData<Int>(View.GONE)

    val taskTitle = MutableLiveData("")
    val taskDescription = MutableLiveData("")
    val taskDueDate = MutableLiveData("")
    val taskDueHour = MutableLiveData("")


    fun loadTasks() {
        val tasks = dao?.getAll()
        tasks?.let {
            taskList.postValue(it)
            if (it.isEmpty())
                emptyStateVisibility.postValue(View.VISIBLE)
            else
                emptyStateVisibility.postValue(View.INVISIBLE)
        }
    }

    fun createTask(title: String, description: String, date: String, hour: String) {
        val task = Task(title, description, date, hour)
        val id = dao?.insert(task)
        loadTasks()
        emptyStateVisibility.postValue(View.INVISIBLE)
    }

    fun updateTask(task: Task) {
        val id = dao?.update(task)
        loadTasks()
    }

    fun isDataValid(): Boolean {

        if (taskTitle.value.isNullOrBlank())
            return false
        if (taskDescription.value.isNullOrBlank())
            return false
        if (taskDueDate.value.isNullOrBlank())
            return false
        if (taskDueHour.value.isNullOrBlank())
            return false

        return true
    }

    private fun setEmptyStateVisibility() {
        if (taskList.value.isNullOrEmpty())
            emptyStateVisibility.postValue(View.VISIBLE)
        else
            emptyStateVisibility.postValue(View.INVISIBLE)
    }
}