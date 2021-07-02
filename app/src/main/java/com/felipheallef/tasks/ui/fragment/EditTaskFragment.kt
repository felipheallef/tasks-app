package com.felipheallef.tasks.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import com.felipheallef.tasks.R
import com.felipheallef.tasks.data.entity.Task
import com.felipheallef.tasks.databinding.FragmentNewTaskBinding
import com.felipheallef.tasks.ui.view.TasksViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class EditTaskFragment(val task: Task) : BottomSheetDialogFragment() {

    private val model: TasksViewModel by activityViewModels()
    lateinit var binding: FragmentNewTaskBinding

    override fun getTheme(): Int {
        return R.style.ThemeOverlay_MaterialComponents_BottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTaskBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@EditTaskFragment
            viewModel = model
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()
        bindFields()

        binding.btnCreateTask.setOnClickListener {
            val title = model.taskTitle.value!!
            val description = model.taskDescription.value!!
            val date = model.taskDueDate.value!!
            val hour = model.taskDueHour.value!!

            if (model.isDataValid()) {
                val task = Task(title, description, date, hour)
                task.id = this.task.id

                model.updateTask(task)
                dismiss()
            }
        }
    }

    private fun bindData() {
        binding.txtNewTaskTitle.text = getString(R.string.label_edit_task)
        binding.btnCreateTask.text = getString(R.string.action_save)

        model.taskTitle.postValue(task.title)
        model.taskDescription.postValue(task.description)
        model.taskDueDate.postValue(task.date)
        model.taskDueHour.postValue(task.hour)
    }

    private fun bindFields() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecione uma data")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        binding.inputDate.editText?.setOnClickListener {
            it as EditText
            datePicker.show(requireActivity().supportFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener { milis ->
                val date = CreateNewTaskFragment.getDate(milis)
                it.setText(date)
            }
        }

        binding.inputHour.editText?.setOnClickListener {
            it as EditText

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener { _ ->
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                it.setText("$hour:$minute")
            }

            timePicker.show(requireActivity().supportFragmentManager, "tag")
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        model.taskTitle.postValue("")
        model.taskDescription.postValue("")
        model.taskDueDate.postValue("")
        model.taskDueHour.postValue("")
    }

    companion object {

    }
}