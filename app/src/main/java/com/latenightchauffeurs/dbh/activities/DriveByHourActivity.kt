package com.latenightchauffeurs.dbh.activities

import android.os.Bundle
import com.latenightchauffeurs.Utils.ConstantUtil
import com.latenightchauffeurs.databinding.ActivityDbhLayoutBinding
import com.latenightchauffeurs.extension.navigate
import com.latenightchauffeurs.dbh.fragments.DriverByTheHourFragment
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.model.response.DbhRide

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
        val dataMap = intent?.extras?.getSerializable(ConstantUtil.DATA_MAP) as? HashMap<String, Any>
        val rideInfo = intent?.getParcelableExtra(ConstantUtil.RIDE_INFO) as? DbhRide
        val isEditableRide = intent?.getBooleanExtra(ConstantUtil.EDIT_RIDE_INFO, false) as Boolean

        if (savedInstanceState == null) {
            navigate(
                fragment = DriverByTheHourFragment.newInstance(
                    dataMap = dataMap,
                    isEditableRide = isEditableRide,
                    rideInfo = rideInfo
                ),addToBackStack = false
            )
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