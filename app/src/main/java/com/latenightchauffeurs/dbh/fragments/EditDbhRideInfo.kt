package com.latenightchauffeurs.dbh.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Geocoder
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.latenightchauffeurs.R
import com.latenightchauffeurs.databinding.FragmentEditDbhRideBinding
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.model.response.DbhRide
import com.latenightchauffeurs.extension.navigate
import java.util.*

/**
 * Create by Siru Malayil on 20-04-2023.
 */
class EditDbhRideInfo : Fragment() {

    private var binding: FragmentEditDbhRideBinding? = null
    private var rideInfo: DbhRide? = null
    private var lastClickTime: Long = 0
    private var dataMap: HashMap<String, Any>? = null
    private var fields = arrayListOf<Place.Field>()

    companion object {
        fun newInstance(rideInfo: DbhRide? = null) = EditDbhRideInfo().apply {
            this.rideInfo = rideInfo
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditDbhRideBinding.inflate(layoutInflater,container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAutoCompleteFragment()
        setRideInfo()
        setOnClickListner()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnClickListner() {
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
        binding?.btnSubmit?.setOnClickListener {
            if (validated()) submitDetails()
        }
    }

    private fun submitDetails() {
        dataMap?.set("date_ride", binding?.textDate?.text?.trim().toString()) ?: rideInfo?.otherdate
        dataMap?.set("time_ride", binding?.textTime?.text?.trim().toString()) ?: rideInfo?.time
        dataMap?.set("notes", binding?.edtTextNotes?.text?.trim().toString()) ?: rideInfo?.notes
        (activity as? AppCompatActivity)?.navigate(
            ChooseCardFragment.newInstance(dataMap, rideInfo)
        )
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

    private fun setRideInfo() {
        binding?.edtTextNotes?.setText(rideInfo?.notes)
        binding?.textDate?.setText(rideInfo?.otherdate)
        binding?.textTime?.setText(rideInfo?.time)
        binding?.textPickupPlace?.setText(rideInfo?.pickup_address)
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
}