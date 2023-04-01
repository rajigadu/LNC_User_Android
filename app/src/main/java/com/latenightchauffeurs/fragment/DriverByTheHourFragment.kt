package com.latenightchauffeurs.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.latenightchauffeurs.databinding.DriverByTheHourLayoutBinding
import java.util.Objects

/**
 * Create by Sirumalayil on 01-04-2023.
 */
class DriverByTheHourFragment: Fragment() {

    companion object {
        fun newInstance(dataMap: HashMap<String, Object>?) = DriverByTheHourFragment().apply {
            this.dataMap = dataMap
        }
    }

    private var binding: DriverByTheHourLayoutBinding? = null
    private var dataMap: HashMap<String, Object>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DriverByTheHourLayoutBinding.inflate(inflater,container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickListeners()
        setViewData()

    }

    private fun setViewData() {
        if (dataMap != null && dataMap?.get("one") != null) {
            binding?.textPickupPlace?.setText(dataMap?.get("one")?.toString() ?: "")
        }
    }

    private fun onClickListeners() {
        binding?.toolbarDbh?.setNavigationOnClickListener {
            activity?.finish()
        }

        binding?.cardViewDatePicker?.setOnClickListener {
            showDatePickerDialog()
        }

        binding?.cardViewTimePicker?.setOnClickListener {
            showTimePickerDialog()
        }
    }

    private fun showTimePickerDialog() {

    }

    private fun showDatePickerDialog() {
        val builder = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())

        val picker = builder.build()
        picker.addOnPositiveButtonClickListener {

        }
        activity?.supportFragmentManager?.let { picker.show(it, "date_picker") }
    }
}