package com.latenightchauffeurs.dbh.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.Utils.ParsingHelper
import com.latenightchauffeurs.Utils.Utils
import com.latenightchauffeurs.databinding.FragmentDbhCardChooseViewBinding
import com.latenightchauffeurs.dbh.activities.TAG
import com.latenightchauffeurs.dbh.adapter.CardListAdapter
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.model.response.CardDynamicFields
import com.latenightchauffeurs.dbh.utils.ProgressCaller
import com.latenightchauffeurs.dbh.utils.Resource
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel
import com.latenightchauffeurs.model.ItemCardList
import com.latenightchauffeurs.model.SavePref
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.ArrayList
import java.util.HashMap

/**
 * Create by Siru Malayil on 20-04-2023.
 */
class DbhCardSelectionFragment : Fragment() {

    private var binding: FragmentDbhCardChooseViewBinding? = null
    private var bookingViewModel: DbhViewModel? = null
    private var preferences: SavePref? = null
    private var selectedCard: ItemCardList? = null
    private var dataMap: HashMap<String, Any>? = null
    private var callback: FragmentCallBack? = null

    companion object {
        fun newInstance(dataMap: HashMap<String, Any>? = null,
                        callback: FragmentCallBack? = null) = DbhCardSelectionFragment().apply {
            this.dataMap = dataMap
            this.callback = callback
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDbhCardChooseViewBinding.inflate(layoutInflater, container, false)
        bookingViewModel = ViewModelProvider(this)[DbhViewModel::class.java]
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = SavePref()
        preferences?.SavePref(activity)
        getCardDetails()
        onClickListener()
    }

    private fun onClickListener() {
        binding?.btnAddCard?.setOnClickListener {
            if (validatedCardFields()) addNewCard()
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
                            (activity as? BaseActivity)?.showAlertMessageDialog(
                                message = "Please check you are provided Card details, $msg")
                        }
                    }
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
     * After fetching card list details will initialize
     * @see CardListAdapter for card lists
     */
    private fun initializeCardListAdapter(cardList: ArrayList<ItemCardList>) {
        val cardListAdapter = CardListAdapter(object : FragmentCallBack {
            override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                selectedCard = param1 as? ItemCardList
                activity?.supportFragmentManager?.popBackStack()
                callback?.onResult(selectedCard)
            }
        })
        binding?.rvCardList?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = cardListAdapter
        }
        cardListAdapter.submitList(cardList)
    }

}