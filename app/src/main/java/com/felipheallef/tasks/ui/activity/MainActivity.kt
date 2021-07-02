package com.felipheallef.tasks.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.felipheallef.tasks.data.entity.Task
import com.felipheallef.tasks.databinding.ActivityMainBinding
import com.felipheallef.tasks.ui.adapter.TaskItemAdapter
import com.felipheallef.tasks.ui.fragment.CreateNewTaskFragment
import com.felipheallef.tasks.ui.fragment.EditTaskFragment
import com.felipheallef.tasks.ui.view.TasksViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val model: TasksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@MainActivity
            viewModel = model
        }

        setContentView(binding.root)

        model.loadTasks()

        bindEvents()
    }

    private fun bindEvents() {
        binding.btnAddTask.setOnClickListener {
            CreateNewTaskFragment.getInstance().apply {
                this.show(supportFragmentManager, "TAG")
            }
        }
        model.taskList.observe(this) {
            binding.empty.visibility =
                if (it.isNotEmpty()) View.GONE else View.VISIBLE
            initList(it as MutableList<Task>)
        }

        model.emptyStateVisibility.observe(this) {
            binding.empty.visibility = it
        }
    }

    private fun initList(list: MutableList<Task>) {
        binding.tasksList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = TaskItemAdapter(list).apply {
                listenEdit = {
                    EditTaskFragment(it).apply {
                        this.show(supportFragmentManager, "TAG")
                    }
                }
                doAfterDeleted = {
                    model.loadTasks()
                }
            }
        }
    }

}