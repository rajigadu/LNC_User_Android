package com.latenightchauffeurs.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.APIClient;
import com.latenightchauffeurs.Utils.APIInterface;
import com.latenightchauffeurs.model.SavePref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCard extends AppCompatActivity {

    SavePref pref1 = new SavePref();


    TextView textViewTitleName;

    public Call<ResponseBody> call = null;
    public APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);


    private static final String TAG = "AddCard" ;
    EditText editTextCardName, editTextCardNumber, editTextExpiryDate, editTextCVV, editTextPostalCode;
    Button buttonSubmit;

    Bundle bundle = null;

    String idDD = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_card);

        textViewTitleName = (TextView) findViewById(R.id.title);
        textViewTitleName.setText("Add Card");

        editTextCardName = (EditText) findViewById(R.id.card_name);
        editTextCardNumber = (EditText) findViewById(R.id.card_number);
        editTextExpiryDate = (EditText) findViewById(R.id.button678789789);
        editTextCVV = (EditText) findViewById(R.id.button675768);
        editTextPostalCode = (EditText) findViewById(R.id.button423435);

        buttonSubmit = (Button) findViewById(R.id.submit);


        ImageView imageViewBack = (ImageView) findViewById(R.id.back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        pref1.SavePref(AddCard.this);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);


        bundle = getIntent().getExtras();

        if(bundle != null){
            String key = bundle.getString("key");
            idDD = key;
        }else{
            idDD = pref1.getUserId();
        }


        Log.e(TAG , "buttonSubmitIDD "+idDD);

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

                        Toast.makeText(AddCard.this , "All field is required!", Toast.LENGTH_SHORT).show();
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



    }



    private void callRefreshMethod(String cardName, String cardNumber, String expiry, String cardCVV, String postal) {
        final ProgressDialog progressDialog = new ProgressDialog(AddCard.this);
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
// String id = pref1.getUserId();
        MultipartBody.Part uid = MultipartBody.Part.createFormData("userid", idDD);
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
                                    Toast.makeText(AddCard.this , ""+msg, Toast.LENGTH_SHORT).show();

                                    finish();
                                }
                            } else {
                                JSONArray array = jsonObject.getJSONArray("data");
                                if(array.length() > 0){
                                    JSONObject jsonObject1 = array.getJSONObject(0);
                                    String msg = jsonObject1.getString("msg");

                                    if(msg.equalsIgnoreCase("Service not allowed") || msg.equalsIgnoreCase("Bad card check digit")){
                                        Toast.makeText(AddCard.this , "Invalid Card", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(AddCard.this , ""+msg, Toast.LENGTH_SHORT).show();

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


}
