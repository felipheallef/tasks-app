package com.felipheallef.tasks.ui.fragment

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.felipheallef.tasks.R
import com.felipheallef.tasks.databinding.FragmentNewTaskBinding
import com.felipheallef.tasks.ui.view.TasksViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class CreateNewTaskFragment : BottomSheetDialogFragment() {

    val model: TasksViewModel by activityViewModels()
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
            lifecycleOwner = this@CreateNewTaskFragment
            viewModel = model
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindFields()

        binding.btnCreateTask.setOnClickListener {
            val title = model.taskTitle.value!!
            val description = model.taskDescription.value!!
            val date = model.taskDueDate.value!!
            val hour = model.taskDueHour.value!!

            if (model.isDataValid()) {
                model.createTask(title, description, date, hour)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun bindFields() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecione uma data")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        binding.inputDate.editText?.setOnClickListener {
            it as EditText
            datePicker.show(requireActivity().supportFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener { milis ->
                val date = getDate(milis)
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


    companion object {
        fun getInstance(): CreateNewTaskFragment {
            return CreateNewTaskFragment()
        }

        fun getDate(milis: Long): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val data = Instant.ofEpochMilli(milis)
                    .atZone(ZoneId.of("America/Sao_Paulo"))
                    .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                    .toLocalDate()
                val formatter = DateTimeFormatter
                    .ofPattern("dd/MM/yyyy", Locale("pt-br"))
                formatter.format(data)
            } else {
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = milis
                val data = format.format(milis)
                data
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        model.taskTitle.postValue("")
        model.taskDescription.postValue("")
        model.taskDueDate.postValue("")
        model.taskDueHour.postValue("")
    }

}