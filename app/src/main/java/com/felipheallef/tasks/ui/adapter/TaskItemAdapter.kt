package com.felipheallef.tasks.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.felipheallef.tasks.R
import com.felipheallef.tasks.app.Application
import com.felipheallef.tasks.data.entity.Task
import com.felipheallef.tasks.databinding.ListItemTaskBinding
import com.google.android.material.checkbox.MaterialCheckBox


class TaskItemAdapter(private val tasks: MutableList<Task>) :
    RecyclerView.Adapter<TaskItemAdapter.ViewHolder>() {

    private lateinit var binding: ListItemTaskBinding
    lateinit var context: Context
    var clicked = 0

    var listenEdit  : (Task) -> Unit = {}
    var doAfterDeleted : (Task) -> Unit = {}

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        binding = ListItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.txtTaskTitle.text = tasks[position].title
        binding.txtTaskDesc.text = tasks[position].description
        binding.txtTaskTime.text = "${tasks[position].date} ${tasks[position].hour}"

        bindEvents(position)
    }

    override fun getItemId(position: Int): Long {
        return tasks[position].id
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun bindEvents(position: Int) {

        binding.btnMore.setOnClickListener {
            showPopup(tasks[position], position)
        }

    }

    private fun deleteTask(task: Task, position: Int) {
        Application.database?.tasksDao()?.delete(task)
        tasks.removeAt(position)
        notifyItemRemoved(position)
        doAfterDeleted.invoke(task)
    }

    private fun showPopup(item: Task, position: Int) {
        val popupMenu = PopupMenu(binding.btnMore.context, binding.btnMore)
        popupMenu.menuInflater.inflate(R.menu.task_actions_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_delete -> deleteTask(item, position)
                R.id.action_edit -> listenEdit(item)
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    override fun getItemCount(): Int = tasks.size
}