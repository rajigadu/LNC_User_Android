package com.latenightchauffeurs.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.ServiceApi;
import com.latenightchauffeurs.Utils.ServiceGenerator;
import com.latenightchauffeurs.Utils.Settings;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.adapter.PlaceArrayAdapter;
import com.latenightchauffeurs.adapter.StopsAddressAdapter;
import com.latenightchauffeurs.fragment.DashBorad;
import com.latenightchauffeurs.model.SavePref;
import com.latenightchauffeurs.model.StopsList;
import com.latenightchauffeurs.model.modelItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.latenightchauffeurs.activity.Navigation.BookNewNowBack;
import static com.latenightchauffeurs.activity.Navigation.Instance;


public class ActivityEditBookingInfo extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "ActivityEditBookingInfo";

    private ImageView ivBack, ivClosePickUp, ivCloseDropLoc;
    private EditText etBookingType, etNote, etYearTitle, etMonth, etYear;
    private AutoCompleteTextView autoPickupLoc, autoDropLoc;
    private Button btnNStops, btnChoose, btnChooseCard, btnEstimatePrice, btnBookUpdate;
    private static RecyclerView rvStopsLocList;
    private CheckBox car_ManualT;
    private TextView tvCardNumber, tvMonthTitle, tvNoCard;
    private static EditText etDate, etTime;
    private Button btnSubmit;

    LinearLayout llDateTime;

    private Context mcontext;
    private HashMap<String, Object> detailMap;
    private String bookingTypeStatus = "", rideId = "", pickUpAddress = "", dropAddress = "",
            pickUpLat = "", pickUpLang = "", dropLat = "", dropLang = "", notesLoc = "";

    private boolean isBookingType = false;
    private boolean isAutoPickUp = false, isAutoDropLoc = false;

    public static StopsAddressAdapter requestsAdapter;
    public List<HashMap<String, Object>> list = new ArrayList<>();
    public static List<modelItem> requestsList;
    private String payment_Id = "";
    private String card_numberId = "";
    public static ActivityEditBookingInfo appCompatActivity;

    private static final int GOOGLE_API_CLIENT_ID_PICK = 10, GOOGLE_API_CLIENT_ID_DROP = 11;
    private PlaceArrayAdapter mPlaceArrayAdapter_pickup, mPlaceArrayAdapter_drop;
    private int isClick = 0;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(37.398160, -122.180831),
            new LatLng(37.430610, -121.972090));

    private HashMap<String, Object> map, dropMap, cardMap;
    private List<HashMap<String, Object>> stopsAddList;
    private String isCarML = "";
    private JSONObject json, jsonStops;
    private String date_ride = "", time_ride = "";
    HashMap<String, Object> stMap;

    private String pickUpCityName = "", dropCityName = "", pickUpText, dropText;
    View viewShow;
    TextView tvAddStopsText;
    Button btnPromoCode;
    RelativeLayout rlChooseCard;
    CardView cardPaymentView;
    private AlertDialog alertDialog = null;
    String promoCode = "";
    LinearLayout llPromo;
    EditText etPromoName;
    Button btnApply;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);
        mcontext = this;
        appCompatActivity = this;
        llPromo = findViewById(R.id.ll_apply_promo);
        etPromoName = findViewById(R.id.et_apply_promo);
        btnApply = findViewById(R.id.btn_apply);
        cardPaymentView = findViewById(R.id.card_payment);
        rlChooseCard = findViewById(R.id.rl_payment);
        btnSubmit = findViewById(R.id.btn_submit);
        viewShow = findViewById(R.id.view_show);
        tvAddStopsText = findViewById(R.id.tv_add_stops);
        btnPromoCode = findViewById(R.id.btn_apply_promo);
        setupView();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDate.getText().toString().trim().isEmpty()) {
                    Utils.toastTxt("Please select date.", mcontext);
                } else if (etTime.getText().toString().trim().isEmpty()) {
                    Utils.toastTxt("Please select time.", mcontext);
                } else if (pickUpLat.trim().isEmpty() && pickUpLang.trim().isEmpty()) {
                    Utils.toastTxt("Please enter correct pickup location.", mcontext);
                } else if (dropLat.trim().isEmpty() && dropLang.trim().isEmpty()) {
                    Utils.toastTxt("Please enter correct drop location.", mcontext);
                } else
                    setWidgetVisibility(true);
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etPromoName.getText().toString().trim().equals("")) {
                    map = new HashMap<>();
                    promoCode = etPromoName.getText().toString();
                    map.put("promo", promoCode);
                    OnlineRequest.applyPromo(mcontext, map);
                    Utils.hideSoftKeyboard(appCompatActivity);
                } else {
                    Utils.toastTxt("Please Enter Promo Code", mcontext);
                }
            }
        });

        if (getIntent().getExtras() != null) {
            detailMap = (HashMap<String, Object>) getIntent().getExtras().getSerializable(Utils.EDIT_RIDE_INFO);
        }
        btnPromoCode.setOnClickListener(this);
        Utils.e("Taggg", detailMap.toString());
        bookingTypeStatus = (String) detailMap.get("booking_type");
        rideId = (String) detailMap.get("id");
        pickUpAddress = (String) detailMap.get("pickup_address");
        dropAddress = (String) detailMap.get("drop_address");
        notesLoc = (String) detailMap.get("notes");

        etNote.setText(notesLoc);
        autoPickupLoc.setText(pickUpAddress, true);
        autoDropLoc.setText(dropAddress, true);

        pickUpLat = (String) detailMap.get("pickup_lat");
        pickUpLang = (String) detailMap.get("pickup_long");
        dropLat = (String) detailMap.get("d_lat");
        dropLang = (String) detailMap.get("d_long");

        isBookingType = bookingTypeStatus.equalsIgnoreCase("1");
        etBookingType.setText("Make Reservation");
        etDate.setVisibility(isBookingType ? View.GONE : View.VISIBLE);
        etTime.setVisibility(isBookingType ? View.GONE : View.VISIBLE);

        etDate.setText((String) detailMap.get("otherdate"));
        etTime.setText((String) detailMap.get("time"));

        pickUpCityName = (String) detailMap.get("city_pickup");
        dropCityName = (String) detailMap.get("city_drop");


