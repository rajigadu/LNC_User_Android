package com.latenightchauffeurs.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cardconnect.consumersdk.CCConsumerTokenCallback;
import com.cardconnect.consumersdk.androidpay.CCConsumerAndroidPayConfiguration;
import com.cardconnect.consumersdk.domain.CCConsumerAccount;
import com.cardconnect.consumersdk.domain.CCConsumerCardInfo;
import com.cardconnect.consumersdk.domain.CCConsumerError;
import com.cardconnect.consumersdk.enums.CCConsumerCardMaskSpacing;
import com.cardconnect.consumersdk.enums.CCConsumerExpirationDateSeparator;
import com.cardconnect.consumersdk.enums.CCConsumerMaskFormat;
import com.cooltechworks.creditcarddesign.CardEditActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.cooltechworks.creditcarddesign.CreditCardView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.wallet.PaymentMethodToken;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.APIClient;
import com.latenightchauffeurs.Utils.APIInterface;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.MyApplication;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.activity.AddCard;
import com.latenightchauffeurs.adapter.CardsListAdapter;
import com.latenightchauffeurs.model.SavePref;
import com.latenightchauffeurs.model.modelItem;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class Cards extends Fragment implements View.OnClickListener {
    private static final String TAG = "Cards";
    private Unbinder unbinder;

    @BindView(R.id.add_card)
    Button addCardButton;

    private final int CREATE_NEW_CARD = 0;
    // private LinearLayout cardContainer;
    private ScrollView scrol;

    public static Cards Instance;
    public static Context mContext;
    private static AVLoadingIndicatorView avi;

    static RecyclerView rv_loc;
    public static CardsListAdapter requestsAdapter;
    public static RecyclerView.LayoutManager requestsListMan;
    public static List<modelItem> requestsList;
    public HashMap<String, Object> map;

    // public static String STRIPE_KEY="pk_test_FGO6NmzgfSidisSjcV5hJVF6";
    // public static String STRIPE_KEY="pk_test_Z2JL4YmwR8tAMn5ctCFMhh8A";
    // Live secret Stripe Key
    private String STRIPE_KEY = "pk_live_OYvx7pJBADWieTMlKcSohH8t";


    public Call<ResponseBody> call = null;
    public APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);

    RelativeLayout relativeLayoutCardContainer;

    Switch aSwitch;


    EditText editTextCardName, editTextCardNumber, editTextExpiryDate, editTextCVV, editTextPostalCode;
    Button buttonSubmit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_linkedcards, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, v);

        rv_loc = (RecyclerView) v.findViewById(R.id.rv_loc);

        aSwitch = (Switch) v.findViewById(R.id.addcard_top);
        relativeLayoutCardContainer = (RelativeLayout) v.findViewById(R.id.m_reative);

        relativeLayoutCardContainer.setVisibility(View.GONE);

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aSwitch.isChecked() == true){
                    relativeLayoutCardContainer.setVisibility(View.VISIBLE);
                }else{
                    relativeLayoutCardContainer.setVisibility(View.GONE);
                }
            }
        });




        editTextCardName = (EditText) v.findViewById(R.id.card_name);
        editTextCardNumber = (EditText) v.findViewById(R.id.card_number);
        editTextExpiryDate = (EditText) v.findViewById(R.id.button678789789);
        editTextCVV = (EditText) v.findViewById(R.id.button675768);
        editTextPostalCode = (EditText) v.findViewById(R.id.button423435);

        buttonSubmit = (Button) v.findViewById(R.id.submit);





        requestsListMan = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_loc.setHasFixedSize(true);
        rv_loc.setLayoutManager(requestsListMan);

        avi = (AVLoadingIndicatorView) v.findViewById(R.id.avi);
        avi.setIndicator("BallPulseIndicator");
        avi.hide();

        mContext = getContext();
        Instance = this;

        addCardButton.setOnClickListener(this);

        CreditCardListRequest();

        initialize(v);
        listeners();





        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (
                        !editTextCardName.getText().toString().equalsIgnoreCase("") ||
                                !editTextCardNumber.getText().toString().equalsIgnoreCase("") ||
                                !editTextExpiryDate.getText().toString().equalsIgnoreCase("") ||
                                !editTextCVV.getText().toString().equalsIgnoreCase("") ||
                                !editTextPostalCode.getText().toString().equalsIgnoreCase("")
//                                    ||
//                                    !editTextCVV.getText().toString().equalsIgnoreCase("")
                ) {

                    // if(editTextExpiryDate.getText().toString().contains("/")){
                    // String[] s = editTextExpiryDate.getText().toString().split("/");
                    callRefreshMethod(
                            editTextCardName.getText().toString(),
                            editTextCardNumber.getText().toString(),
                            editTextExpiryDate.getText().toString(),
                            editTextCVV.getText().toString(),
                            editTextPostalCode.getText().toString()

                    );


                } else {

                    Toast.makeText(getActivity() , "All field is required!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        editTextExpiryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                StringBuilder builder = new StringBuilder();
                int len = editTextExpiryDate.getText().toString().length();

                if (len == 2) {
                    String string = editTextExpiryDate.getText().toString();

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
                        string = editTextExpiryDate.getText().toString() + "/";

                        editTextExpiryDate.requestFocus();

                        editTextExpiryDate.setText(string);
                        //editText.setSelection(len);
                        int textLength = editTextExpiryDate.getText().length();
                        editTextExpiryDate.setSelection(textLength, textLength);

                    } else {
                        editTextExpiryDate.setText("");
                    }
                }
                if (len == 3) {
                    editTextExpiryDate.selectAll();
                }

                Log.d(TAG, "stringfff " + editTextExpiryDate.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        super.onViewCreated(v, savedInstanceState);
    }









    private void callRefreshMethod(String cardName, String cardNumber, String expiry, String cardCVV, String postal) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Doing something, please wait.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String expiryMonth = "";
        String expiryYear = "";
        if(expiry.contains("/")) {
            expiryMonth = expiry.split("/")[0];
            expiryYear = "20"+expiry.split("/")[1];
        }

        Log.e(TAG , "expiryMonth: "+expiryMonth);
        Log.e(TAG , "expiryYear: "+expiryYear);


        ArrayList<MultipartBody.Part> arrayListMash = new ArrayList<MultipartBody.Part>();
//        MultipartBody.Part key1 = MultipartBody.Part.createFormData(pref.getTokenKey(), pref.getTokenValue());
//        arrayListMash.add(key1);
        SavePref pref1 = new SavePref();
        pref1.SavePref(getActivity());
        String id = pref1.getUserId();

        MultipartBody.Part uid = MultipartBody.Part.createFormData("userid", id);
        arrayListMash.add(uid);

        MultipartBody.Part cardName1 = MultipartBody.Part.createFormData("name", cardName);
        arrayListMash.add(cardName1);

        MultipartBody.Part cardNumber1 = MultipartBody.Part.createFormData("account", cardNumber);
        arrayListMash.add(cardNumber1);

        MultipartBody.Part expiry1 = MultipartBody.Part.createFormData("exp", expiry);
        arrayListMash.add(expiry1);

        MultipartBody.Part cvv1 = MultipartBody.Part.createFormData("cvv", cardCVV);
        arrayListMash.add(cvv1);

        MultipartBody.Part postal1 = MultipartBody.Part.createFormData("postal", postal);
        arrayListMash.add(postal1);


        // userid=701&account=5178058269050897&exp=0122&postal=160071&cvv=291


        call = apiInterface.addCard(arrayListMash);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                String responseCode = "";
                try {
                    if (response.body() != null) {
                        responseCode = response.body().string();
                        Log.e(TAG, "responseCode MSGXX " + responseCode);

//                        {"data":[{"msg":"Card added successfully."}],"status":"1"}
                        // new ShowMsg().createSnackbar(AddCardInformation.this, "" + responseCode);

                        try {
                            JSONObject jsonObject = new JSONObject(responseCode);
                            String status = jsonObject.getString("status");

                            if (status.equals("1")) {
                                JSONArray array = jsonObject.getJSONArray("data");
                                if(array.length() > 0){
                                    JSONObject jsonObject1 = array.getJSONObject(0);
                                    String msg = jsonObject1.getString("msg");
                                    Toast.makeText(getActivity() , ""+msg, Toast.LENGTH_SHORT).show();

                                    editTextCardName.setText("");
                                    editTextCardNumber.setText("");
                                    editTextExpiryDate.setText("");
                                    editTextCVV.setText("");
                                    editTextPostalCode.setText("");


                                    CreditCardListRequest();

                                }
                            } else {
                                JSONArray array = jsonObject.getJSONArray("data");
                                if(array.length() > 0){
                                    JSONObject jsonObject1 = array.getJSONObject(0);
                                    String msg = jsonObject1.getString("msg");

                                    if(msg.equalsIgnoreCase("Service not allowed") || msg.equalsIgnoreCase("Bad card check digit")){
                                        Toast.makeText(getActivity() , "Invalid Card", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getActivity() , ""+msg, Toast.LENGTH_SHORT).show();

                                    }




                                }
                            }

                        } catch (Exception ex) {
                            Log.e(TAG, "Exception MSG " + ex.getMessage());
                            //  new ShowMsg().createSnackbar(AddCardInformation.this, "" + ex.getMessage());
                        }
                    }else{
                        //new ShowMsg().createSnackbar(AddCardInformation.this,  getString(R.string.something_went_wrong));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //new ShowMsg().createSnackbar(AddCardInformation.this, "" + ex.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                //new ShowMsg().createSnackbar(AddCardInformation.this, "" + t.getMessage());
            }
        });
    }







    private void initialize(View v) {
        addCardButton = (Button) v.findViewById(R.id.add_card);
        // cardContainer = (LinearLayout) v.findViewById(R.id.card_container);
        scrol = (ScrollView) v.findViewById(R.id.scroll);

        // cardContainer.setVisibility(View.VISIBLE);
        //addCardButton.setVisibility(View.VISIBLE);

       /* Card card = new Card("4242424242424242", 12, 2023, "123");

        if (!card.validateCard())
        {
            // Do not continue token creation.
        }
        else
        {
            avi.show();
            Stripe stripe = new Stripe(mContext, "pk_test_ZbLShxvI48s4eV5Q3759W8vx");
            stripe.createToken(
                    card,
                    new TokenCallback()
                    {
                        public void onSuccess(Token token)
                        {
                            avi.hide();
                            Toast.makeText(mContext,token.toString(),Toast.LENGTH_LONG).show();
                            Log.e("strip12345",String.valueOf(token));
                        }
                        public void onError(Exception error)
                        {
                            avi.hide();
                            // Show localized error message
                            Toast.makeText(mContext,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                            Log.e("strip12345",error.getLocalizedMessage());
                        }
                    }
            );
        }*/

//        getSupportActionBar().setTitle("Cards");
        // populate();
    }

    private void populate() {
        CreditCardView sampleCreditCardView = new CreditCardView(getContext());

        String name = "Glarence Zhao";
        String cvv = "420";
        String expiry = "01/18";
        String cardNumber = "4242424242424242";

        sampleCreditCardView.setCVV(cvv);
        sampleCreditCardView.setCardHolderName(name);
        sampleCreditCardView.setCardExpiry(expiry);
        sampleCreditCardView.setCardNumber(cardNumber);

      // sampleCreditCardView.setEnabled(true);

        // cardContainer.addView(sampleCreditCardView);
        // int index = cardContainer.getChildCount() - 1;
        // addCardListener(index, sampleCreditCardView);
    }

    private void listeners() {
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddCard.class);
                startActivityForResult(intent, CREATE_NEW_CARD);



