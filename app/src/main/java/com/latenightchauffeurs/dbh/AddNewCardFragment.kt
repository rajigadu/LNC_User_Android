package com.latenightchauffeurs.dbh

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.Utils.*
import com.latenightchauffeurs.databinding.FragmentAddNewCardBinding
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.model.response.CardDynamicFields
import com.latenightchauffeurs.dbh.utils.AlertDialogMessageFragment.Companion.ACTION_OK
import com.latenightchauffeurs.dbh.utils.ProgressCaller
import com.latenightchauffeurs.dbh.utils.Resource
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel
import com.latenightchauffeurs.fragment.BookReservation_new
import com.latenightchauffeurs.model.ItemCardList
import com.latenightchauffeurs.model.SavePref
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.json.JSONObject
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*


/**
 * Create by Sirumalayil on 01-04-2023.
 */
class AddNewCardFragment : Fragment() {

    private var binding: FragmentAddNewCardBinding? = null
    private var dataMap: HashMap<String, Any>? = null
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
        binding?.newCardLayout?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        preferences = SavePref()
        getCardDetails()
        onClickListeners()
        doTextChangeListener()
    }

    /**
     * Card Expiry field validating here
     */
    private fun doTextChangeListener() {
        binding?.edtTextCardExpiry?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val len: Int = binding?.edtTextCardExpiry?.text.toString().length
                if (len == 2) {
                    var string: String = binding?.edtTextCardExpiry?.text.toString()
                    if (string.startsWith("01") ||
                        string.startsWith("02") ||
                        string.startsWith("03") ||
                        string.startsWith("04") ||
                        string.startsWith("05") ||
                        string.startsWith("06") ||
                        string.startsWith("07") ||
                        string.startsWith("08") ||
                        string.startsWith("09") ||
                        string.startsWith("10") ||
                        string.startsWith("11") ||
                        string.startsWith("12")
                    ) {
                        string = binding?.edtTextCardExpiry?.text.toString() + "/"
                        binding?.textInputLayoutCardExpiry?.requestFocus()
                        binding?.edtTextCardExpiry?.setText(string)
                        val textLength: Int? = binding?.edtTextCardExpiry?.text?.length
                        binding?.edtTextCardExpiry?.setSelection(textLength!!, textLength)
                    } else {
                        binding?.edtTextCardExpiry?.setText("")
                    }
                }
                if (len == 3) {
                    binding?.edtTextCardExpiry?.selectAll()
                }
                if (len == 5) {
                    binding?.textInputLayoutCardExpiry?.error = null
                    binding?.textInputLayoutCardExpiry?.clearFocus()
                }
            }
            override fun afterTextChanged(s: Editable) {} })

        binding?.edtTextPostal?.doOnTextChanged { text, start, before, count ->
            if (TextUtils.isEmpty(text)) {
                binding?.textInputLayoutPostal?.error = null
                binding?.textInputLayoutPostal?.clearFocus()
            }
        }
        binding?.edtTextCardcvv?.doOnTextChanged { text, start, before, count ->
            if (TextUtils.isEmpty(text)) {
                binding?.textInputLayoutCardCvv?.error = null
                binding?.textInputLayoutCardCvv?.clearFocus()
            }
        }
        binding?.edtTextCardNumber?.doOnTextChanged { text, start, before, count ->
            if (TextUtils.isEmpty(text)) {
                binding?.textInputLayoutCardNumber?.error = null
                binding?.textInputLayoutCardNumber?.clearFocus()
            }
        }
        binding?.edtTextCardHolder?.doOnTextChanged { text, start, before, count ->
            if (TextUtils.isEmpty(text)) {
                binding?.textInputLayoutCardHolder?.error = null
                binding?.textInputLayoutCardHolder?.clearFocus()
            }
        }
    }

    private fun onClickListeners() {
        binding?.btnPromoCode?.setOnClickListener {
            binding?.btnPromoCodeView?.isVisible = true
            binding?.btnPromoCode?.visibility = View.INVISIBLE
        }
        binding?.applyPromoCode?.setOnClickListener {
            val promoCode = binding?.extTextPromoCode?.text?.toString()?.trim()
            if (!TextUtils.isEmpty(promoCode)) {
                applyPromoCode(promoCode)
                Utils.hideSoftKeyboard(activity)
            } else {
                Utils.toastTxt("Enter a valid Promo Code", activity)
                ProgressCaller.hideProgressDialog()
            }
        }
        binding?.btnBookMtChauffeur?.setOnClickListener {
            if (validated()) submitDetails()
        }
        binding?.btnAddCard?.setOnClickListener {
            if (validatedCardFields()) addNewCard()
        }
        binding?.switchNewCard?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                TransitionManager.beginDelayedTransition(
                    binding?.newCardLayout!!,
                    AutoTransition()
                )
                binding?.newCardLayout?.isVisible = true
            } else {
                TransitionManager.beginDelayedTransition(
                    binding?.newCardLayout!!,
                    AutoTransition()
                )
                binding?.newCardLayout?.isVisible = false
                clearFields()
            }
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
                            val msg = jsonObject.getString("msg")
                            (activity as? BaseActivity)?.showAlertMessageDialog(message = msg)
                        }
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

    /**
     * Clear EditTextInputLayout fields error
     */
    private fun clearFields() {
        // Clear the text in all EditTexts
        binding?.edtTextCardExpiry?.setText("")
        binding?.edtTextCardHolder?.setText("")
        binding?.edtTextCardNumber?.setText("")
        binding?.edtTextCardcvv?.setText("")
        binding?.edtTextPostal?.setText("")

        // Clear the error messages for all TextInputLayouts
        binding?.textInputLayoutCardNumber?.error = null
        binding?.textInputLayoutCardNumber?.isErrorEnabled = false
        binding?.textInputLayoutCardNumber?.errorIconDrawable = null
        binding?.textInputLayoutCardExpiry?.error = null
        binding?.textInputLayoutCardExpiry?.isErrorEnabled = false
        binding?.textInputLayoutCardExpiry?.errorIconDrawable = null
        binding?.textInputLayoutCardHolder?.error = null
        binding?.textInputLayoutCardHolder?.isErrorEnabled = false
        binding?.textInputLayoutCardHolder?.errorIconDrawable = null
        binding?.textInputLayoutCardCvv?.error = null
        binding?.textInputLayoutCardCvv?.isErrorEnabled = false
        binding?.textInputLayoutCardCvv?.errorIconDrawable = null
        binding?.textInputLayoutPostal?.error = null
        binding?.textInputLayoutPostal?.isErrorEnabled = false
        binding?.textInputLayoutPostal?.errorIconDrawable = null
    }

    /**
     * Here will add new Card API
     * Once the validation are satisfied will call add new card Server API
     */
    private fun addNewCard() {
        Utils.hideSoftKeyboard(activity)
        val cardHolderName = binding?.edtTextCardHolder?.text?.toString()?.trim()
        val cardNumber = binding?.edtTextCardNumber?.text?.toString()?.trim()
        val cardExpiry = binding?.edtTextCardExpiry?.text?.toString()?.trim()
        val cardCvv = binding?.edtTextCardcvv?.text?.toString()?.trim()
        val postalCode = binding?.edtTextPostal?.text?.toString()?.trim()
        var expiryMonth: String? = null
        var expiryYear: String? = null
        if (cardExpiry?.contains("/") == true) {
            expiryMonth =
                cardExpiry.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            expiryYear =
                "20" + cardExpiry.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1]
        }
        Log.e(TAG, "expMonth: $expiryMonth  expYear: $expiryYear")

        /**
         * Generate Multipart body here
         */
        val newCardRequest = ArrayList<MultipartBody.Part>()
        val uid = MultipartBody.Part.createFormData("userid", preferences?.userId!!)
        newCardRequest.add(uid)
        val cardName1 = MultipartBody.Part.createFormData("name", cardHolderName!!)
        newCardRequest.add(cardName1)
        val cardNumber1 = MultipartBody.Part.createFormData("account", cardNumber!!)
        newCardRequest.add(cardNumber1)
        val expiry1 = MultipartBody.Part.createFormData("exp", cardExpiry!!)
        newCardRequest.add(expiry1)
        val cvv1 = MultipartBody.Part.createFormData("cvv", cardCvv!!)
        newCardRequest.add(cvv1)
        val postal1 = MultipartBody.Part.createFormData("postal", postalCode!!)
        newCardRequest.add(postal1)

        bookingViewModel?.addNewCard(newCardRequest)?.observe(viewLifecycleOwner) { uiModel ->
            when (uiModel.status) {
                Resource.Status.LOADING -> {
                    activity?.let { ProgressCaller.showProgressDialog(it) }
                }
                Resource.Status.SUCCESS -> {

                    try {
                        val response = uiModel.data as ResponseBody
                        val responseBufferedSource = response.source()?.buffer()
                        val responseString =
                            responseBufferedSource?.readString(Charset.defaultCharset())
                        val jsonObject = responseString?.let { JSONObject(it) }

                        setNewCardData(jsonObject)

                        Log.e(TAG, "Success NEW CARD ADDED: $responseString")
                    } catch (e: java.lang.Exception) {
                        Log.e(TAG, "Exception NEW CARD ADDED: ${e.localizedMessage}")
                    }

                    ProgressCaller.hideProgressDialog()
                }
                Resource.Status.ERROR -> {
                    Log.e(TAG, "Failure NEW CARD ADDED: ${uiModel.message}")
                    ProgressCaller.hideProgressDialog()
                }
            }
        }
    }

    private fun setNewCardData(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            when (jsonObject.getString("status")) {
                "1" -> {
                    val array = jsonObject.getJSONArray("data")
                    if (array.length() > 0) {
                        val jsonObject1 = array.getJSONObject(0)
                        val msg = jsonObject1.getString("msg")
                        (activity as? BaseActivity)?.showAlertMessageDialog(message = msg)
                        clearFields()
                        getCardDetails()
                    }
                }
                else -> {
                    val array = jsonObject.getJSONArray("data")
                    if (array.length() > 0) {
                        val jsonObject1 = array.getJSONObject(0)
                        val msg = jsonObject1.getString("msg")
                        if (msg.equals(
                                "Service not allowed",
                                ignoreCase = true
                            ) || msg.equals("Bad card check digit", ignoreCase = true)
                        ) {
                            (activity as? BaseActivity)?.showAlertMessageDialog(message = msg)
                        } else {
                            (activity as? BaseActivity)?.showAlertMessageDialog(message = msg)
                        }
                    }
                }
            }
        }
    }

    /**
     * Validating New card fields,
     * All Edit fields are validating here and it's success will [addNewCard]
     */
    private fun validatedCardFields(): Boolean {
        val cardHolderName = binding?.edtTextCardHolder?.text?.toString()?.trim()
        val cardNumber = binding?.edtTextCardNumber?.text?.toString()?.trim()
        val cardExpiry = binding?.edtTextCardExpiry?.text?.toString()?.trim()
        val cardCvv = binding?.edtTextCardcvv?.text?.toString()?.trim()
        val postalCode = binding?.edtTextPostal?.text?.toString()?.trim()

        val listOfFields = arrayListOf<CardDynamicFields>().apply {
            add(
                CardDynamicFields(
                    binding?.edtTextCardHolder,
                    binding?.textInputLayoutCardHolder,
                    cardHolderName
                )
            )
            add(
                CardDynamicFields(
                    binding?.edtTextCardExpiry,
                    binding?.textInputLayoutCardExpiry,
                    cardExpiry
                )
            )
            add(
                CardDynamicFields(
                    binding?.edtTextCardNumber,
                    binding?.textInputLayoutCardNumber,
                    cardNumber
                )
            )
            add(
                CardDynamicFields(
                    binding?.edtTextCardcvv,
                    binding?.textInputLayoutCardCvv,
                    cardCvv
                )
            )
            add(
                CardDynamicFields(
                    binding?.edtTextPostal,
                    binding?.textInputLayoutPostal,
                    postalCode
                )
            )
        }

        listOfFields.forEach { dynamicFeilds ->
            if (TextUtils.isEmpty(dynamicFeilds.value)) {
                dynamicFeilds.inputLayout?.error =
                    "${dynamicFeilds.inputLayout?.hint} should not be empty"
            } else {
                dynamicFeilds.validated = true
            }
        }

        listOfFields.forEach {
            if (!it.validated) return false
        }

        return true
    }

    /**
     * Submit All Booking reservation data is here and it will validate,
     * once validation satisfied will [invokeDbhBookingReservation]
     */
    @SuppressLint("SimpleDateFormat")
    private fun submitDetails() {
        Utils.hideSoftKeyboard(activity)
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
                Log.e(TAG, "")
            } else {
                (activity as? BaseActivity)?.showAlertMessageDialog(
                    message = "Please select a date in future.",
                    title = "Booking Reservation",
                    callBack = object : FragmentCallBack {
                        override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                            when (param1) {
                                ACTION_OK -> {
                                    activity?.supportFragmentManager?.popBackStack()
                                }
                            }
                        }
                    }
                )
            }
        }
        val rideDate =
            BookReservation_new.convertDate("" + rideDateAndTime, "EEEE dd MMMM yyyy hh:mm a")

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
            (activity as? BaseActivity)?.showAlertMessageDialog(
                message = "Are you sure to book this ride for $rideDate ?",
                title = "Booking Reservation",
                callBack = object : FragmentCallBack {
                    override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                        when (param1) {
                            ACTION_OK -> {
                                invokeDbhBookingReservation(json)
                                Log.e(TAG, "Booking REQUEST: $json")
                            }
                        }
                    }
                }
            )
        }
    }

    /**
     * Here will invoking DBH Booking reservation Server API
     * and handling response
     */
    private fun invokeDbhBookingReservation(bookingData: String) {
        bookingViewModel?.dbhBookingReservation(bookingData)
            ?.observe(viewLifecycleOwner) { result ->
                Log.e(TAG, "Booking RESPONSE: $result")
                when (result.status) {
                    Resource.Status.LOADING -> {
                        context?.let { ProgressCaller.showProgressDialog(it) }
                    }
                    Resource.Status.SUCCESS -> {
                        val dbhResponse = result.data
                        (activity as? BaseActivity)?.showAlertMessageDialog(
                            message = dbhResponse?.data?.firstOrNull()?.msg ?: "",
                            title = "Booking Response",
                            negativeButton = false,
                            callBack = object : FragmentCallBack {
                                override fun onResult(param1: Any?, param2: Any?, param3: Any?) {}
                            }
                        )
                        ProgressCaller.hideProgressDialog()
                    }
                    Resource.Status.ERROR -> {
                        (activity as? BaseActivity)?.showAlertMessageDialog(
                            message = result.message ?: "",
                            title = "Booking Response",
                            negativeButton = false,
                            callBack = object : FragmentCallBack {
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
        preferences?.SavePref(activity)

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
                        initializeCardListAdapter(cardList)
                        ProgressCaller.hideProgressDialog()
                    }
                    Resource.Status.ERROR -> {
                        Utils.toastTxt(uiModel.message, activity)
                        ProgressCaller.hideProgressDialog()
                    }
                }
            }
    }
}