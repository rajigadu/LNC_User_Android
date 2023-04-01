package com.latenightchauffeurs.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.latenightchauffeurs.Utils.ConstantUtil
import com.latenightchauffeurs.databinding.ActivityDbhLayoutBinding
import com.latenightchauffeurs.extension.navigate
import com.latenightchauffeurs.fragment.DriverByTheHourFragment
import java.util.Objects

/**
 * Create by Sirumalayil on 01-04-2023.
 */
class DriveByHourActivity: AppCompatActivity() {

    private var binding: ActivityDbhLayoutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDbhLayoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val dataMap = intent?.extras?.getSerializable(ConstantUtil.DATA_MAP) as HashMap<String, Object>

        if (savedInstanceState == null) {
            navigate(
                fragment = DriverByTheHourFragment.newInstance(
                    dataMap = dataMap
                ),addToBackStack = false
            )
        }
    }
}