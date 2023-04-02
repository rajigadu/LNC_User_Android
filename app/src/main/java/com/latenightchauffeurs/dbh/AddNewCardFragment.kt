package com.latenightchauffeurs.dbh

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.Utils.APIClient
import com.latenightchauffeurs.Utils.APIInterface
import com.latenightchauffeurs.Utils.ParsingHelper
import com.latenightchauffeurs.Utils.Utils
import com.latenightchauffeurs.databinding.FragmentAddNewCardBinding
import com.latenightchauffeurs.model.ItemCardList
import com.latenightchauffeurs.model.SavePref
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.charset.Charset
import java.util.ArrayList

/**
 * Create by Sirumalayil on 01-04-2023.
 */
class AddNewCardFragment: Fragment() {

    private var binding: FragmentAddNewCardBinding? = null
    private var dataMap: HashMap<String,Any>? = null
    private var cardListAdapter: CardListAdapter? = null


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

        getCardDetails()
    }

    /**
     * After fetching card list details will initialize
     * @see CardListAdapter for card lists
     */
    private fun initializeCardListAdapter(cardList: ArrayList<ItemCardList>) {
        cardListAdapter = CardListAdapter(object : FragmentCallBack {
            override fun onResult(param1: Any?, param2: Any?, param3: Any?) {

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
        val preferences = SavePref()
        preferences.SavePref(activity)

        val apiInterface = APIClient.getClientVO().create(APIInterface::class.java)
        val call = apiInterface.cardList(preferences.userId)
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
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Utils.toastTxt(t.message, activity)
            }
        })
    }
}