//        if (cardMap.containsKey("card_id") && !cardMap.get("card_id").toString().
//                equalsIgnoreCase("")) {
//            tvCardNumber.setText("" + cardMap.get("card_id").toString().replaceAll("\\w(?=\\w{4})", "*"));
//            payment_Id = cardMap.get("card_id").toString();
//            card_numberId = cardMap.get("acctid").toString();
//        }
//
//        String expiryTotal = "";
//        if(cardMap.get("expiry").toString().length() >= 2){
//            String mm = cardMap.get("expiry").toString().substring(0, 2);
//            String yy = cardMap.get("expiry").toString().substring(Math.max(cardMap.get("expiry").toString().length() - 2, 0));
//            etMonth.setText(mm);
//            etYear.setText(yy);
//        }


        autoPickupLoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && isAutoPickUp == true) {
                    ivClosePickUp.setVisibility(View.VISIBLE);
                } else {
                    ivCloseDropLoc.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        autoDropLoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && isAutoDropLoc == true) {
                    ivCloseDropLoc.setVisibility(View.VISIBLE);
                } else {
                    ivCloseDropLoc.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Utils.global.mGoogleApiClient_pickup = new GoogleApiClient.Builder(mcontext)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(appCompatActivity, GOOGLE_API_CLIENT_ID_PICK, this)
                .addConnectionCallbacks(this)
                .build();

        Utils.global.mGoogleApiClient_drop = new GoogleApiClient.Builder(mcontext)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(appCompatActivity, GOOGLE_API_CLIENT_ID_DROP, this)
                .addConnectionCallbacks(this)
                .build();

        autoPickupLoc.setThreshold(3);
        autoPickupLoc.setOnItemClickListener(mAutocompleteClickListener_pick);
        mPlaceArrayAdapter_pickup = new PlaceArrayAdapter(mcontext, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        autoPickupLoc.setAdapter(mPlaceArrayAdapter_pickup);

        autoDropLoc.setThreshold(3);
        autoDropLoc.setOnItemClickListener(mAutocompleteClickListener_drop);
        mPlaceArrayAdapter_drop = new PlaceArrayAdapter(mcontext, android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        autoDropLoc.setAdapter(mPlaceArrayAdapter_drop);

        if (car_ManualT.isChecked()) {
            isCarML = "manual";
        } else {
            isCarML = "automatic";
        }
        checkNoOfStopsList();
        setWidgetVisibility(false);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener_drop
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter_drop.getItem(position);
            final String placeId = String.valueOf(item.placeId);

            String[] items = item.toString().split(",");
            String value = items[0];

            autoDropLoc.post(new Runnable() {
                public void run() {
                    autoDropLoc.dismissDropDown();
                }
            });

            isAutoDropLoc = false;
            ivCloseDropLoc.setVisibility(View.GONE);
            autoDropLoc.setText(String.valueOf(item.description));
            autoDropLoc.setCursorVisible(false);
            autoDropLoc.setSelection(autoDropLoc.getText().length());

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(Utils.global.mGoogleApiClient_drop, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_drop);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback_drop
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                //Log.e(LOG_TAG, "Place query did not complete. Error: " +places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            Geocoder geocoder = new Geocoder(mcontext);
            try {
                List<android.location.Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);

                if (addresses != null && addresses.size() > 0) {
                    if (addresses.get(0).getAddressLine(0) != null) {
                    }

                    if (addresses.get(0).getCountryName() != null) {
                        String mcountry = addresses.get(0).getCountryName();
                        //Utils.e("map======="+"105========",country);
                        String cityName = addresses.get(0).getLocality();
                        if (cityName != null && cityName.length() > 0)
                            dropCityName = cityName;
                    }
                } else {
                    new Utils(mcontext);
                    Utils.toastTxt("No location found with these address.", mcontext);
                }
            } catch (IOException e) {
                // bottomSheetAddress.setText("Not Available.");
                e.printStackTrace();
            }

            dropLat = String.valueOf(place.getLatLng().latitude);
            dropLang = String.valueOf(place.getLatLng().longitude);
            Utils.hideSoftKeyboard(ActivityEditBookingInfo.this);
        }
    };

    private void setupView() {
        ivBack = findViewById(R.id.back);
        ivClosePickUp = findViewById(R.id.close11);
        ivCloseDropLoc = findViewById(R.id.close22);

        etBookingType = findViewById(R.id.btype);
        etDate = findViewById(R.id.date);
        etTime = findViewById(R.id.time);
        etNote = findViewById(R.id.note);
        etYearTitle = findViewById(R.id.eyeartitle);
        etMonth = findViewById(R.id.emonth);
        etYear = findViewById(R.id.eyear);
        llDateTime = findViewById(R.id.ll_datetime);

        tvMonthTitle = findViewById(R.id.emonthtitle);
        tvNoCard = findViewById(R.id.nocard);
        tvCardNumber = findViewById(R.id.cnumner);

        btnNStops = findViewById(R.id.nstops);
        btnChooseCard = findViewById(R.id.card_choose);
        btnEstimatePrice = findViewById(R.id.price);
        btnBookUpdate = findViewById(R.id.submit);
        btnChoose = findViewById(R.id.choose);

        car_ManualT = findViewById(R.id.car_ml);
        rvStopsLocList = findViewById(R.id.rv_loc);

        autoPickupLoc = findViewById(R.id.pickup);
        autoDropLoc = findViewById(R.id.drop);

        rvStopsLocList.setHasFixedSize(true);
        rvStopsLocList.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL,
                false));

        btnBookUpdate.setText("Update");

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String cid = pref1.getCardId();
        String acctid = pref1.getAcctid();
        String cnumber = pref1.getCardNumber();
        String cmonth = pref1.getCardExMonth();
        String cyear = pref1.getCardExYear();

        if (cid.equalsIgnoreCase("")) {
            tvNoCard.setVisibility(View.VISIBLE);
            tvCardNumber.setVisibility(View.GONE);
            tvMonthTitle.setVisibility(View.GONE);
            etMonth.setVisibility(View.GONE);
            etYearTitle.setVisibility(View.GONE);
            etYear.setVisibility(View.GONE);
        } else {
            tvNoCard.setVisibility(View.GONE);
            tvCardNumber.setVisibility(View.VISIBLE);
            tvMonthTitle.setVisibility(View.VISIBLE);
            etMonth.setVisibility(View.VISIBLE);
            etYearTitle.setVisibility(View.VISIBLE);
            etYear.setVisibility(View.VISIBLE);
        }

        if (!cnumber.toString().equalsIgnoreCase("")) {
            tvCardNumber.setText(cnumber);
            payment_Id = cid;
            card_numberId = acctid;
        }
        if (!cmonth.toString().equalsIgnoreCase("")) {
            etMonth.setText(cmonth);
        }
        if (!cyear.toString().equalsIgnoreCase("")) {
            etYear.setText(cyear);
        }

        clickListners();
    }

    public void checkVisibility() {
        if (btnSubmit.getVisibility() == View.VISIBLE) {
            BookNewNowBack = true;
            //setWidgetVisibility(false);
        } else {
            BookNewNowBack = false;
            setWidgetVisibility(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.hideSoftKeyboard(this);
        Utils.global.mGoogleApiClient_pickup.stopAutoManage(Instance);
        Utils.global.mGoogleApiClient_pickup.disconnect();

        Utils.global.mGoogleApiClient_drop.stopAutoManage(Instance);
        Utils.global.mGoogleApiClient_drop.disconnect();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Utils.hideSoftKeyboard(this);
        checkVisibility();

        if (BookNewNowBack) {
            super.onBackPressed();
        }
    }

    private void setWidgetVisibility(final boolean isBookVisible) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                etNote.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                autoPickupLoc.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                autoDropLoc.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                llDateTime.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
              //  etBookingType.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                btnSubmit.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                viewShow.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                tvAddStopsText.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                btnNStops.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                rvStopsLocList.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                car_ManualT.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                ivClosePickUp.setVisibility(View.GONE);
                ivCloseDropLoc.setVisibility(View.GONE);

                btnPromoCode.setVisibility(isBookVisible ? View.GONE : View.GONE);
                llPromo.setVisibility(isBookVisible ? View.VISIBLE : View.GONE);
                btnEstimatePrice.setVisibility(isBookVisible ? View.VISIBLE : View.VISIBLE);
                rlChooseCard.setVisibility(isBookVisible ? View.VISIBLE : View.GONE);
                btnBookUpdate.setVisibility(isBookVisible ? View.VISIBLE : View.GONE);
                cardPaymentView.setVisibility(isBookVisible ? View.VISIBLE : View.GONE);
            }
        }, 1 * 200);

    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener_pick
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter_pickup.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            //Log.i(LOG_TAG, "Selected==============================: " + item.description);

            String[] items = item.toString().split(",");
            String value = items[0];

            autoPickupLoc.post(new Runnable() {
                public void run() {
                    autoPickupLoc.dismissDropDown();
                }
            });

            isAutoPickUp = false;
            ivClosePickUp.setVisibility(View.GONE);
            autoPickupLoc.setText(String.valueOf(item.description));

            autoPickupLoc.setCursorVisible(false);
            autoPickupLoc.setSelection(autoPickupLoc.getText().length());

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(Utils.global.mGoogleApiClient_pickup, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_pick);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback_pick
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                //Log.e(LOG_TAG, "Place query did not complete. Error: " +places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            // CharSequence attributions = places.getAttributions();

            Geocoder geocoder = new Geocoder(mcontext);
            try {
                List<android.location.Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);

                if (addresses != null && addresses.size() > 0) {
                    if (addresses.get(0).getAddressLine(0) != null) {
                        //String fulladdress = addresses.get(0).getAddressLine(0).toString();
                        // Log.d("fulladdress", fulladdress);
                        // city.setText(cityname);
                        //pickupLoc.setText(fulladdress);
                    }

                    if (addresses.get(0).getCountryName() != null) {
                        String cityName = addresses.get(0).getLocality();
                        if (cityName != null && cityName.length() > 0)
                            pickUpCityName = cityName;


                       /* if (addresses.get(0).getAddressLine(0) != null) {
                            pickUpText = addresses.get(0).getAddressLine(0);
                            Utils.e(Utils.Tag, " mcountryPick " + " cityName1 " + pickUpText);
                        } else {
                            pickUpText = dropLoc.getText().toString();
                        }*/
                    }
                } else {
                    new Utils(mcontext);
                    Utils.toastTxt("No location found with these address.", mcontext);
                }
            } catch (IOException e) {
                Log.e("error123", e.getMessage());
                e.printStackTrace();
            }

            pickUpLat = String.valueOf(place.getLatLng().latitude);
            pickUpLang = String.valueOf(place.getLatLng().longitude);
            Utils.hideSoftKeyboard(ActivityEditBookingInfo.this);
           /* mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            mWebTextView.setText(place.getWebsiteUri() + "");
            if(attributions != null)
            {
                mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }*/
        }
    };

    private void clickListners() {
        etBookingType.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        etDate.setOnClickListener(this);
        etTime.setOnClickListener(this);

        btnBookUpdate.setOnClickListener(this);
        btnEstimatePrice.setOnClickListener(this);
        btnChoose.setOnClickListener(this);
        btnNStops.setOnClickListener(this);
        btnChooseCard.setOnClickListener(this);
        autoPickupLoc.setOnClickListener(this);
        autoDropLoc.setOnClickListener(this);
        ivClosePickUp.setOnClickListener(this);
        ivCloseDropLoc.setOnClickListener(this);
        car_ManualT.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.nstops:
                pickStops();
                break;

            case R.id.submit:
                submit();
                break;

            case R.id.back:
                onBackPressed();
                break;

            case R.id.date:
                showDatePickerDialog();
                break;

            case R.id.time:
                showTimePickerDialog();
                break;

            case R.id.card_choose:
                Intent j = new Intent(mcontext, CardsList.class);
                startActivityForResult(j, 2);
                break;

            case R.id.close11:
                autoPickupLoc.setText("");
                ivClosePickUp.setVisibility(View.GONE);
                pickUpLat = "";
                pickUpLang = "";
                break;

            case R.id.close22:
                autoDropLoc.setText("");
                ivCloseDropLoc.setVisibility(View.GONE);
                dropLang = "";
                dropLat = "";
                break;

            case R.id.pickup:
                ivCloseDropLoc.setVisibility(View.GONE);
                autoPickupLoc.setSelection(0);
                autoPickupLoc.setCursorVisible(true);
                autoDropLoc.setSelection(0);
                autoDropLoc.setCursorVisible(false);

                if (autoPickupLoc.getText().length() > 0) {
                    autoPickupLoc.setSelection(autoPickupLoc.getText().length());
                    isAutoPickUp = true;
                    ivClosePickUp.setVisibility(View.VISIBLE);
                } else {
                    ivClosePickUp.setVisibility(View.GONE);
                }
                isClick = 1;
                break;

            case R.id.drop:
                ivClosePickUp.setVisibility(View.GONE);
                autoDropLoc.setSelection(0);
                autoDropLoc.setCursorVisible(true);
                autoPickupLoc.setSelection(0);
                autoPickupLoc.setCursorVisible(false);

                isAutoDropLoc = true;

                Log.e("error123", "error123");

                if (autoDropLoc.getText().length() > 0) {
                    autoDropLoc.setSelection(autoDropLoc.getText().length());
                    ivCloseDropLoc.setVisibility(View.VISIBLE);
                } else {
                    ivCloseDropLoc.setVisibility(View.GONE);
                }
                isClick = 2;
                break;

            case R.id.price:
                estimatedRideCostRequest();
                break;

            case R.id.btn_apply_promo:
                showPromoDialog();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {
                    dropMap = new HashMap<>();
                    dropMap = (HashMap<String, Object>) data.getSerializableExtra("map");

                    if (dropMap != null && dropMap.size() > 0) {
                        if (dropMap.containsKey("street_address") && !dropMap.get("street_address").
                                toString().equalsIgnoreCase("")) {
                            autoDropLoc.post(new Runnable() {
                                public void run() {
                                    autoDropLoc.dismissDropDown();
                                }
                            });

                            autoDropLoc.setText(dropMap.get("street_address").toString());
                        }

                        if (dropMap.containsKey("notes") && !dropMap.get("notes").toString().equalsIgnoreCase("")) {
                            etNote.setText(dropMap.get("notes").toString());
                        }

                        if (dropMap.containsKey("lat") && !dropMap.get("lat").toString().equalsIgnoreCase("")) {
                            dropLat = dropMap.get("lat").toString();
                            dropLang = dropMap.get("lon").toString();
                        }
                    }
                }
                if (resultCode == Activity.RESULT_CANCELED) {

                }
            } else if (requestCode == 2) {
                cardMap = new HashMap<>();
                cardMap = (HashMap<String, Object>) data.getSerializableExtra("map");

                Log.e(TAG, "cardMap: "+cardMap.toString());

                if (cardMap != null && cardMap.size() > 0) {
                    tvNoCard.setVisibility(View.GONE);
                    tvCardNumber.setVisibility(View.VISIBLE);
                    tvMonthTitle.setVisibility(View.VISIBLE);
                    etMonth.setVisibility(View.VISIBLE);
                    etYearTitle.setVisibility(View.VISIBLE);
                    etYear.setVisibility(View.VISIBLE);

                    if (cardMap.containsKey("token") && !cardMap.get("token").toString().
                            equalsIgnoreCase("")) {
                        tvCardNumber.setText("" + cardMap.get("token").toString().replaceAll("\\w(?=\\w{4})", "*"));
                        payment_Id = cardMap.get("token").toString();
                        card_numberId = cardMap.get("acctid").toString();
                    }
//                    if (cardMap.containsKey("exp_month") && !cardMap.get("exp_month").toString().
//                            equalsIgnoreCase("")) {
//                        etMonth.setText(cardMap.get("exp_month").toString());
//                    }
//                    if (cardMap.containsKey("exp_year") && !cardMap.get("exp_year").toString().
//                            equalsIgnoreCase("")) {
//                        etYear.setText(cardMap.get("exp_year").toString());
//                    }

                    String expiryTotal = "";
                    if(cardMap.get("expiry").toString().length() >= 2){
                        String mm = cardMap.get("expiry").toString().substring(0, 2);
                        String yy = cardMap.get("expiry").toString().substring(Math.max(cardMap.get("expiry").toString().length() - 2, 0));
                        etMonth.setText(mm);
                        etYear.setText(yy);
                    }


                    SavePref pref1 = new SavePref();
                    pref1.SavePref(mcontext);
                    pref1.setCardId(payment_Id);
                    pref1.setAcctid(card_numberId);
                    pref1.setCardNumber(tvCardNumber.getText().toString());
                    pref1.setCardExMonth(etMonth.getText().toString());
                    pref1.setCardExYear(etYear.getText().toString());
                }
            } else if (requestCode == 3) {
                stopsAddList = new ArrayList<>();
                stopsAddList = (List<HashMap<String, Object>>) data.getSerializableExtra("map");

                // status_stops.setText("Added.");
                // status_stops.setTextColor(getResources().getColor(R.color.green));
                // status_stops.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tick, 0, 0, 0);

                if (stopsAddList != null && stopsAddList.size() > 0)
                    loadRequestsList(mcontext, stopsAddList, "");
            }
        }
    }

    public static void loadRequestsList(Context context, List<HashMap<String, Object>> viewList, String mode) {
        // Utils.e(TAG+"81", "load browseMembersList Data"+mode+viewList);
        if (viewList != null && viewList.size() > 0) {
            try {
                //Utils.e(TAG+"88", "browseMembersList new");
                requestsList = new ArrayList<>();

                //Utils.e(TAG+"93", "browseMembersList new "+eventList);

                for (int i = 0; i < viewList.size(); i++) {
                    HashMap<String, Object> mp = new HashMap<String, Object>();
                    mp = viewList.get(i);

                    if (!requestsList.contains(mp)) {
                        requestsList.add(new modelItem(mp));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (!mode.equalsIgnoreCase("update")) {
                    setAdapterFriendsRequestList(context);
                } else {
                    requestsAdapter.notifyItemInserted(requestsList.size());
                    requestsAdapter.notifyDataSetChanged();
                }
            }
        } else {
            rvStopsLocList.setVisibility(View.GONE);
        }
    }

    public static void setAdapterFriendsRequestList(final Context context) {
        if (rvStopsLocList != null) {
            rvStopsLocList.setVisibility(View.VISIBLE);
        }
        requestsAdapter = new StopsAddressAdapter(appCompatActivity, requestsList, rvStopsLocList,
                R.layout.stop_rowitem, ConstVariable.StopLocations);
        rvStopsLocList.setAdapter(requestsAdapter);
    }

    public void estimatedRideCostRequest() {
        if (autoPickupLoc.toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter pickup location.", mcontext);
        } else if (autoDropLoc.toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter destination location.", mcontext);
        } else {
            HashMap map = new HashMap<>();
            map.put("pick_lat", pickUpLat);
            map.put("pick_lng", pickUpLang);
            map.put("dest_lat", dropLat);
            map.put("dest_lng", dropLang);
            map.put("promo", promoCode);
            map.put("count", stopsAddList != null ? stopsAddList.size() : 0);
            OnlineRequest.updateEditPriceRequest(mcontext, map);
        }
    }

    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter_pickup.setGoogleApiClient(Utils.global.mGoogleApiClient_pickup);
        mPlaceArrayAdapter_drop.setGoogleApiClient(Utils.global.mGoogleApiClient_drop);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter_pickup.setGoogleApiClient(null);
        mPlaceArrayAdapter_drop.setGoogleApiClient(null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hh = "";
            String mm = "";

            if (hourOfDay < 10)
                hh = "0" + String.valueOf(hourOfDay);
            else
                hh = String.valueOf(hourOfDay);

            if (minute < 10)
                mm = "0" + String.valueOf(minute);
            else
                mm = String.valueOf(minute);

            String time_24 = "";

            time_24 = hh + ":" + mm;

            try {
                String _24HourTime = time_24;
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                Date _24HourDt = _24HourSDF.parse(_24HourTime);
                etTime.setText(_12HourSDF.format(_24HourDt));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            // Create a new instance of DatePickerDialog and return it
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String yy = "";
            String mm = "";
            String dd = "";

            if ((month + 1) < 10)
                mm = "0" + String.valueOf(month + 1);
            else
                mm = String.valueOf(month + 1);

            if (day < 10)
                dd = "0" + String.valueOf(day);
            else
                dd = String.valueOf(day);

            if (year < 10)
                yy = "0" + String.valueOf(year);
            else
                yy = String.valueOf(year);

            etDate.setText(mm + "-" + dd + "-" + yy);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (Utils.global.mGoogleApiClient_pickup != null) {
            if (Utils.global.mGoogleApiClient_pickup.isConnected()) {
                Utils.global.mGoogleApiClient_pickup.stopAutoManage(appCompatActivity);
                Utils.global.mGoogleApiClient_pickup.disconnect();
            }
        }

        if (Utils.global.mGoogleApiClient_drop != null) {
            if (Utils.global.mGoogleApiClient_drop.isConnected()) {
                Utils.global.mGoogleApiClient_drop.stopAutoManage(appCompatActivity);
                Utils.global.mGoogleApiClient_drop.disconnect();
            }
        }
    }

    public void showUpdatedPriceDialog(final HashMap<String, Object> data) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog8, null);
        final AlertDialog dialog = new AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        TextView title = dialog.findViewById(R.id.title);
        TextView distance = dialog.findViewById(R.id.distance);
        TextView time = dialog.findViewById(R.id.time);
        TextView price = dialog.findViewById(R.id.price);
        Button ok = dialog.findViewById(R.id.ok);
        title.setText(R.string.app_name);

        if (data != null && data.size() > 0) {
            if (data.containsKey("distance") && !data.get("distance").toString().equalsIgnoreCase("")) {
                distance.setText(String.format("%.2f", Double.valueOf(data.get("distance").toString())) + " mi");
            }

            if (data.containsKey("estimate_time") && !data.get("estimate_time").toString()
                    .equalsIgnoreCase("null")) {
                time.setText(data.get("estimate_time").toString());

                if (data.containsKey("estimate_price") && !data.get("estimate_price").toString().
                        equalsIgnoreCase("")) {
                    price.setText(data.get("estimate_price").toString() + " $");
                }
            }
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.cancel();
                } catch (Exception e) {

                }
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

    public void submit() {
        if (car_ManualT.isChecked()) {
            isCarML = "manual";
        } else {
            isCarML = "automatic";
        }

        if (bookingTypeStatus.equalsIgnoreCase("1")) {
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat df1 = new SimpleDateFormat("hh:mm a");

            date_ride = df.format(c);
            time_ride = df1.format(c);

        } else {
            date_ride = etDate.getText().toString();
            time_ride = etTime.getText().toString();
        }

        new Utils(mcontext);
        if (bookingTypeStatus.equalsIgnoreCase("2") && etDate.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please select date.", mcontext);
        } else if (bookingTypeStatus.equalsIgnoreCase("2") && etTime.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please select time.", mcontext);
        } else if (autoPickupLoc.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter pickup location.", mcontext);
        } else if (autoDropLoc.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter drop location.", mcontext);
        } else if (tvCardNumber.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please select card for payment.", mcontext);
        } else {
            jsonStops = new JSONObject();

            if (stopsAddList != null) {
                if (stopsAddList.size() > 0) {
                    for (int i = 0; i < stopsAddList.size(); i++) {
                        try {
                            jsonStops.put("location" + (i + 1), stopsAddList.get(i).get("location").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            json = new JSONObject();
            SavePref pref1 = new SavePref();
            pref1.SavePref(mcontext);
            String id = pref1.getUserId();

            try {
                json.put("userid", id);
                Log.e("Tagggg", payment_Id);
                json.put("nstops", btnNStops.getText().toString());
                json.put("platitude", pickUpLat);
                json.put("plongitude", pickUpLang);
                json.put("date", date_ride);
                json.put("time", time_ride);
                json.put("booking_type", bookingTypeStatus);
                json.put("pickup_address", autoPickupLoc.getText().toString());
                json.put("drop_address", autoDropLoc.getText().toString());
                json.put("notes", etNote.getText().toString());
                json.put("dlatitude", dropLat);
                json.put("dlongitude", dropLang);
                json.put("transmission", isCarML);
                json.put("card_id", payment_Id);
                json.put("acctid", card_numberId);
                json.put("booking_id", rideId);
                json.put("pickup_city", pickUpCityName);
                json.put("drop_city", dropCityName);
                json.put("promo", promoCode);
                map = new HashMap<>();
                map.put("json", json);
                map.put("jsonstops", jsonStops);
                OnlineRequest.updateBookingRequest(mcontext, map);
                Log.e("Tagggg", map.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void pickStops() {
        Intent i = new Intent(mcontext, AddStops.class);

        if (stopsAddList != null)
            i.putExtra("map", (Serializable) stopsAddList);

        startActivityForResult(i, 3);
    }

    private void checkNoOfStopsList() {
        ServiceApi service = ServiceGenerator.createService(ServiceApi.class);
        Call<StopsList> response = service.fetchStopsList(rideId);
        Utils.initiatePopupWindow(mcontext, "Please wait...");
        response.enqueue(new Callback<StopsList>() {
            @Override
            public void onResponse(Call<StopsList> call, Response<StopsList> response) {

                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
                stopsAddList = new ArrayList<>();
                //Utils.toastTxt(response.body().getMessage(), mcontext);

                if (response.body().getDataStops() != null && response.body().getDataStops().size() > 0) {
                    for (int i = 0; i < response.body().getDataStops().size(); i++) {
                        try {
                            stMap = new HashMap<>();
                            stMap.put("location", response.body().getDataStops().get(i).getLocation());
                            stopsAddList.add(stMap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (stopsAddList != null && stopsAddList.size() > 0)
                        loadRequestsList(mcontext, stopsAddList, "");
                }
            }

            @Override
            public void onFailure(Call<StopsList> call, Throwable t) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
            }
        });

    }

    private void postUpdatedData(HashMap<String, Object> map) {
        ServiceApi service = ServiceGenerator.createService(ServiceApi.class);
        Call<JSONObject> response = service.postUpdateData(Settings.URL_EDIT_RIDE_INFO, map);
        Utils.initiatePopupWindow(mcontext, "Please wait...");
        response.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                //removeLastChar(response.toString());
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                    finish();
                    DashBorad.IS_UPDATED_RIDE = true;
                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
            }
        });

    }

    public void showPromoDialog() {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog_apply_promo, null);
        final AlertDialog dialog = new AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();
        this.alertDialog = dialog;

        final EditText etPromoCode = dialog.findViewById(R.id.et_apply_promo);
        Button btnApply = dialog.findViewById(R.id.btn_apply_promo);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_promo);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPromoCode.getText().toString().trim().equals("")) {
                    map = new HashMap<>();
                    promoCode = etPromoCode.getText().toString();
                    map.put("promo", promoCode);
                    OnlineRequest.applyPromo(mcontext, map);
                } else {
                    Utils.toastTxt("Please Enter Promo Code", mcontext);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPromoCode.setText("");
                promoCode = "";
                dismissDialog();
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

    public void dismissDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

}
