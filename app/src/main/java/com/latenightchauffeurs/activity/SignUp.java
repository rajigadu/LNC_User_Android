package com.latenightchauffeurs.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.creditcarddesign.CardEditActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.cooltechworks.creditcarddesign.CreditCardView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.Settings;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.adapter.PlaceArrayAdapter;
import com.latenightchauffeurs.model.SavePref;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUp extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks  {
    private static final String TAG = "SignUp";
    public String type = "";

    @BindView(R.id.fname)
    EditText fname;

    @BindView(R.id.lname)
    EditText lname;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.mobile)
    EditText mobile;

//    @BindView(R.id.address)
//    EditText address;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.cpassword)
    EditText cpassword;

    @BindView(R.id.signup)
    Button signup;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    AutoCompleteTextView autoCompleteTextViewAddress;

    boolean pickupBoolean = false;
    private PlaceArrayAdapter mPlaceArrayAdapter_drop;
    private static final int GOOGLE_API_CLIENT_ID_DROP = 116;
    private final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new
            LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private GoogleApiClient mGoogleApiClient;


   /* @BindView(R.id.card_cc)
    EditText card_cc;*/

    String address_home = "";
    // String tokenCard="";

    HashMap<String, Object> map;

    private final int CREATE_NEW_CARD = 0;

    //public static String STRIPE_KEY="pk_live_OYvx7pJBADWieTMlKcSohH8t";

    public static String strip_key = "pk_test_FGO6NmzgfSidisSjcV5hJVF6";


    private static AVLoadingIndicatorView avi;

    public static List<String> areas;
    public static List<String> aids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_signup);
        } catch (Exception e) {
            Log.e("error123", e.getMessage());
        }

        ButterKnife.bind(this);
        signup.setOnClickListener(this);
        back.setOnClickListener(this);
        //address.setOnClickListener(this);

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator("BallPulseIndicator");
        avi.hide();


        autoCompleteTextViewAddress = (AutoCompleteTextView) findViewById(R.id.address);



        mGoogleApiClient = new GoogleApiClient.Builder(SignUp.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID_DROP, this)
                .addConnectionCallbacks(this)
                .build();

        autoCompleteTextViewAddress.setThreshold(3);

        mPlaceArrayAdapter_drop = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        autoCompleteTextViewAddress.setAdapter(mPlaceArrayAdapter_drop);


        autoCompleteTextViewAddress.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable co = ((TextView) v).getCompoundDrawables()[2];
                    if (event.getX() > v.getMeasuredWidth() - v.getPaddingRight() - co.getIntrinsicWidth()) {
                        Log.d(TAG, "right");
                        autoCompleteTextViewAddress.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
        autoCompleteTextViewAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // String description = (String) parent.getItemAtPosition(position);
//                Log.e(TAG , "description:: "+description);
//                autoCompleteTextViewAddress.setText(description);
//                Location latLng = getLocationFromAddress11(getActivity() ,description);
//                Log.e(TAG , "latLng:: "+latLng);
//                if(latLng != null){
//                    locationAA = latLng;
//                    setLocationMethod(locationAA, latLngDropOFF, 1);
//                }

                pickupBoolean = true;
            }
        });


        autoCompleteTextViewAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() != 0 && isdcursor == true) {
//                    close22.setVisibility(View.VISIBLE);
//                } else {
//                    close22.setVisibility(View.GONE);
//                }

                pickupBoolean = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private static final int REQUEST_CODE_AUTOCOMPLETE = 10;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:
                submit();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.card_cc:
                pickCard();
                break;
            case R.id.address:
                openAutocompleteActivity();
                break;
        }
    }


    private void openAutocompleteActivity() {
        try {
            //The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e("hjhjkj988989", message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }


    public void pickCard() {
        Intent intent = new Intent(SignUp.this, CardEditActivity.class);
        startActivityForResult(intent, CREATE_NEW_CARD);
    }

    public void submit() {
        // Utils.startActivity(ActivityLogin.this,ActivityEvents.class);
        new Utils(SignUp.this);

        if (fname.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter first name.", SignUp.this);
        } else if (lname.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter last name.", SignUp.this);
        } else if (email.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter email address.", SignUp.this);
        } else if (!Utils.isValidEmail(email.getText().toString().trim())) {
            Utils.toastTxt("Please enter valid email address.", SignUp.this);
        } else if (mobile.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter mobile number.", SignUp.this);
        } else if (mobile.getText().toString().trim().length() < 10) {
            Utils.toastTxt("Please enter 10 digit mobile number.", SignUp.this);
        }  else if (autoCompleteTextViewAddress.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter home location.", SignUp.this);
        } else if (password.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter password.", SignUp.this);
        } else if (cpassword.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter conform password.", SignUp.this);
        } else if (!cpassword.getText().toString().trim().equalsIgnoreCase(password.getText().toString().trim())) {
            Utils.toastTxt("Password and confirm password must be same.", SignUp.this);
        } else {
            map = new HashMap<>();
            map.put(ConstVariable.FULLNAME, fname.getText().toString());
            map.put(ConstVariable.LASTNAME, lname.getText().toString());
            map.put(ConstVariable.EMAIL, email.getText().toString());
            map.put(ConstVariable.MOBILE, mobile.getText().toString());
            map.put(ConstVariable.ADDRESS, autoCompleteTextViewAddress.getText().toString());
            map.put(ConstVariable.PASSWORD, password.getText().toString());
            // map.put("card_token",tokenCard);
            OnlineRequest.signupRequest(SignUp.this, map);
        }
    }

    public static void populatecities(List<HashMap<String, Object>> list) {
        aids = new ArrayList<>();
        areas = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            aids.add(list.get(i).get("id").toString());
            areas.add(list.get(i).get("location").toString());
        }
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
//            Debug.printToast("Result Code is OK", getApplicationContext());

            String name = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
            String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
            String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);

            if (reqCode == CREATE_NEW_CARD) {
                CreditCardView creditCardView = new CreditCardView(SignUp.this);

                creditCardView.setCVV(cvv);
                creditCardView.setCardHolderName(name);
                creditCardView.setCardExpiry(expiry);
                creditCardView.setCardNumber(cardNumber);

                // cardContainer.addView(creditCardView);
                //int index = cardContainer.getChildCount() - 1;
                // AddCardListener(0, creditCardView);

                // Card card = new Card("4242424242424242", 12, 2023, "123");
                // Card card = new Card("4000056655665556", 12, 2023, "123");

                String[] exp = expiry.split("/");
                Card card = new Card(cardNumber, Integer.valueOf(exp[0]), Integer.valueOf(exp[1]), cvv);

                if (!card.validateCard()) {
                    Log.e("cid====----------======", "wrong etails");
                    Toast.makeText(SignUp.this, "Wrong details..", Toast.LENGTH_LONG).show();
                } else {
                    avi.show();
                    Stripe stripe = new Stripe(SignUp.this, strip_key);
                    stripe.createToken(
                            card,
                            new TokenCallback() {
                                public void onSuccess(Token token) {
                                    avi.hide();
                                    // Toast.makeText(mContext,token.toString(),Toast.LENGTH_LONG).show();
                                    // Log.e("strip12345",String.valueOf(token));
                                    Log.e("cid", token.getId());
                                    Log.e("type", token.getType());
                                    Log.e("card", token.getCard().toString());
                                    Log.e("last 4", token.getCard().getLast4());

                                    Toast.makeText(SignUp.this, "Card added successfull.", Toast.LENGTH_LONG).show();
                                    //tokenCard=token.getId().toString();
                                    // card_cc.setText("XXXXXXXXXXXX"+token.getCard().getLast4().toString());
                                }

                                public void onError(Exception error) {
                                    avi.hide();
                                    // Show localized error message
                                    Toast.makeText(SignUp.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    Log.e("strip123456789", error.getLocalizedMessage());

                                    /// tokenCard="";
                                    // card_cc.setText("");
                                }
                            }
                    );
                }
                //avi.show();
            } else {
                // CreditCardView creditCardView = (CreditCardView) cardContainer.getChildAt(reqCode);

                //creditCardView.setCardExpiry(expiry);
                // creditCardView.setCardNumber(cardNumber);
                // creditCardView.setCardHolderName(name);
            }

            switch (reqCode) {
                case REQUEST_CODE_AUTOCOMPLETE:
                    // Get the user's selected place from the Intent.
                    Place place = PlaceAutocomplete.getPlace(this, data);

                    Log.e("one=========", "Place Selected: " + place.getName());
                    Log.e("two=========", "Place Selected: " + place.getAddress());

                    //Log.e("two=========", "Place Selected: " + place.getAddress());

                    address_home = place.getName().toString() + "," + place.getAddress().toString();
                    //address.setText(address_home);


            }

        }

    }


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            //getAddress

//            mNameTextView.setText(Html.fromHtml(place.getName() + ""));
//            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
//            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
//            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
//            mWebTextView.setText(place.getWebsiteUri() + "");
//            if (attributions != null) {
//                mAttTextView.setText(Html.fromHtml(attributions.toString()));
//            }
        }
    };


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter_drop.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        //mPlaceArrayAdapter_drop.setGoogleApiClient(null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
