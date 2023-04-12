package com.latenightchauffeurs.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cardconnect.consumersdk.CCConsumerTokenCallback;
import com.cardconnect.consumersdk.domain.CCConsumerAccount;
import com.cardconnect.consumersdk.domain.CCConsumerCardInfo;
import com.cardconnect.consumersdk.domain.CCConsumerError;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.APIClient;
import com.latenightchauffeurs.Utils.APIInterface;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.MyApplication;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.adapter.CardsList2Adapter;
import com.latenightchauffeurs.model.SavePref;
import com.latenightchauffeurs.model.modelItem;
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

public class CardsList extends AppCompatActivity {
    private static final String TAG = "CardsList";
    private Unbinder unbinder;
    static RecyclerView rv_loc;
    public static CardsList Instance;
    private final int CREATE_NEW_CARD = 0;
    public static Context mcontext;
    public static CardsList2Adapter requestsAdapter;
    public static RecyclerView.LayoutManager requestsListMan;
    public static List<modelItem> requestsList;
    public HashMap<String, Object> map;
    public List<HashMap<String, Object>> list = new ArrayList<>();
    private static AVLoadingIndicatorView avi;

    @BindView(R.id.add_card)
    Button addCardButton;

    ImageView back;
    TextView title;




    public Call<ResponseBody> call = null;
    public APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);

    RelativeLayout relativeLayoutCardContainer;

    Switch aSwitch;


    EditText editTextCardName, editTextCardNumber, editTextExpiryDate, editTextCVV, editTextPostalCode;
    Button buttonSubmit;


    //test
    // public static String STRIPE_KEY="pk_test_FGO6NmzgfSidisSjcV5hJVF6";

    //live
    public static final String STRIPE_KEY = "pk_live_OYvx7pJBADWieTMlKcSohH8t";

    //public static String STRIPE_KEY="pk_test_Z2JL4YmwR8tAMn5ctCFMhh8A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardslist);


        TextView textViewTitleName = (TextView) findViewById(R.id.title);
        textViewTitleName.setText("Card List");



        aSwitch = (Switch) findViewById(R.id.addcard_top);
        relativeLayoutCardContainer = (RelativeLayout) findViewById(R.id.m_reative);

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




        editTextCardName = (EditText) findViewById(R.id.card_name);
        editTextCardNumber = (EditText) findViewById(R.id.card_number);
        editTextExpiryDate = (EditText) findViewById(R.id.button678789789);
        editTextCVV = (EditText) findViewById(R.id.button675768);
        editTextPostalCode = (EditText) findViewById(R.id.button423435);

        buttonSubmit = (Button) findViewById(R.id.submit);




        unbinder = ButterKnife.bind(this);

        rv_loc = (RecyclerView) findViewById(R.id.rv_loc);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        mcontext = CardsList.this;
        Instance = CardsList.this;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Utils.global.locationIdentity=1;

        requestsListMan = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
        rv_loc.setHasFixedSize(true);
        rv_loc.setLayoutManager(requestsListMan);

        MyrequestsListRequest();

        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CardsList.this, CardEditActivity.class);