//                Intent intent = new Intent(getActivity(), CardEditActivity.class);
//                startActivityForResult(intent, CREATE_NEW_CARD);




//                CCConsumerCardInfo mCCConsumerCardInfo = new CCConsumerCardInfo();
//                //mCCConsumerCardInfo.setMaskCharacter(name);
//              //  mCCConsumerCardInfo.se
//                mCCConsumerCardInfo.setCardNumber("5178058269050897");
//                mCCConsumerCardInfo.setExpirationDate("01/22");
//                mCCConsumerCardInfo.setCvv("291");
//                //mCCConsumerCardInfo.setCCConsumerMaskFormat(CCConsumerMaskFormat.CARD_MASK_LAST_FOUR);
//
//
//                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setMessage("Loading...");
//                progressDialog.show();
//
//
//
////
//                MyApplication.getConsumerApi().generateAccountForCard(mCCConsumerCardInfo, new CCConsumerTokenCallback() {
//                    @Override
//                    public void onCCConsumerTokenResponseError(CCConsumerError error) {
//                        progressDialog.dismiss();
//                        showErrorDialog(error.getResponseMessage());
//                    }
//
//                    @Override
//                    public void onCCConsumerTokenResponse(CCConsumerAccount consumerAccount) {
//                        progressDialog.dismiss();
//
//                        Log.e(TAG , "consumerAccount1 "+consumerAccount.getToken());
//
//                        addCreditCardRequest(consumerAccount.getToken(), "01/22");
//                    }
//                });



            }
        });
    }



    private void addCardListener(final int index, CreditCardView creditCardView) {
        creditCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditCardView creditCardView = (CreditCardView) v;
                String cardNumber = creditCardView.getCardNumber();
                String expiry = creditCardView.getExpiry();
                String cardHolderName = creditCardView.getCardHolderName();
                String cvv = creditCardView.getCVV();

                Intent intent = new Intent(getActivity(), CardEditActivity.class);
                intent.putExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME, cardHolderName);
                intent.putExtra(CreditCardUtils.EXTRA_CARD_NUMBER, cardNumber);
                intent.putExtra(CreditCardUtils.EXTRA_CARD_EXPIRY, expiry);
                intent.putExtra(CreditCardUtils.EXTRA_CARD_SHOW_CARD_SIDE, CreditCardUtils.CARD_SIDE_BACK);
                intent.putExtra(CreditCardUtils.EXTRA_VALIDATE_EXPIRY_DATE, false);

                // start at the CVV activity to edit it as it is not being passed
                intent.putExtra(CreditCardUtils.EXTRA_ENTRY_START_PAGE, CreditCardUtils.CARD_CVV_PAGE);
                startActivityForResult(intent, index);
            }
        });
    }



    protected void showErrorDialog(@NonNull String errorMessage) {
        new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.error)).setMessage(errorMessage)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }


    public void onActivityResult(int reqCode, int resultCode, Intent data) {

        if (reqCode == CREATE_NEW_CARD) {
            CreditCardListRequest();
        }

        if (resultCode == RESULT_OK) {
//            Debug.printToast("Result Code is OK", getApplicationContext());

            Log.e(TAG, "onActivityResult111");

            String name = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
            final String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
            String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);

            Log.e(TAG , "expiryD "+expiry);

            if (reqCode == CREATE_NEW_CARD) {

                Log.e(TAG, "onActivityResult222");





//                CCConsumerCardInfo mCCConsumerCardInfo = new CCConsumerCardInfo();
//                //mCCConsumerCardInfo.setMaskCharacter(name);
//                mCCConsumerCardInfo.setCardNumber(cardNumber);
//                mCCConsumerCardInfo.setExpirationDate(expiry);
//                mCCConsumerCardInfo.setCvv(cvv);
//
//                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setMessage("Loading...");
//                progressDialog.show();
//
//                MyApplication.getConsumerApi().generateAccountForCard(mCCConsumerCardInfo, new CCConsumerTokenCallback() {
//                    @Override
//                    public void onCCConsumerTokenResponseError(CCConsumerError error) {
//                        progressDialog.dismiss();
//                        showErrorDialog(error.getResponseMessage());
//                    }
//
//                    @Override
//                    public void onCCConsumerTokenResponse(CCConsumerAccount consumerAccount) {
//                       progressDialog.dismiss();
//                        addCreditCardRequest(consumerAccount.getToken() ,expiry);
//                    }
//                });


//                CreditCardView creditCardView = new CreditCardView(mContext);
//
//                creditCardView.setCVV(cvv);
//                creditCardView.setCardHolderName(name);
//                creditCardView.setCardExpiry(expiry);
//                creditCardView.setCardNumber(cardNumber);
//
//
//                String[] exp = expiry.split("/");
//
//                Card card = new Card(cardNumber, Integer.valueOf(exp[0]), Integer.valueOf(exp[1]), cvv);
//
//                if (!card.validateCard()) {
//                    Log.e(TAG, "onActivityResult444");
//
//                    Toast.makeText(mContext, "invalid credit card.", Toast.LENGTH_LONG).show();
//
//                    // Do not continue token creation.
//                } else {
//                    Log.e(TAG, "onActivityResult555");
//                    avi.show();
//                    Stripe stripe = new Stripe(mContext, STRIPE_KEY);
//                    stripe.createToken(card,
//                            new TokenCallback() {
//                                public void onSuccess(Token token) {
//                                    avi.hide();
//                                    // Toast.makeText(mContext,token.toString(),Toast.LENGTH_LONG).show();
//                                    Log.e("strip12345", String.valueOf(token));
//                                    Log.e("cid", token.getId());
//                                    Log.e("type", token.getType());
//                                    Log.e("card", token.getCard().toString());
//                                    Log.e("last 4", token.getCard().getLast4());
//
//                                    addCreditCardRequest(token.getId().toString());
//                                }
//
//                                public void onError(Exception error) {
//                                    avi.hide();
//                                    // Show localized error message
//                                    Toast.makeText(mContext, "invalid credit card.", Toast.LENGTH_LONG).show();
//                                    Log.e("strip123456789", error.getLocalizedMessage());
//                                }
//                            }
//                    );
//                }
            } else {
                Log.e(TAG, "onActivityResult333");
        }
        }
    }

    public void CreditCardListRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mContext);
        String id = pref1.getUserId();

        new Utils(mContext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_CREDIT_CARDS_LIST1);

        if (Utils.isNetworkAvailable(Utils.context)) {
            JsonPost.getNetworkResponse(Utils.context, null, Utils.global.mapMain, ConstVariable.CreditCardsList);
        } else {
            Utils.showInternetErrorMessage(Utils.context);
        }
    }

    public void addCreditCardRequest(String mtoken, String mmyy) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mContext);
        String id = pref1.getUserId();

        new Utils(mContext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("Token", mtoken);
        Utils.global.mapMain.put("exp", mmyy);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_CC_ADD_CREDIT_CARD);

        if (Utils.isNetworkAvailable(Utils.context)) {
            JsonPost.getNetworkResponse(Utils.context, null, Utils.global.mapMain, ConstVariable.AddCreditCard);
        } else {
            Utils.showInternetErrorMessage(Utils.context);
        }
    }



    public void loadRequestCC_ADD_CREDIT_CARD(){

    }


    public static void hideLoader() {
        avi.hide();
    }

    public void loadRequestsLis(Context context, List<HashMap<String, Object>> viewList, String mode) {
        // cardContainer.setVisibility(View.VISIBLE);
        // cardContainer.removeAllViews();

        HashMap<String, Object> map;

        for (int i = 0; i < viewList.size(); i++) {
            map = new HashMap<>();

            map = viewList.get(i);
            CreditCardView sampleCreditCardView = new CreditCardView(mContext);

            String exp = map.get("expirymonth").toString() + "/" + map.get("expiryyear").toString();

            String name = map.get("nameoncard").toString();
            String cvv = map.get("cvv").toString();
            String expiry = exp;
            String cardNumber = map.get("cardno").toString();

            sampleCreditCardView.setCVV(cvv);
            sampleCreditCardView.setCardHolderName(name);
            sampleCreditCardView.setCardExpiry(expiry);
            sampleCreditCardView.setCardNumber(cardNumber);

            // cardContainer.addView(sampleCreditCardView);
            //  int index = cardContainer.getChildCount() - 1;
            // addCardListener(index, sampleCreditCardView);
        }
        scrol.fullScroll(ScrollView.FOCUS_UP);
    }

    public static void loadRequestsList(Context context, List<HashMap<String, Object>> viewList, String mode) {
        // Utils.e(TAG+"81", "load browseMembersList Data"+mode+viewList);
        if (viewList != null && viewList.size() > 0) {
            try {
                //Utils.e(TAG+"88", "browseMembersList new");
                requestsList = new ArrayList<modelItem>();

                //Utils.e(TAG+"93", "browseMembersList new "+eventList);

                for (int i = 0; i < viewList.size(); i++) {
                    HashMap<String, Object> mp = new HashMap<String, Object>();
                    mp = viewList.get(i);

                    if (!requestsList.contains(mp)) {
                        requestsList.add(new modelItem(mp));
                    }
                }
                //Utils.e(TAG+"118", "browseMembersList"+eventList.size());
            } catch (Exception e) {
                //Utils.e(TAG+"122","Exception======================Exception======================Exception");
                e.printStackTrace();
            } finally {
                // Utils.e(TAG+"127", "browseMembersList"+eventList.size());
                //Utils.e(TAG+"128", "ok");
                if (!mode.equalsIgnoreCase("update")) {
                    setAdapterFriendsRequestList(context);
                } else {
                    requestsAdapter.notifyItemInserted(requestsList.size());
                    requestsAdapter.notifyDataSetChanged();
                }
            }
            requestsListMan.setAutoMeasureEnabled(true);
        } else {
            rv_loc.setVisibility(View.GONE);
            //noData.setVisibility(View.VISIBLE);
        }
    }

    public static void setAdapterFriendsRequestList(final Context context) {
        if (rv_loc != null) {
            rv_loc.setVisibility(View.VISIBLE);
            //noData.setVisibility(View.GONE);
        }

        //Utils.e(TAG+"156", "setAdapter ok "+eventList);
        requestsAdapter = new CardsListAdapter(mContext, requestsList, rv_loc, R.layout.card_rowitem, ConstVariable.CurrentRides);
        //set the adapter object to the Recyclerview
        //Utils.e(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_loc.setAdapter(requestsAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_card:
                Intent intent = new Intent(mContext, CardEditActivity.class);
                startActivityForResult(intent, CREATE_NEW_CARD);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void showDeleteDilog(String dlgText, final HashMap<String, Object> data) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog5, null);
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mContext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        // final RelativeLayout rootlo = (RelativeLayout) dialog.findViewById(R.id.rootlo);
        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView textView = (TextView) dialog.findViewById(R.id.desc);
        final Button attend = (Button) dialog.findViewById(R.id.attend);
        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        textView.setText(dlgText);
        title.setText(R.string.app_name);

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    dialog.cancel();
                    deleteCardRequest(data);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

        dialogLayout.post(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.ZoomIn).duration(500).playOn(dialogLayout);
            }
        });
    }

    public void deleteCardRequest(HashMap<String, Object> data) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mContext);
        String id = pref1.getUserId();

        new Utils(mContext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("card_id", data.get("acctid").toString());
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_DELETE_CARD);

        if (Utils.isNetworkAvailable(Utils.context)) {
            JsonPost.getNetworkResponse(Utils.context, null, Utils.global.mapMain, ConstVariable.DeleteCard);
        } else {
            Utils.showInternetErrorMessage(Utils.context);
        }


       // remove-card.php?userid=166&card_id=2
    }
}

