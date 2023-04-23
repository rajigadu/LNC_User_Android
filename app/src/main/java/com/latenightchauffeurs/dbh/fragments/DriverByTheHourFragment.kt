package com.latenightchauffeurs.dbh.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Geocoder
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.latenightchauffeurs.R
import com.latenightchauffeurs.databinding.DriverByTheHourLayoutBinding
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.model.response.DbhRide
import com.latenightchauffeurs.extension.navigate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * Create by Sirumalayil on 01-04-2023.
 */
class DriverByTheHourFragment: Fragment() {

    companion object {
        fun newInstance(dataMap: HashMap<String, Any>?,
                        isEditableRide: Boolean = false,
                        rideInfo: DbhRide? = null
        ) = DriverByTheHourFragment().apply {
            this.dataMap = dataMap
            this.isEditableRide = isEditableRide
            this.rideInfo = rideInfo
        }
    }

    private var binding: DriverByTheHourLayoutBinding? = null
    private var dataMap: HashMap<String, Any>? = null
    private var lastClickTime: Long = 0
    private var fields = arrayListOf<Place.Field>()
    private var isEditableRide: Boolean = false
    private var rideInfo: DbhRide? = null

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

        if (isEditableRide) {
            setRideInfo()
        }
        onClickListeners()
        setViewData()
        initializeAutoCompleteFragment()

    }

    private fun setRideInfo() {
        binding?.edtTextNotes?.setText(rideInfo?.notes)
        binding?.textDate?.setText(rideInfo?.otherdate)
        binding?.textTime?.setText(rideInfo?.time)
        binding?.textPickupPlace?.setText(rideInfo?.pickup_address)
    }

    private fun initializeAutoCompleteFragment() {
        /** Initializing the Places API with the help of our API_KEY */
        if (!Places.isInitialized()) {
            Places.initialize(
                activity?.applicationContext ?: requireContext(),
                getString(R.string.google_map_key)
            )
        }
        /**
         * Set the fields to specify which types of place data to
         * return after the user has made a selection.
         */
        fields = arrayListOf(
            Place.Field.ID,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS,
            Place.Field.NAME
        )
    }

    private fun setViewData() {
        if (dataMap != null && dataMap?.get("one") != null) {
            binding?.textPickupPlace?.setText(dataMap?.get("one")?.toString() ?: "")
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onClickListeners() {

        binding?.textPickupPlace?.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return@setOnTouchListener false
                }
                lastClickTime = SystemClock.elapsedRealtime()
                val autoCompletePlaceIntent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields
                ).build(requireContext())
                launchPlaceIntentBuilder.launch(autoCompletePlaceIntent)
            }
            return@setOnTouchListener false
        }

        binding?.textDate?.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_UP -> {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                        return@setOnTouchListener false
                    }
                    lastClickTime = SystemClock.elapsedRealtime()
                    showDatePickerDialog()
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        }

        binding?.textTime?.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_UP -> {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                        return@setOnTouchListener false
                    }
                    lastClickTime = SystemClock.elapsedRealtime()
                    showTimePickerDialog()
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        }

        binding?.btnBookMyChauffeur?.setOnClickListener {
            if (validated()) bookMyChauffeur()
        }
    }

    private val launchPlaceIntentBuilder = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        when(result.resultCode) {
            Activity.RESULT_OK -> {
                result?.data?.let { intent ->
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    binding?.textPickupPlace?.setText(place.address)
                    place.address?.let { dataMap?.set("one", it) }
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    //TODO geocoder.getFromLocation is deprecated, need to set geocoder.getFromLocationAsync minSDK v26
                    val addresses = geocoder.getFromLocation(
                        place.latLng.latitude, place.latLng.longitude, 1
                    )
                    if (addresses != null) {
                        dataMap?.set("city_name", addresses.firstOrNull()?.locality.toString())
                    } else {
                        (activity as? BaseActivity)?.showAlertMessageDialog(
                            message = "No location found with these address."
                        )
                    }
                }
            }
        }
    }

    private fun validated(): Boolean {
        val dateValue = binding?.textDate?.text?.toString()?.trim()
        val timeValue = binding?.textTime?.text?.toString()?.trim()
        val location = binding?.textPickupPlace?.text?.toString()?.trim()

        if (location.isNullOrEmpty()) {
            Toast.makeText(context,"Please select pickup location", Toast.LENGTH_LONG).show()
            return false
        }
        if (dateValue.isNullOrEmpty()) {
            Toast.makeText(context,"Please select date", Toast.LENGTH_LONG).show()
            return false
        }
        if (timeValue.isNullOrEmpty()) {
            Toast.makeText(context,"Please select time", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun bookMyChauffeur() {
        if (isEditableRide) {
            dataMap = HashMap()
            dataMap?.set("one",rideInfo?.pickup_address ?: "")
            dataMap?.set("two",rideInfo?.pickup_lat ?: "")
            dataMap?.set("three",rideInfo?.pickup_long ?: "")
            dataMap?.set("city_name",rideInfo?.city_pickup ?: "")
        }
        dataMap?.set("date_ride", binding?.textDate?.text?.trim().toString())
        dataMap?.set("time_ride", binding?.textTime?.text?.trim().toString())
        dataMap?.set("notes", binding?.edtTextNotes?.text?.trim().toString())
        (activity as? AppCompatActivity)?.navigate(
            fragment = AddNewCardFragment.newInstance(
                dataMap = dataMap,
                isEditableRide = isEditableRide
            )
        )
    }

    /**
     * [MaterialTimePicker] will invoke here
     */
    private fun showTimePickerDialog() {
        // Get the current time in hours and minutes
        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentTime.get(Calendar.MINUTE)

        // Create a MaterialTimePicker.Builder object
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(currentHour)
            .setMinute(currentMinute)
            .setTitleText("Select Time")
            .build()

        // Set the time picker to allow selection of only future times
        timePicker.addOnPositiveButtonClickListener {
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            val isAM = selectedHour < 12

            // Convert the hour to 12-hour format
            val hour12 = if (selectedHour == 0) 12 else if (selectedHour > 12) selectedHour - 12 else selectedHour

            // Format the time as a string with AM/PM indicator
            val timeString = String.format("%02d:%02d %s", hour12, selectedMinute, if (isAM) "AM" else "PM")

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            calendar.set(Calendar.MINUTE, selectedMinute)

            // Check if the selected time is in the future
            if (calendar.timeInMillis > System.currentTimeMillis()) {
                // Do something with the selected time
                val selectedTime = "${selectedHour}:${selectedMinute}"
                binding?.textTime?.setText(timeString)
            } else {
                Toast.makeText(activity, "Please select a future time", Toast.LENGTH_SHORT).show()
            }
        }

        // Show the time picker dialog
        activity?.supportFragmentManager?.let { timePicker.show(it, "time_picker") }
    }

    /**
     * [MaterialDatePicker] will invoke here
     */
    @SuppressLint("SimpleDateFormat")
    private fun showDatePickerDialog() {
        val builder = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(
                constraintsBuilder.build()
            )
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())

        val picker = builder.build()
        picker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = Date(it)

            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            calendar.set(year,month, day)
            val format = SimpleDateFormat("MM-dd-yyyy")
            val strDate: String = format.format(calendar.time)
            binding?.textDate?.setText(strDate)
        }
        activity?.supportFragmentManager?.let { picker.show(it, "date_picker") }
    }

    /**
     * Allowing that enables dates from a given point forward.
     * Defaults to the current moment in device time forward using now(),
     * but can be set to any point, as UTC milliseconds, using from(long).
     */
    private val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())

}