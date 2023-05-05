package com.latenightchauffeurs.dbh.activities

import android.os.Bundle
import com.latenightchauffeurs.databinding.ActivityDbhLayoutBinding
import com.latenightchauffeurs.extension.navigate
import com.latenightchauffeurs.dbh.fragments.DriverByTheHourFragment
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.fragments.DbhRideInfoViewDetailsFragment
import com.latenightchauffeurs.dbh.model.response.DbhRide
import com.latenightchauffeurs.dbh.utils.DbhUtils

/**
 * Create by Sirumalayil on 01-04-2023.
 */

const val TAG = "DBH Booking Reservation"
class DriveByHourActivity: BaseActivity() {

    private var binding: ActivityDbhLayoutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDbhLayoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.toolbarDbh?.title = "Driver By The Hour"
        val dataMap = intent?.extras?.getSerializable(DbhUtils.DATA_MAP) as? HashMap<String, Any>
        val rideInfo = intent?.getParcelableExtra(DbhUtils.RIDE_INFO) as? DbhRide
        val viewOrEdit = intent?.getStringExtra(DbhUtils.EDIT_RIDE_INFO)

        if (savedInstanceState == null) {
            when (viewOrEdit) {
                "edit" -> {
                    navigate(
                        fragment = DriverByTheHourFragment.newInstance(
                            dataMap = dataMap,
                            isEditableRide = true,
                            rideInfo = rideInfo
                        ),addToBackStack = false
                    )
                }
                "view" -> {
                    navigate(
                        fragment = DbhRideInfoViewDetailsFragment.newInstance(
                            rideInfo = rideInfo
                        ),addToBackStack = false
                    )
                }
                else -> {
                    navigate(
                        fragment = DriverByTheHourFragment.newInstance(
                            dataMap = dataMap,
                            isEditableRide = false,
                            rideInfo = rideInfo
                        ),addToBackStack = false
                    )
                }
            }
        }

        binding?.toolbarDbh?.setNavigationOnClickListener {
            if (supportFragmentManager.fragments.size > 1) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        }
    }
}