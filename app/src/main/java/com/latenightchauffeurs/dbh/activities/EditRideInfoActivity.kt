package com.latenightchauffeurs.dbh.activities

import android.os.Bundle
import com.latenightchauffeurs.Utils.ConstantUtil
import com.latenightchauffeurs.databinding.ActivityDbhLayoutBinding
import com.latenightchauffeurs.dbh.fragments.DbhRideFeedbackFragment
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.model.response.DbhRide
import com.latenightchauffeurs.extension.navigate

/**
 * Create by Siru Malayil on 20-04-2023.
 */

class EditRideInfoActivity: BaseActivity() {

    private var binding: ActivityDbhLayoutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDbhLayoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.toolbarDbh?.title = "Edit Ride Info"
        val rideInfo = intent?.getParcelableExtra(ConstantUtil.RIDE_INFO) as? DbhRide

        if (savedInstanceState == null) {
            navigate(
                fragment = DbhRideFeedbackFragment.newInstance(
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