//
//                intent.putExtra(CreditCardUtils.EXTRA_CARD_NUMBER, "");
//                startActivityForResult(intent, CREATE_NEW_CARD);

                Intent intent = new Intent(CardsList.this, AddCard.class);
                startActivityForResult(intent, CREATE_NEW_CARD);
            }
        });






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

                    Toast.makeText(CardsList.this , "All field is required!", Toast.LENGTH_SHORT).show();
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

             //   Log.d(TAG, "stringfff " + editTextExpiryDate.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });






        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator("BallPulseIndicator");
        avi.hide();
    }











    private void callRefreshMethod(String cardName, String cardNumber, String expiry, String cardCVV, String postal) {
        final ProgressDialog progressDialog = new ProgressDialog(CardsList.this);
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
        pref1.SavePref(CardsList.this);
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
                                    Toast.makeText(CardsList.this , ""+msg, Toast.LENGTH_SHORT).show();


                                    editTextCardName.setText("");
                                            editTextCardNumber.setText("");
                                            editTextExpiryDate.setText("");
                                            editTextCVV.setText("");
                                            editTextPostalCode.setText("");



                                    MyrequestsListRequest();

                                }
                            } else {
                                JSONArray array = jsonObject.getJSONArray("data");
                                if(array.length() > 0){
                                    JSONObject jsonObject1 = array.getJSONObject(0);
                                    String msg = jsonObject1.getString("msg");

                                    if(msg.equalsIgnoreCase("Service not allowed") || msg.equalsIgnoreCase("Bad card check digit")){
                                        Toast.makeText(CardsList.this , "Invalid Card", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(CardsList.this , ""+msg, Toast.LENGTH_SHORT).show();

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








    public static void MyrequestsListRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_CREDIT_CARDS_LIST1);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain,
                    ConstVariable.CreditCardsList1);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
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
        requestsAdapter = new CardsList2Adapter(mcontext, requestsList, rv_loc, R.layout.card_rowitem, ConstVariable.Login);
        //set the adapter object to the Recyclerview
        //Utils.e(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_loc.setAdapter(requestsAdapter);
    }

    @Override
    public void onBackPressed() {
        //finishAffinity();
        super.onBackPressed();
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {

        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == CREATE_NEW_CARD) {
            MyrequestsListRequest();
        }


        if (resultCode == RESULT_OK) {
//            Debug.printToast("Result Code is OK", getApplicationContext());

            String name = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
            final String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
            String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);

            if (reqCode == CREATE_NEW_CARD) {
//                CreditCardView creditCardView = new CreditCardView(mcontext);
//
//                creditCardView.setCVV(cvv);
//                creditCardView.setCardHolderName(name);
//                creditCardView.setCardExpiry(expiry);
//                creditCardView.setCardNumber(cardNumber);
//
//                // cardContainer.addView(creditCardView);
//                //int index = cardContainer.getChildCount() - 1;
//                // addCardListener(0, creditCardView);
//
//                // Card card = new Card("4242424242424242", 12, 2023, "123");
//                // Card card = new Card("4000056655665556", 12, 2023, "123");
//
//                String[] exp = expiry.split("/");
//                Card card = new Card(cardNumber, Integer.valueOf(exp[0]), Integer.valueOf(exp[1]), cvv);
//
//                if (!card.validateCard()) {
//                    // Do not continue token creation.
//                } else {
//                    avi.show();
//                    Stripe stripe = new Stripe(mcontext, STRIPE_KEY);
//                    stripe.createToken(card,
//                            new TokenCallback() {
//                                public void onSuccess(Token token) {
//                                    avi.hide();
//                                    // Toast.makeText(mContext,token.toString(),Toast.LENGTH_LONG).show();
//                                    // Log.e("strip12345",String.valueOf(token));
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
//                                    Toast.makeText(CardsList.this, "invalid credit card.", Toast.LENGTH_LONG).show();
//                                    Log.e("strip123456789", error.getLocalizedMessage());
//                                }
//                            }
//                    );
//                }
//

                CCConsumerCardInfo mCCConsumerCardInfo = new CCConsumerCardInfo();
                //mCCConsumerCardInfo.setMaskCharacter(name);
                mCCConsumerCardInfo.setCardNumber(cardNumber);
                mCCConsumerCardInfo.setExpirationDate(expiry);
                mCCConsumerCardInfo.setCvv(cvv);

                final ProgressDialog progressDialog = new ProgressDialog(CardsList.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                MyApplication.getConsumerApi().generateAccountForCard(mCCConsumerCardInfo, new CCConsumerTokenCallback() {
                    @Override
                    public void onCCConsumerTokenResponseError(CCConsumerError error) {
                        progressDialog.dismiss();
                        showErrorDialog(error.getResponseMessage());
                    }

                    @Override
                    public void onCCConsumerTokenResponse(CCConsumerAccount consumerAccount) {
                        progressDialog.dismiss();
                        addCreditCardRequest(consumerAccount.getToken(), expiry);
                    }
                });


            } else {
                // CreditCardView creditCardView = (CreditCardView) cardContainer.getChildAt(reqCode);

                //creditCardView.setCardExpiry(expiry);
                // creditCardView.setCardNumber(cardNumber);
                // creditCardView.setCardHolderName(name);
            }
        }
    }



    protected void showErrorDialog(@NonNull String errorMessage) {
        new AlertDialog.Builder(CardsList.this).setTitle(getString(R.string.error)).setMessage(errorMessage)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }


    public void addCreditCardRequest(String mtoken, String mmyy) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("Token", mtoken);
        Utils.global.mapMain.put("exp", mmyy);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_CC_ADD_CREDIT_CARD);

        if (Utils.isNetworkAvailable(Utils.context)) {
            JsonPost.getNetworkResponse(Utils.context, null,
                    Utils.global.mapMain, ConstVariable.AddCreditCard1);
        } else {
            Utils.showInternetErrorMessage(Utils.context);
        }
    }
}
