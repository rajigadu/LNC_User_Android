package com.latenightchauffeurs.dbh.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.Utils.ParsingHelper
import com.latenightchauffeurs.Utils.Utils
import com.latenightchauffeurs.databinding.FragmentChooseCardViewBinding
import com.latenightchauffeurs.dbh.activities.TAG
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.model.response.DbhRide
import com.latenightchauffeurs.dbh.utils.ProgressCaller
import com.latenightchauffeurs.dbh.utils.Resource
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel
import com.latenightchauffeurs.extension.navigate
import com.latenightchauffeurs.model.ItemCardList
import com.latenightchauffeurs.model.SavePref
import okhttp3.ResponseBody
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.HashMap

/**
 * Create by Siru Malayil on 20-04-2023.
 */
class ChooseCardFragment : Fragment(){

    private var binding: FragmentChooseCardViewBinding? = null
    private var bookingViewModel: DbhViewModel? = null
    private var dataMap: HashMap<String, Any>? = null
    private var rideInfo: DbhRide? = null
    private var preferences:  SavePref? = null
    private var selectedCard: List<ItemCardList>? = null

    companion object {
        fun newInstance(dataMap: HashMap<String, Any>?, rideInfo: DbhRide?) = ChooseCardFragment().apply {
            this.dataMap = dataMap
            this.rideInfo = rideInfo
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseCardViewBinding.inflate(layoutInflater, container, false)
        bookingViewModel = ViewModelProvider(this)[DbhViewModel::class.java]
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = SavePref()
        preferences?.SavePref(activity)
        setOnClickListeners()
        getCardDetails()
    }

    private fun updateCardDetails() {
        //binding?.cardExpiryMonth?.text = dataMap
    }

    /**
     * Fetch cardList details from server and updating to
     * Adapter for listing card items
     */
    private fun getCardDetails() {
        bookingViewModel?.getCardDetails(preferences?.userId)
            ?.observe(viewLifecycleOwner) { uiModel ->
                when (uiModel.status) {
                    Resource.Status.LOADING -> {
                        activity?.let { ProgressCaller.showProgressDialog(it) }
                    }
                    Resource.Status.SUCCESS -> {
                        val response = uiModel.data as ResponseBody
                        val responseBufferedSource = response.source()?.buffer()
                        val responseString =
                            responseBufferedSource?.readString(Charset.defaultCharset())
                        val cardList = ParsingHelper().getCardList(responseString)
                        selectedCard = cardList?.filter { itemcard ->
                            itemcard.token == rideInfo?.card_id
                        }
                        ProgressCaller.hideProgressDialog()
                    }
                    Resource.Status.ERROR -> {
                        Utils.toastTxt(uiModel.message, activity)
                        ProgressCaller.hideProgressDialog()
                    }
                }
            }
    }

    private fun setOnClickListeners() {
        binding?.applyPromoCode?.setOnClickListener {
            val promoCode = binding?.extTextPromoCode?.text?.toString()?.trim()
            if (!TextUtils.isEmpty(promoCode)) {
                applyPromoCode(promoCode)
                Utils.hideSoftKeyboard(activity)
            } else {
                Utils.toastTxt("Enter a valid Promo Code", activity)
            }
        }
        binding?.btnChooseCard?.setOnClickListener {
            (activity as? AppCompatActivity)?.navigate(
                fragment = DbhCardSelectionFragment.newInstance(
                    dataMap = dataMap,
                    callback = object : FragmentCallBack {
                        override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                            //selectedCard = param1 as ItemCardList
                            updateCardDetails()
                        }
                    }
                )
            )
        }
    }

    private fun applyPromoCode(promoCode: String?) {
        bookingViewModel?.applyPromoCode(promoCode)?.observe(viewLifecycleOwner) { uiModel ->
            when(uiModel.status) {
                Resource.Status.LOADING -> { activity?.let { ProgressCaller.showProgressDialog(it) }}
                Resource.Status.SUCCESS -> {
                    try {
                        val response = uiModel.data as ResponseBody
                        val responseBufferedSource = response.source()?.buffer()
                        val responseString =
                            responseBufferedSource?.readString(Charset.defaultCharset())
                        val jsonObject = responseString?.let { JSONObject(it) }
                        if (jsonObject?.getString("status") == "1") {
                            dataMap?.set("promo", promoCode.toString())
                        }
                        val msg = jsonObject?.getString("msg")
                        (activity as? BaseActivity)?.showAlertMessageDialog(message = msg)
                        Log.e(TAG,"Success APPLY_PROMO_CODE: $responseString")
                    }catch (e: Exception) {
                        Log.e(TAG,"Exception APPLY_PROMO_CODE: ${e.localizedMessage}")
                        (activity as? BaseActivity)?.showAlertMessageDialog(message = e.localizedMessage)
                    }
                    ProgressCaller.hideProgressDialog()
                }
                Resource.Status.ERROR -> {
                    Log.e(TAG,"Error APPLY_PROMO_CODE: ${uiModel.message}")
                    (activity as? BaseActivity)?.showAlertMessageDialog(message = uiModel.message)
                    ProgressCaller.hideProgressDialog()
                }
            }
        }
    }
}