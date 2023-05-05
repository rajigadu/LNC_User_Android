package com.latenightchauffeurs.dbh.activities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.databinding.FragmentDbhFeedbackBinding
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.model.response.DbhRideHistoryData
import com.latenightchauffeurs.dbh.utils.DbhUtils
import com.latenightchauffeurs.dbh.utils.ProgressCaller
import com.latenightchauffeurs.dbh.utils.Resource
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel
import org.json.JSONObject

/**
 * Create by Siru Malayil on 20-04-2023.
 */
class DbhRideFeedback : BaseActivity() {

    private var binding: FragmentDbhFeedbackBinding? = null
    private var rideHistory: DbhRideHistoryData? = null
    private var startRating: String? = null
    private var dbhViewModel: DbhViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDbhFeedbackBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        dbhViewModel = ViewModelProvider(this)[DbhViewModel::class.java]
        rideHistory = intent?.extras?.getParcelable(DbhUtils.RIDE_HISTORY) as? DbhRideHistoryData

        onClickListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onClickListener() {
        binding?.toolbarFeedback?.setNavigationOnClickListener {
            finish()
        }

        binding?.btnSubmit?.setOnClickListener {
            if (validated()) submitFeedback()
        }

        binding?.edtTextNotes?.doOnTextChanged { text, start, before, count ->
            if (TextUtils.isEmpty(text)) {
                binding?.edtTextNotes?.error = "should not be empty"
            } else binding?.edtTextNotes?.error = null
        }

        binding?.nestedScrollView?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Get the current focus and check if it's an EditText
                val currentFocus = currentFocus
                if (currentFocus is AppCompatEditText) {
                    // Get the rectangle of the EditText and the touch event
                    val outRect = Rect()
                    currentFocus.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        // Hide the keyboard
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                        // Clear the focus from the EditText
                        currentFocus.clearFocus()
                    }
                }
            }
            false
        }
    }

    private fun validated(): Boolean {
        val textNotes = binding?.edtTextNotes?.text?.toString()?.trim()
        startRating = binding?.rating?.rating.toString()

        if (TextUtils.isEmpty(textNotes)) {
            binding?.edtTextNotes?.error = "should not be empty"
            return false
        }
        if (startRating == null || startRating == "0.0") {
            Toast.makeText(this, "Please rate your ride", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun submitFeedback() {
        val json = JSONObject()
        json.put("userid", rideHistory?.user_id)
        json.put("driverid", rideHistory?.driver_id_for_future_ride)
        json.put("rideid", rideHistory?.id)
        json.put("msg", binding?.edtTextNotes?.text?.toString()?.trim())
        json.put("rating", startRating)
        json.put("tip", "")
        json.put("percentage", "")

        dbhViewModel?.dbhRideFeedback(json)?.observe(this) { result ->
            when(result.status) {
                Resource.Status.LOADING -> { ProgressCaller.showProgressDialog(this)}
                Resource.Status.SUCCESS -> {
                    showAlertMessageDialog(
                        message = result.data?.data?.firstOrNull()?.msg ?: "",
                        callBack = object : FragmentCallBack {
                            override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                                finish()
                            }
                        }
                    )
                    ProgressCaller.hideProgressDialog()
                }
                Resource.Status.ERROR -> {
                    showAlertMessageDialog(
                        message = result.data?.data?.firstOrNull()?.msg)
                    ProgressCaller.hideProgressDialog()
                }
            }
        }
    }

}