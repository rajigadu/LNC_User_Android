package com.latenightchauffeurs.dbh

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.R
import com.latenightchauffeurs.Utils.*
import com.latenightchauffeurs.databinding.FragmentAddNewCardBinding
import com.latenightchauffeurs.dbh.model.response.DbhBookingResponse
import com.latenightchauffeurs.dbh.utils.DbhUtils.ACTION_CANCEL
import com.latenightchauffeurs.dbh.utils.DbhUtils.ACTION_OK
import com.latenightchauffeurs.dbh.utils.DbhUtils.showAlertDialog
import com.latenightchauffeurs.dbh.utils.Resource
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel
import com.latenightchauffeurs.fragment.BookReservation_new
import com.latenightchauffeurs.model.ItemCardList
import com.latenightchauffeurs.model.SavePref
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * Create by Sirumalayil on 01-04-2023.
 */
class AddNewCardFragment: Fragment() {

    private var binding: FragmentAddNewCardBinding? = null
    private var dataMap: HashMap<String,Any>? = null
    private var cardListAdapter: CardListAdapter? = null
    private var selectedCard: ItemCardList? = null
    private var preferences: SavePref? = null
    private var bookingViewModel: DbhViewModel? = null


    companion object {
        fun newInstance(dataMap: HashMap<String, Any>? = null) = AddNewCardFragment().apply {
            this.dataMap = dataMap
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewCardBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookingViewModel = ViewModelProvider(this)[DbhViewModel::class.java]
        preferences = SavePref()
        getCardDetails()
        onClickListeners()
    }

    private fun onClickListeners() {
        binding?.btnBookMtChauffeur?.setOnClickListener {

        }
        binding?.btnPromoCode?.setOnClickListener {
            binding?.btnPromoCodeView?.isVisible = true
            binding?.btnPromoCode?.visibility = View.INVISIBLE
        }
        binding?.applyPromoCode?.setOnClickListener {
            val promoCode = binding?.extTextPromoCode?.text?.toString()?.trim()
            if (!TextUtils.isEmpty(promoCode)) {
                dataMap!!["promo"] = promoCode.toString()
                OnlineRequest.applyPromo(activity,dataMap)
                Utils.hideSoftKeyboard(activity)
            } else {
                Utils.toastTxt("Enter a valid Promo Code", activity)
                ProgressCaller.hideProgressDialog()
            }
        }
        binding?.btnBookMtChauffeur?.setOnClickListener {
            if (validated()) submitDetails()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun submitDetails() {
        val dateRide = dataMap?.get("date_ride").toString()
        val timeRide = dataMap?.get("time_ride").toString()
        val dateCurrent = Calendar.getInstance().time
        val sdf = SimpleDateFormat("M-dd-yyyy hh:mm a")
        val date = sdf.parse("$dateRide $timeRide")

        val rideDateAndTime = date?.time
        val formattedCurrentDate = sdf.format(dateCurrent)
        val currentDateAndTime = sdf.parse(formattedCurrentDate)
        val currentDateLong = currentDateAndTime?.time

        if (rideDateAndTime != null) {
            if (rideDateAndTime >= currentDateLong!!) {
                Log.e("TAG","")
            } else {
                activity?.let { activity ->
                    showAlertDialog(
                        context = activity,
                        message = "Please select a date in future.",
                        title = "Booking Reservation",
                        callback = object : FragmentCallBack {
                            override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                                when(param1) {
                                    ACTION_OK -> {
                                        activity.supportFragmentManager.popBackStack()
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
        val rideDate = BookReservation_new.convertDate("" + rideDateAndTime, "EEEE dd MMMM yyyy hh:mm a")

        val jsonMap = HashMap<String, String>()
        jsonMap["userid"] = preferences?.userId ?: ""
        jsonMap["card_id"] = selectedCard?.token ?: ""
        jsonMap["acctid"] = selectedCard?.acctid ?: ""
        jsonMap["platitude"] = dataMap?.get("two").toString()
        jsonMap["plongitude"] = dataMap?.get("three").toString()
        jsonMap["pickup_address"] = dataMap?.get("one").toString()
        jsonMap["pickup_city"] = dataMap?.get("city_name").toString()
        jsonMap["notes"] = dataMap?.get("notes").toString()
        jsonMap["booking_type"] = "3"
        jsonMap["date"] = dateRide
        jsonMap["time"] = timeRide
        jsonMap["transmission"] = "automatic"
        jsonMap["promo"] = dataMap?.get("promo").toString()
        jsonMap["version"] = "yes"

        val jsonObject = (jsonMap as Map<*, *>?)?.let { JSONObject(it) }
        val json = jsonObject.toString()

        activity?.let { activity ->
            showAlertDialog(
                context = activity,
                message = "Are you sure to book this ride for $rideDate ?",
                title = "Booking Reservation",
                callback = object : FragmentCallBack {
                    override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                        when(param1) {
                            ACTION_OK -> {
                                invokeDbhBookingReservation(json)
                            }
                        }
                    }
                }
            )
        }
    }

    private fun invokeDbhBookingReservation(bookingData: String) {
        bookingViewModel?.dbhBookingReservation(bookingData)?.observe(viewLifecycleOwner) { result ->
            Log.e("TAG", "Response SUCCESSFUL: $result")
            when(result.status) {
                Resource.Status.LOADING -> {context?.let { ProgressCaller.showProgressDialog(it) }}
                Resource.Status.SUCCESS -> {
                    val dbhResponse = result.data
                    showAlertDialog(
                        context = requireContext(),
                        message = dbhResponse?.data?.firstOrNull()?.msg ?: "",
                        title = "Booking Response",
                        callback = object: FragmentCallBack {
                            override fun onResult(param1: Any?, param2: Any?, param3: Any?) {}
                        }
                    )
                    ProgressCaller.hideProgressDialog()
                }
                Resource.Status.ERROR -> {
                    showAlertDialog(
                        context = requireContext(),
                        message = result.message ?: "",
                        title = "Booking Response",
                        callback = object: FragmentCallBack {
                            override fun onResult(param1: Any?, param2: Any?, param3: Any?) {}
                        }
                    )
                    ProgressCaller.hideProgressDialog()
                }
            }
        }
    }

    private fun validated(): Boolean {
        val dateRide = dataMap?.get("date_ride")
        val timeRide = dataMap?.get("time_ride")
        if (selectedCard == null) {
            Utils.toastTxt("Please select a card", activity)
            return false
        }
        if (dateRide == null) {
            Utils.toastTxt("Please select a Ride Date", activity)
            return false
        }
        if (timeRide == null) {
            Utils.toastTxt("Please select Ride Time", activity)
            return false
        }
        return true
    }

    /**
     * After fetching card list details will initialize
     * @see CardListAdapter for card lists
     */
    private fun initializeCardListAdapter(cardList: ArrayList<ItemCardList>) {
        cardListAdapter = CardListAdapter(object : FragmentCallBack {
            override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                selectedCard = param1 as? ItemCardList
            }
        })
        binding?.rvCardList?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = cardListAdapter
        }
        cardListAdapter?.submitList(cardList)
    }

    /**
     * Fetch cardList details from server and updating to
     * Adapter for listing card items
     */
    private fun getCardDetails() {
        activity?.let { ProgressCaller.showProgressDialog(it) }
        preferences?.SavePref(activity)

        val apiInterface = APIClient.getClientVO().create(APIInterface::class.java)
        val call = apiInterface.cardList(preferences?.userId)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val responseBufferedSource = response.body()?.source()?.buffer()
                        val responseString = responseBufferedSource?.readString(Charset.defaultCharset())
                        val cardList = ParsingHelper().getCardList(responseString)
                        initializeCardListAdapter(cardList)
                    }
                }
                ProgressCaller.hideProgressDialog()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Utils.toastTxt(t.message, activity)
                ProgressCaller.hideProgressDialog()
            }
        })
    }
}