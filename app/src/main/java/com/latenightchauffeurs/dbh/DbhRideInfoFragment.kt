package com.latenightchauffeurs.dbh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.latenightchauffeurs.databinding.FragmentDbhRideInfoLayoutBinding
import com.latenightchauffeurs.extension.navigate

/**
 * Create by Siru Malayil on 15-04-2023.
 */
class DbhRideInfoFragment : Fragment() {

    private var binding: FragmentDbhRideInfoLayoutBinding? = null

    companion object {
        fun newInstance() = DbhRideInfoFragment.apply {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDbhRideInfoLayoutBinding.inflate(layoutInflater,container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabs()

    }

    private fun setupTabs() {
        binding?.dbhRideTabs?.newTab()?.setText("UpComing")?.let { binding?.dbhRideTabs?.addTab(it) }
        binding?.dbhRideTabs?.newTab()?.setText("History")?.let { binding?.dbhRideTabs?.addTab(it) }

        binding?.dbhRideTabs?.getTabAt(0)?.select()
        setTabPositions(binding?.dbhRideTabs?.selectedTabPosition)


        binding?.dbhRideTabs?.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                setTabPositions(tab?.position)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {} })
    }

    private fun setTabPositions(selectedTabPosition: Int?) {
        when(selectedTabPosition) {
            0 -> {
                binding?.dbhRideTabs?.getTabAt(0)
                (activity as? AppCompatActivity)?.navigate(
                    DbhUpcomingRidesFragment()
                )
            }
            else -> {
                binding?.dbhRideTabs?.getTabAt(1)
                (activity as? AppCompatActivity)?.navigate(
                    DbhRideHistoryFragment()
                )
            }
        }
    }
}