package com.latenightchauffeurs.fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.APIClient;
import com.latenightchauffeurs.Utils.APIInterface;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.GetDistanceTimeAsyncTask;
import com.latenightchauffeurs.Utils.IonListners;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.ParsingHelper;
import com.latenightchauffeurs.Utils.ServiceApi;
import com.latenightchauffeurs.Utils.ServiceGenerator;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.activity.AddStops;
import com.latenightchauffeurs.activity.CardsList;
import com.latenightchauffeurs.activity.DropAddressList;
import com.latenightchauffeurs.activity.Navigation;
import com.latenightchauffeurs.adapter.PlaceArrayAdapter;
import com.latenightchauffeurs.adapter.StopsAddressAdapter;
import com.latenightchauffeurs.model.GApiKeyPojo;
import com.latenightchauffeurs.model.ItemCardList;
import com.latenightchauffeurs.model.SavePref;
import com.latenightchauffeurs.model.modelItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ClickableViewAccessibility")
public class BookReservation_new extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, IonListners {
    private static final String TAG = "BookReservation_new";
    private static int AUTOCOMPLETE_DROP_REQUEST_CODE = 999;
    private static int AUTOCOMPLETE_PICK_REQUEST_CODE = 901;

    public Call<ResponseBody> call = null;
    public APIInterface apiInterface = APIClient.getClientVO().create(APIInterface.class);

    static RecyclerView rv_locCard;
    public static MyRewardProgramAdapter requestsAdapterCard;
    public static RecyclerView.LayoutManager requestsListManCard;
    public static List<modelItem> requestsListCard;
    public HashMap<String, Object> mapCard;
    private Long lastClickTime = Long.valueOf(0);

    public ArrayList<ItemCardList> itemCardLists = new ArrayList<ItemCardList>();


    RelativeLayout relativeLayoutCardContainer;
    Switch aSwitch;


    EditText editTextCardName, editTextCardNumber, editTextExpiryDate, editTextCVV, editTextPostalCode;
    Button buttonSubmit;


    String card_numberSelect = "";
    String card_numberId = "";

    boolean pickupBoolean = false;
    boolean dropBoolean = false;

    boolean buttonHide = false;

    static Calendar calendarSelected = null;
    static Calendar calendarCurrent = null;


    static int selectedYY = 0;
    static int selectedMM = 0;
    static int selectedDD = 0;


    public static BookReservation_new Instance;
    // public  List<String> stopsList;
    public List<String> bTypeList;

    private Unbinder unbinder;

   /* @BindView(R.id.stopstatus)
    TextView status_stops;*/

    @BindView(R.id.submit)
    Button btnBookNow;

    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @BindView(R.id.price)
    Button btnEstimatePrice;

    @BindView(R.id.nstops)
    Button no_stops;

//    @BindView(R.id.btype)
//    TextView etBookingType;

    static EditText date;
    static EditText time;

    @BindView(R.id.pickup)
    AutoCompleteTextView pickupLoc;

    @BindView(R.id.drop)
    AutoCompleteTextView dropLoc;

    @BindView(R.id.note)
    EditText etNote;

    @BindView(R.id.car_ml)
    CheckBox car_ManualT;

    @BindView(R.id.choose)
    Button choose;

    @BindView(R.id.cnumner)
    TextView card_number;

    @BindView(R.id.emonthtitle)
    TextView emonthtitle;

    @BindView(R.id.emonth)
    TextView emonth;

    @BindView(R.id.eyeartitle)
    TextView eyeartitle;

    @BindView(R.id.eyear)
    TextView eyear;

    @BindView(R.id.nocard)
    TextView nodata;

    @BindView(R.id.card_choose)
    Button chooseCard;

    @BindView(R.id.close22)
    ImageView close22;

    @BindView(R.id.close11)
    ImageView close11;

    @BindView(R.id.btn_apply_promo)
    Button btnPromoCode;

    @BindView(R.id.tv_add_stops)
    TextView tvAddStopsText;

    @BindView(R.id.card_payment)
    CardView cardPaymentView;

    @BindView(R.id.view_show)
    View viewShow;

    @BindView(R.id.ll_datetime)
    LinearLayout llDateTime;

    @BindView(R.id.rl_payment)
    RelativeLayout rlChooseCard;

    boolean iscursor = false, isdcursor = false;

    public static boolean bookingAsap = false;

    public static Context mContext;
    public HashMap<String, Object> dataMap;
    private static long dateCalendar = 0, calTime = 0;

    private static final int GOOGLE_API_CLIENT_ID_PICK = 115, GOOGLE_API_CLIENT_ID_DROP = 116;
    private PlaceArrayAdapter mPlaceArrayAdapter_pickup, mPlaceArrayAdapter_drop;
    public String lat_pickup = "", long_pick = "", lat_drop = "", long_drop = "", payment_Id = "", status_BookingType = "";
    public String new_lat_pickup = "", new_long_pick = "", new_lat_drop = "", new_long_drop = "";
    private final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new
            LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    String date_ride = "", time_ride = "";
    HashMap<String, Object> map, dropMap, cardMap;
    List<HashMap<String, Object>> stopsAddList;
    JSONObject json, jsonStops;
    public String isCarML = "", promoCode = "";
    public int isClick = 0;
    IonListners listners;

    public static RecyclerView rv_loc;
    public static StopsAddressAdapter requestsAdapter;
    public List<HashMap<String, Object>> list = new ArrayList<>();
    public static List<modelItem> requestsList;
    private AlertDialog alertDialog = null;
    private static OnFragmentInteractionListenerBooking mListener;
    private String pickUpCityName = "", dropCityName = "", pickUpText, dropText;

    @BindView(R.id.ll_apply_promo)
    LinearLayout llPromo;

    @BindView(R.id.et_apply_promo)
    EditText etPromoName;

    @BindView(R.id.btn_apply)
    Button btnApply;

    private String googleApiKey = "";
    static int hour = 0;
    static int minute = 0;
    static int second = 0;

    public interface OnFragmentInteractionListenerBooking {
        void onFragmentInteraction(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_bookreservation_1, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setVisibility(false);
        listners = this;
        getGoogApiKey();
    }

    private void getGoogApiKey() {
        Utils.initiatePopupWindow(getActivity(), "Please wait...");
        ServiceApi service = ServiceGenerator.createService(ServiceApi.class);
        Call<GApiKeyPojo> response = service.getGoogleApiKey();
        response.enqueue(new Callback<GApiKeyPojo>() {
            @Override
            public void onResponse(Call<GApiKeyPojo> call, Response<GApiKeyPojo> response) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
                if (response.body() != null && response.body().getData() != null) {
                    googleApiKey = response.body().getData().getKey();
                } else {
                    Utils.toastTxt(response.body().getMsg(), getActivity());
                }

            }

            @Override
            public void onFailure(Call<GApiKeyPojo> call, Throwable t) {
                //call.execute().body().getData().getCreatedDate()
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
            }
        });
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        initBinding(v);

        setOnClickListener();

        cardList();

        initializeAutoCompleteFragment();

        /**
         *  Commented due  to code deprecated and not working.
         */
        /*Utils.global.mGoogleApiClient_pickup = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID_PICK, this)
                .addConnectionCallbacks(this)
                .build();

        Utils.global.mGoogleApiClient_drop = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID_DROP, this)
                .addConnectionCallbacks(this)
                .build();

        pickupLoc.setThreshold(3);
        pickupLoc.setOnItemClickListener(mAutocompleteClickListener_pick);
        mPlaceArrayAdapter_pickup = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        pickupLoc.setAdapter(mPlaceArrayAdapter_pickup);

        dropLoc.setThreshold(3);
        dropLoc.setOnItemClickListener(mAutocompleteClickListener_drop);
        mPlaceArrayAdapter_drop = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        dropLoc.setAdapter(mPlaceArrayAdapter_drop);
        dropLoc.setText("");*/

        if (car_ManualT.isChecked()) {
            isCarML = "manual";
        } else {
            isCarML = "automatic";
        }

    }

    private void setOnClickListener() {

        buttonSubmit.setOnClickListener(v -> {

            if (
                    !editTextCardName.getText().toString().equalsIgnoreCase("") ||
                            !editTextCardNumber.getText().toString().equalsIgnoreCase("") ||
                            !editTextExpiryDate.getText().toString().equalsIgnoreCase("") ||
                            !editTextCVV.getText().toString().equalsIgnoreCase("") ||
                            !editTextPostalCode.getText().toString().equalsIgnoreCase("")
//                                    ||
//                                    !editTextCVV.getText().toString().equalsIgnoreCase("")
            ) {


                callRefreshMethod(
                        editTextCardName.getText().toString(),
                        editTextCardNumber.getText().toString(),
                        editTextExpiryDate.getText().toString(),
                        editTextCVV.getText().toString(),
                        editTextPostalCode.getText().toString()

                );


            } else {

                Toast.makeText(mContext, "All field is required!", Toast.LENGTH_SHORT).show();
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

        btnApply.setOnClickListener(v12 -> {
            if (!etPromoName.getText().toString().trim().equals("")) {
                map = new HashMap<>();
                promoCode = etPromoName.getText().toString();
                map.put("promo", promoCode);
                OnlineRequest.applyPromo(mContext, map);
                Utils.hideSoftKeyboard(getActivity());
            } else {
                Utils.toastTxt("Please Enter Promo Code", mContext);
            }
        });
        car_ManualT.setOnClickListener(this);
        btnBookNow.setOnClickListener(this);
        btnEstimatePrice.setOnClickListener(this);
        choose.setOnClickListener(this);
        no_stops.setOnClickListener(this);
        chooseCard.setOnClickListener(this);
        pickupLoc.setOnClickListener(this);
        dropLoc.setOnClickListener(this);
        close11.setOnClickListener(this);
        close22.setOnClickListener(this);
        date.setOnClickListener(this);
        time.setOnClickListener(this);
        car_ManualT.setOnClickListener(this);
        btnPromoCode.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        close11.setVisibility(View.GONE);
        close22.setVisibility(View.GONE);

        card_number.setVisibility(View.GONE);
        emonthtitle.setVisibility(View.GONE);
        emonth.setVisibility(View.GONE);
        eyeartitle.setVisibility(View.GONE);
        eyear.setVisibility(View.GONE);

        // stopsList=new ArrayList<>();
        bTypeList = new ArrayList<>();
        // stopsList.add("No");
        // stopsList.add("Yes");
        // bTypeList.add("AS SOON AS POSSIBLE");

        //bTypeList.add("MAKE RESERVATION");

        // no_stops.setText(stopsList.get(0));
        //  etBookingType.setText("MAKE RESERVATION");

        // status_BookingType = "1";
        // date.setVisibility(View.GONE);
        // time.setVisibility(View.GONE);

        date.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);
        status_BookingType = "2";

        SavePref pref1 = new SavePref();
        pref1.SavePref(mContext);
        String cid = pref1.getCardId();
        String cnumber = pref1.getCardNumber();
        String cmonth = pref1.getCardExMonth();
        String cyear = pref1.getCardExYear();

        if (cid.equalsIgnoreCase("")) {
            nodata.setVisibility(View.VISIBLE);
            card_number.setVisibility(View.GONE);
            emonthtitle.setVisibility(View.GONE);
            emonth.setVisibility(View.GONE);
            eyeartitle.setVisibility(View.GONE);
            eyear.setVisibility(View.GONE);
        } else {
            nodata.setVisibility(View.GONE);
            card_number.setVisibility(View.VISIBLE);
            emonthtitle.setVisibility(View.VISIBLE);
            emonth.setVisibility(View.VISIBLE);
            eyeartitle.setVisibility(View.VISIBLE);
            eyear.setVisibility(View.VISIBLE);
        }

        /*pickupLoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && iscursor == true) {
                    close11.setVisibility(View.VISIBLE);
                } else {
                    close11.setVisibility(View.GONE);
                }

                pickupBoolean = false;
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        dropLoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && isdcursor == true) {
                    close22.setVisibility(View.VISIBLE);
                } else {
                    close22.setVisibility(View.GONE);
                }

                dropBoolean = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        dropLoc.setOnTouchListener((v, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return false;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                launchPlaceIntentBuilder(AUTOCOMPLETE_DROP_REQUEST_CODE);
            }
            return false;
        });

        pickupLoc.setOnTouchListener((v, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return false;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                launchPlaceIntentBuilder(AUTOCOMPLETE_PICK_REQUEST_CODE);
            }
            return false;
        });

        if (!cnumber.equalsIgnoreCase("")) {
            card_number.setText(cnumber);
            payment_Id = cid;
        }
        if (!cmonth.equalsIgnoreCase("")) {
            emonth.setText(cmonth);
        }
        if (!cyear.equalsIgnoreCase("")) {
            eyear.setText(cyear);
        }
    }

    private void launchPlaceIntentBuilder(int requestCode) {
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).build(getContext());
        startActivityForResult(intent, requestCode);
    }

    private void initBinding(View v) {
        buttonSubmit = v.findViewById(R.id.submit_card);
        unbinder = ButterKnife.bind(this, v);
        rv_loc = v.findViewById(R.id.rv_loc);
        date = v.findViewById(R.id.date);
        time = v.findViewById(R.id.time);
        mContext = getContext();
        Instance = this;
        rv_loc.setHasFixedSize(true);
        rv_loc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rv_locCard = v.findViewById(R.id.rv_loc_cards);
        requestsListManCard = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_locCard.setHasFixedSize(true);
        rv_locCard.setLayoutManager(requestsListManCard);
        requestsAdapterCard = new MyRewardProgramAdapter(getActivity(), itemCardLists);
        rv_locCard.setAdapter(requestsAdapterCard);

        rv_locCard.setVisibility(View.GONE);

        aSwitch = v.findViewById(R.id.addcard_top);
        relativeLayoutCardContainer = v.findViewById(R.id.m_reative);

        relativeLayoutCardContainer.setVisibility(View.GONE);

        aSwitch.setOnClickListener(v1 -> {
            if (aSwitch.isChecked() == true) {
                relativeLayoutCardContainer.setVisibility(View.VISIBLE);
            } else {
                relativeLayoutCardContainer.setVisibility(View.GONE);
            }
        });

        editTextCardName = v.findViewById(R.id.card_name);
        editTextCardNumber = v.findViewById(R.id.card_number);
        editTextExpiryDate = v.findViewById(R.id.button678789789);
        editTextCVV = v.findViewById(R.id.button675768);
        editTextPostalCode = v.findViewById(R.id.button423435);
    }

    private List<Place.Field> fields;
    private void initializeAutoCompleteFragment() {
        /** Initializing the Places API with the help of our API_KEY*/
        if (!Places.isInitialized()) {
            Places.initialize(
                    getActivity().getApplicationContext(),
                    getString(R.string.google_map_key));
        }
        /**
         * Set the fields to specify which types of place data to
         * return after the user has made a selection.
         * */
        fields = Arrays.asList(
                Place.Field.ID,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS,
                Place.Field.NAME);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    dropMap = new HashMap<>();

                    dropMap = (HashMap<String, Object>) data.getSerializableExtra("map");

                    if (dropMap != null && dropMap.size() > 0) {
                        if (dropMap.containsKey("street_address") && !dropMap.get("street_address").toString().equalsIgnoreCase("")) {
                            dropLoc.post(new Runnable() {
                                public void run() {
                                    dropLoc.dismissDropDown();
                                }
                            });

                            //  dropLoc.setText(dropMap.get("street_address").toString());
                        }

                        if (dropMap.containsKey("notes") && !dropMap.get("notes").toString().equalsIgnoreCase("")) {
                            etNote.setText(dropMap.get("notes").toString());
                        }

                        if (dropMap.containsKey("lat") && !dropMap.get("lat").toString().equalsIgnoreCase("")) {
                            lat_drop = dropMap.get("lat").toString();
                            long_drop = dropMap.get("lon").toString();
                        }
                    }
                }
                if (resultCode == RESULT_CANCELED) {

                }
            } else if (requestCode == 2) {
                cardMap = new HashMap<>();
                cardMap = (HashMap<String, Object>) data.getSerializableExtra("map");

                if (cardMap != null && cardMap.size() > 0) {
                    nodata.setVisibility(View.GONE);
                    card_number.setVisibility(View.VISIBLE);
                    emonthtitle.setVisibility(View.VISIBLE);
                    emonth.setVisibility(View.VISIBLE);
                    eyeartitle.setVisibility(View.VISIBLE);
                    eyear.setVisibility(View.VISIBLE);

                    if (cardMap.containsKey("token") && !cardMap.get("token").toString().
                            equalsIgnoreCase("")) {
                        card_number.setText("" + cardMap.get("token").toString());
                        payment_Id = cardMap.get("token").toString();
                    }

                    if (cardMap.containsKey("expiry")) {
                        if (cardMap.get("expiry").toString().length() >= 2) {
                            String cc = cardMap.get("expiry").toString().substring(0, 2);
                            emonth.setText(cc);
                        }

                        if (cardMap.get("expiry").toString().length() >= 4) {
                            String substring = cardMap.get("expiry").toString().substring(Math.max(cardMap.get("expiry").toString().length() - 2, 0));
                            eyear.setText(substring);
                        }
                    }


//                    if (cardMap.containsKey("exp_month") && !cardMap.get("exp_month").toString().
//                            equalsIgnoreCase("")) {
//                        emonth.setText(cardMap.get("exp_month").toString());
//                    }
//                    if (cardMap.containsKey("exp_year") && !cardMap.get("exp_year").toString().
//                            equalsIgnoreCase("")) {
//                        eyear.setText(cardMap.get("exp_year").toString());
//                    }

                    SavePref pref1 = new SavePref();
                    pref1.SavePref(mContext);
                    pref1.setCardId(payment_Id);
                    pref1.setCardNumber(card_number.getText().toString());
                    pref1.setCardExMonth(emonth.getText().toString());
                    pref1.setCardExYear(eyear.getText().toString());
                }
            } else if (requestCode == 3) {
                stopsAddList = new ArrayList<>();
                stopsAddList = (List<HashMap<String, Object>>) data.getSerializableExtra("map");
                if (stopsAddList != null)
                    loadRequestsList(mContext, stopsAddList, "");
            }
        }
        if (requestCode == AUTOCOMPLETE_DROP_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId()+ ", " + place.getLatLng());
                dropLoc.setText(place.getAddress());

                Geocoder geocoder = new Geocoder(mContext);
                try {
                    List<android.location.Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,
                            place.getLatLng().longitude, 1);

                    if (addresses != null && addresses.size() > 0) {
                        if (addresses.get(0).getAddressLine(0) != null) {
                            dropText = addresses.get(0).getAddressLine(0);
                            Utils.e(Utils.Tag, " mcountryPick " + " cityName1 " + dropText);
                        } else {
                            dropText = dropLoc.getText().toString();
                        }

                        if (addresses.get(0).getCountryName() != null) {
                            String cityName = addresses.get(0).getLocality();
                            Utils.e(Utils.Tag, " mcountryDrop " + "  " + cityName);
                            if (cityName != null && cityName.length() > 0)
                                dropCityName = cityName;
                        }
                    } else {
                        new Utils(mContext);
                        Utils.toastTxt("No location found with these address.", mContext);
                    }
                } catch (Exception e) {e.printStackTrace();}
                lat_drop = String.valueOf(place.getLatLng().latitude);
                long_drop = String.valueOf(place.getLatLng().longitude);

                new_lat_pickup = lat_pickup;
                new_long_pick = long_pick;
                new_lat_drop = lat_drop;
                new_long_drop = long_drop;
                Utils.hideSoftKeyboard(getActivity());

                if ((!new_lat_pickup.trim().isEmpty() && !new_long_pick.trim().isEmpty()) &&
                        (!new_lat_drop.trim().isEmpty() && !new_long_drop.trim().isEmpty())) {
                    String distanceUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                            lat_pickup + "," + long_pick + "&destinations=" + lat_drop + "," + long_drop +
                            "&mode=driving&sensor=false&key=" + googleApiKey;

                    new GetDistanceTimeAsyncTask(getActivity(), distanceUrl, listners).execute();
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                /** The user canceled the operation.*/
            }
            return;
        }
        if (requestCode == AUTOCOMPLETE_PICK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                pickupLoc.setText(place.getAddress());

                Geocoder geocoder = new Geocoder(mContext);
                try {
                    List<android.location.Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,
                            place.getLatLng().longitude, 1);

                    if (addresses != null && addresses.size() > 0) {
                        if (addresses.get(0).getAddressLine(0) != null) {
                            //String fulladdress = addresses.get(0).getAddressLine(0).toString();
                            // Log.d("fulladdress", fulladdress);
                        }

                        if (addresses.get(0).getCountryName() != null) {
                            //String mcountry = addresses.get(0).getCountryName();
                            String cityName = addresses.get(0).getLocality();
                            if (cityName != null && cityName.length() > 0) {
                                pickUpCityName = cityName;
                            } else if (addresses.get(0).getSubLocality() != null) {
                                cityName = addresses.get(0).getSubLocality();
                                pickUpCityName = cityName;
                            } else {
                                cityName = addresses.get(0).getAdminArea();
                                pickUpCityName = cityName;
                            }

                            if (addresses.get(0).getAddressLine(0) != null) {
                                pickUpText = addresses.get(0).getAddressLine(0);
                            } else {
                                pickUpText = dropLoc.getText().toString();
                            }

                            Utils.e(Utils.Tag, "getLocality " + " " + cityName + "  " +
                                    addresses.get(0).getAdminArea());
                            //Log.d("country", mcountry);
                        }
                    } else {
                        new Utils(mContext);
                        Utils.toastTxt("No location found with these address.", mContext);
                    }
                } catch (IOException e) {
                    Log.e("error123", e.getMessage());
                    e.printStackTrace();
                }

                lat_pickup = String.valueOf(place.getLatLng().latitude);
                long_pick = String.valueOf(place.getLatLng().longitude);
                // new_lat_pickup = "", new_long_pick = "", new_lat_drop = "", new_long_drop = ""
                new_lat_pickup = lat_pickup;
                new_long_pick = long_pick;
                new_lat_drop = lat_drop;
                new_long_drop = long_drop;
                Utils.hideSoftKeyboard(getActivity());

                if ((!new_lat_pickup.trim().isEmpty() && !new_long_pick.trim().isEmpty()) &&
                        (!new_lat_drop.trim().isEmpty() && !new_long_drop.trim().isEmpty())) {
                    String distanceUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                            lat_pickup + "," + long_pick + "&destinations=" + lat_drop + "," + long_drop +
                            "&mode=driving&sensor=false&key=" + googleApiKey;

                    new GetDistanceTimeAsyncTask(getActivity(), distanceUrl, listners).execute();
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                /** The user canceled the operation.*/
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void cardList() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mContext);
        String id = pref1.getUserId();

        call = apiInterface.cardList(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String responseCode = "";
                try {
                    if (response.body() != null) {
                        responseCode = response.body().string();

                        Log.e(TAG, "Refreshed getMyRewardresponseCode: " + responseCode);

                        if (!responseCode.equals("")) {
                            itemCardLists = new ParsingHelper().getCardList(responseCode);
                            Log.e(TAG, "Refreshed getMyRewardProgramList: " + itemCardLists.size());
                        }

                        requestsAdapterCard.updateData(itemCardLists);
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });


    }


    public class MyRewardProgramAdapter extends RecyclerView.Adapter<MyRewardProgramAdapter.ViewHolder> {

        private int selectedPosition = -1;

        private RadioButton rbChecked = null;
        private int rbPosoition = 0;


        private static final String TAG = "MyRewardProgramAdapter";

        ArrayList<ItemCardList> arrayList = new ArrayList<ItemCardList>();

        Activity context;

        ImageLoader imageLoader;
        DisplayImageOptions options;


        public MyRewardProgramAdapter(Activity context11, ArrayList<ItemCardList> arrayList1) {
            super();
            this.context = context11;
            this.arrayList = arrayList1;

//            imageLoader = (ImageLoader) Utils.getImageLoaderDefaultCar(context11).get("imageLoader");
//            options = (DisplayImageOptions) Utils.getImageLoaderDefaultCar(context11).get("options");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_item2, viewGroup, false);
            return new ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            viewHolder.bindItems(arrayList.get(i), i, context);
        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View view11 = null;

            public ViewHolder(View itemView) {
                super(itemView);
                view11 = itemView;
            }


            public void bindItems(final ItemCardList itemMyRewardProgram, int i, final Activity context) {

                TextView cnumber = (TextView) itemView.findViewById(R.id.cnumber);
                TextView emonth = (TextView) itemView.findViewById(R.id.emonth);
                ImageView delete = (ImageView) itemView.findViewById(R.id.delete);

                Button buttonDelete = (Button) itemView.findViewById(R.id.delete_btn);


                // delete.setVisibility(View.GONE);
                RadioButton radioButton = (RadioButton) itemView.findViewById(R.id.radio0);

                String number = itemMyRewardProgram.getToken();
                String mask = number.replaceAll("\\w(?=\\w{4})", "*");

                cnumber.setText(itemMyRewardProgram.getAccttype() + "  " + mask);


                String expiryTotal = "";
                if (itemMyRewardProgram.getExpiry().toString().length() >= 2) {
                    String cc = itemMyRewardProgram.getExpiry().toString().substring(0, 2);
                    expiryTotal = cc;
                }
                if (itemMyRewardProgram.getExpiry().toString().length() >= 4) {
                    String substring = itemMyRewardProgram.getExpiry().toString().substring(Math.max(itemMyRewardProgram.getExpiry().toString().length() - 2, 0));
                    expiryTotal = expiryTotal + "/" + substring;
                }
                emonth.setText(expiryTotal);

                radioButton.setChecked(i == selectedPosition);
                radioButton.setOnClickListener(v -> {
                    if (i == selectedPosition) {
                        radioButton.setChecked(false);
                        selectedPosition = -1;
                    } else {
                        selectedPosition = i;
                        notifyDataSetChanged();
                    }

                    card_numberSelect = itemMyRewardProgram.getToken();
                    card_numberId = itemMyRewardProgram.getAcctid();
                });


                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemMyRewardProgram.isUpDown() == true) {
                            delete.setImageResource(R.drawable.arrow_down);
                            itemMyRewardProgram.setUpDown(false);
                            buttonDelete.setVisibility(View.GONE);
                        } else {
                            delete.setImageResource(R.drawable.arrow_up);
                            itemMyRewardProgram.setUpDown(true);
                            buttonDelete.setVisibility(View.VISIBLE);
                        }
                    }
                });


                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                        builder.setTitle(getActivity().getString(R.string.app_name));
                        builder.setMessage("Are you sure you want to delete this card.")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        deleteCardRequest(itemMyRewardProgram.getAcctid());
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        builder.show();

                    }
                });


            }

        }


        public void updateData(ArrayList<ItemCardList> arrayList2) {
            // TODO Auto-generated method stub
            //arrayList.clear();
//            alName.addAll(arrayList2);
            arrayList = arrayList2;
            notifyDataSetChanged();
        }


    }


    public void deleteCardRequest(String card_id) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mContext);
        String id = pref1.getUserId();

        new Utils(mContext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("card_id", card_id);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_DELETE_CARD);

        if (Utils.isNetworkAvailable(Utils.context)) {
            JsonPost.getNetworkResponse(Utils.context, null, Utils.global.mapMain, ConstVariable.DeleteCard2);
        } else {
            Utils.showInternetErrorMessage(Utils.context);
        }


        // remove-card.php?userid=166&card_id=2
    }


    private void callRefreshMethod(String cardName, String cardNumber, String expiry, String cardCVV, String postal) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Doing something, please wait.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String expiryMonth = "";
        String expiryYear = "";
        if (expiry.contains("/")) {
            expiryMonth = expiry.split("/")[0];
            expiryYear = "20" + expiry.split("/")[1];
        }

        Log.e(TAG, "expiryMonth: " + expiryMonth);
        Log.e(TAG, "expiryYear: " + expiryYear);


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
                                if (array.length() > 0) {
                                    JSONObject jsonObject1 = array.getJSONObject(0);
                                    String msg = jsonObject1.getString("msg");
                                    Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();


                                    editTextCardName.setText("");
                                    editTextCardNumber.setText("");
                                    editTextExpiryDate.setText("");
                                    editTextCVV.setText("");
                                    editTextPostalCode.setText("");


                                    cardList();

                                }
                            } else {
                                JSONArray array = jsonObject.getJSONArray("data");
                                if (array.length() > 0) {
                                    JSONObject jsonObject1 = array.getJSONObject(0);
                                    String msg = jsonObject1.getString("msg");

                                    if (msg.equalsIgnoreCase("Service not allowed") || msg.equalsIgnoreCase("Bad card check digit")) {
                                        Toast.makeText(getActivity(), "Invalid Card", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();

                                    }


                                }
                            }

                        } catch (Exception ex) {
                            Log.e(TAG, "Exception MSG " + ex.getMessage());
                            //  new ShowMsg().createSnackbar(AddCardInformation.this, "" + ex.getMessage());
                        }
                    } else {
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

    @Override
    public void onPause() {
        super.onPause();
//        Utils.global.mGoogleApiClient_pickup.stopAutoManage(getActivity());
//        Utils.global.mGoogleApiClient_pickup.disconnect();
//
//        Utils.global.mGoogleApiClient_drop.stopAutoManage(getActivity());
//        Utils.global.mGoogleApiClient_drop.disconnect();
       /* mGoogleApiClient_pickup.stopAutoManage(getActivity());
        mGoogleApiClient_pickup.disconnect();

        mGoogleApiClient_drop.stopAutoManage(getActivity());
        mGoogleApiClient_drop.disconnect();*/
    }

    /**
     *  Commented due  to code deprecated and not working.
     */
   /* private AdapterView.OnItemClickListener mAutocompleteClickListener_pick
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter_pickup.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            //Log.i(LOG_TAG, "Selected==============================: " + item.description);

            String[] items = item.toString().split(",");
            String value = items[0];

            pickupLoc.post(new Runnable() {
                public void run() {
                    pickupLoc.dismissDropDown();
                }
            });

            iscursor = false;
            close11.setVisibility(View.GONE);
            pickupLoc.setText(String.valueOf(item.description));

            pickupLoc.setCursorVisible(false);
            pickupLoc.setSelection(pickupLoc.getText().length());

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(Utils.global.mGoogleApiClient_pickup, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_pick);
            //Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);

            pickupBoolean = true;
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

            Geocoder geocoder = new Geocoder(mContext);
            try {
                List<android.location.Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,
                        place.getLatLng().longitude, 1);

                if (addresses != null && addresses.size() > 0) {
                    if (addresses.get(0).getAddressLine(0) != null) {
                        //String fulladdress = addresses.get(0).getAddressLine(0).toString();
                        // Log.d("fulladdress", fulladdress);
                        // city.setText(cityname);
                        //pickupLoc.setText(fulladdress);
                    }

                    if (addresses.get(0).getCountryName() != null) {
                        //String mcountry = addresses.get(0).getCountryName();
                        String cityName = addresses.get(0).getLocality();
                        if (cityName != null && cityName.length() > 0) {
                            pickUpCityName = cityName;
                        } else if (addresses.get(0).getSubLocality() != null) {
                            cityName = addresses.get(0).getSubLocality();
                            pickUpCityName = cityName;
                        } else {
                            cityName = addresses.get(0).getAdminArea();
                            pickUpCityName = cityName;
                        }

                        if (addresses.get(0).getAddressLine(0) != null) {
                            pickUpText = addresses.get(0).getAddressLine(0);
                        } else {
                            pickUpText = dropLoc.getText().toString();
                        }

                        Utils.e(Utils.Tag, "getLocality " + " " + cityName + "  " +
                                addresses.get(0).getAdminArea());
                        //Log.d("country", mcountry);
                        //country=mcountry;
                        //Utils.e("map======="+"105========",country);
                    }
                } else {
                    new Utils(mContext);
                    Utils.toastTxt("No location found with these address.", mContext);
                }
            } catch (IOException e) {
                Log.e("error123", e.getMessage());
                e.printStackTrace();
            }

            lat_pickup = String.valueOf(place.getLatLng().latitude);
            long_pick = String.valueOf(place.getLatLng().longitude);
            // new_lat_pickup = "", new_long_pick = "", new_lat_drop = "", new_long_drop = ""
            new_lat_pickup = lat_pickup;
            new_long_pick = long_pick;
            new_lat_drop = lat_drop;
            new_long_drop = long_drop;
            Utils.hideSoftKeyboard(getActivity());

            if ((!new_lat_pickup.trim().isEmpty() && !new_long_pick.trim().isEmpty()) &&
                    (!new_lat_drop.trim().isEmpty() && !new_long_drop.trim().isEmpty())) {
                String distanceUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                        lat_pickup + "," + long_pick + "&destinations=" + lat_drop + "," + long_drop +
                        "&mode=driving&sensor=false&key=" + googleApiKey;

                new GetDistanceTimeAsyncTask(getActivity(), distanceUrl, listners).execute();
            }

           *//* mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            mWebTextView.setText(place.getWebsiteUri() + "");
            if(attributions != null)
            {
                mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }*//*
        }
    };

    private AdapterView.OnItemClickListener mAutocompleteClickListener_drop
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter_drop.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            //Log.i(LOG_TAG, "Selected==============================: " + item.description);

            String[] items = item.toString().split(",");
            String value = items[0];
            dropLoc.setText("");
            dropLoc.post(new Runnable() {
                public void run() {
                    dropLoc.dismissDropDown();
                }
            });

            isdcursor = false;
            close22.setVisibility(View.GONE);
            dropLoc.setText(String.valueOf(item.description));
            dropLoc.setCursorVisible(false);
            dropLoc.setSelection(dropLoc.getText().length());

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(Utils.global.mGoogleApiClient_drop, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_drop);
            //Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);


            dropBoolean = true;
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
            Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            Geocoder geocoder = new Geocoder(mContext);
            try {
                List<android.location.Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,
                        place.getLatLng().longitude, 1);

                if (addresses != null && addresses.size() > 0) {
                    if (addresses.get(0).getAddressLine(0) != null) {
                        dropText = addresses.get(0).getAddressLine(0);
                        Utils.e(Utils.Tag, " mcountryPick " + " cityName1 " + dropText);
                    } else {
                        dropText = dropLoc.getText().toString();
                    }

                    if (addresses.get(0).getCountryName() != null) {
                        //String mcountry = addresses.get(0).getCountryName();
                        String cityName = addresses.get(0).getLocality();
                        //String cityName = addresses.get(0).getAddressLine(0);
                        Utils.e(Utils.Tag, " mcountryDrop " + "  " + cityName);
                        if (cityName != null && cityName.length() > 0)
                            dropCityName = cityName;
                        //Log.d("country", mcountry);
                        // country=mcountry;
                        //Utils.e("map======="+"105========",country);
                    }
                } else {
                    new Utils(mContext);
                    Utils.toastTxt("No location found with these address.", mContext);
                }
            } catch (Exception e) {
                // bottomSheetAddress.setText("Not Available.");
                e.printStackTrace();
            }
            lat_drop = String.valueOf(place.getLatLng().latitude);
            long_drop = String.valueOf(place.getLatLng().longitude);

            new_lat_pickup = lat_pickup;
            new_long_pick = long_pick;
            new_lat_drop = lat_drop;
            new_long_drop = long_drop;
            Utils.hideSoftKeyboard(getActivity());

            if ((!new_lat_pickup.trim().isEmpty() && !new_long_pick.trim().isEmpty()) &&
                    (!new_lat_drop.trim().isEmpty() && !new_long_drop.trim().isEmpty())) {
                String distanceUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                        lat_pickup + "," + long_pick + "&destinations=" + lat_drop + "," + long_drop +
                        "&mode=driving&sensor=false&key=" + googleApiKey;

                new GetDistanceTimeAsyncTask(getActivity(), distanceUrl, listners).execute();
            }

            //https://maps.googleapis.com/maps/api/distancematrix/json?origins=30.7138403,76.699212
            // &destinations=30.685086999999992,76.741748&mode=driving&sensor=false&key=
            // AIzaSyDac9yBJCmt8Tyry6JJ6GOvNQPx0_j76-o

           *//* mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null)
            {
                mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }*//*
        }
    };*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerBooking) {
            mListener = (OnFragmentInteractionListenerBooking) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (dropLoc != null)
            dropLoc.setText("");

        Navigation naviAct = (Navigation) getActivity();
        if (naviAct != null) {
            naviAct.checkBottomTabsVisibility(true);
        }
    }

    private void setVisibility(final boolean isBookVisible) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    //btnEstimatePrice.setVisibility(View.GONE);

                    Log.e(TAG, "NavigationBookNowBack " + Navigation.BookNowBack);

//                    if(buttonHide == false){
//                        btnEstimatePrice.setVisibility(View.VISIBLE);
//                    }else{
//                        btnEstimatePrice.setVisibility(View.GONE);
//                    }

                    etNote.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                    pickupLoc.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                    dropLoc.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                    llDateTime.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
//                etBookingType.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);

                    //if()

                    Log.e(TAG, "isBookVisibleAA " + isBookVisible);

                    btnSubmit.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                    viewShow.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                    tvAddStopsText.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                    no_stops.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                    rv_loc.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                    car_ManualT.setVisibility(isBookVisible ? View.GONE : View.VISIBLE);
                    close11.setVisibility(View.GONE);
                    close22.setVisibility(View.GONE);

                    btnPromoCode.setVisibility(isBookVisible ? View.GONE : View.GONE);
                    llPromo.setVisibility(isBookVisible ? View.VISIBLE : View.GONE);
                    // btnEstimatePrice.setVisibility(isBookVisible ? View.VISIBLE : View.VISIBLE);
                    //rlChooseCard.setVisibility(isBookVisible ? View.VISIBLE : View.GONE);
                    btnBookNow.setVisibility(isBookVisible ? View.VISIBLE : View.GONE);
                    //  cardPaymentView.setVisibility(isBookVisible ? View.VISIBLE : View.GONE);

                    rv_locCard.setVisibility(isBookVisible ? View.VISIBLE : View.GONE);
                    aSwitch.setVisibility(isBookVisible ? View.VISIBLE : View.GONE);

                    relativeLayoutCardContainer.setVisibility(View.GONE);
                    aSwitch.setSelected(false);

                } catch (Exception e) {

                }

            }
        }, 1 * 200);

    }

    public void checkVisibility() {
        if (btnSubmit.getVisibility() == View.VISIBLE) {
            Navigation.BookNowBack = true;
            //setVisibility(false);
            Log.e(TAG, "checkVisibility AAAA");
        } else {
            Navigation.BookNowBack = false;
            buttonHide = false;
            setVisibility(false);
            Log.e(TAG, "checkVisibility BBBB");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (status_BookingType.equalsIgnoreCase("2") && date.getText().toString().trim().isEmpty()) {
                    Utils.toastTxt("Please select date.", mContext);
                } else if (status_BookingType.equalsIgnoreCase("2") && time.getText().toString().trim().isEmpty()) {
                    Utils.toastTxt("Please select time.", mContext);
                }


//                else if(){
//                    if(dropBoolean == true){
//                        if(dropText != null) {
//                            Log.e(TAG, "dropText11111 " + dropText);
//                        }
//                    }else{
//                        if(dropLoc.getText().toString().equalsIgnoreCase("")){
//                            Log.e(TAG ,  "dropLoc33333 "+dropLoc.getText().toString());
//                        }else{
//                            Log.e(TAG ,  "dropLoc444444 "+dropLoc.getText().toString());
//                        }
//                    }
//                }

//                else if (lat_pickup.trim().isEmpty() && long_pick.trim().isEmpty()) {
//                    Utils.toastTxt("Please enter correct pickup location.", mContext);
//                } else if (lat_drop.trim().isEmpty() && long_drop.trim().isEmpty()) {
//                    Utils.toastTxt("Please enter correct drop location.", mContext);
//                }


                else if (pickupLoc.getText().toString().trim().isEmpty()) {
                    Utils.toastTxt("Please enter pickup location.", mContext);
                } else if (dropLoc.getText().toString().trim().isEmpty()) {
                    Utils.toastTxt("Please enter drop location.", mContext);
                } else {
                    buttonHide = true;
                    setVisibility(true);
                }

                break;

            case R.id.submit:
                submit();
                break;

            case R.id.price:
                estimatedRideCostRequest();
                break;

            case R.id.nstops:
                pickStops();
                break;

            case R.id.btype:
                //pickBookingType();
                break;

            case R.id.date:
                showDatePickerDialog();
                break;

            case R.id.time:
                showTimePickerDialog();
                break;

            case R.id.car_ml:
                if (car_ManualT.isChecked()) {
                    isCarML = "manual";
                } else {
                    isCarML = "automatic";
                }
                break;

            case R.id.choose:
                Intent i = new Intent(mContext, DropAddressList.class);
                startActivityForResult(i, 1);
                break;

            case R.id.card_choose:
                Intent j = new Intent(mContext, CardsList.class);
                startActivityForResult(j, 2);
                break;

            case R.id.close11:
                pickupLoc.setText("");
                close11.setVisibility(View.GONE);
                lat_pickup = "";
                long_pick = "";
                break;

            case R.id.close22:
                dropLoc.setText("");
                close22.setVisibility(View.GONE);
                lat_drop = "";
                long_drop = "";
                break;

            case R.id.btn_apply_promo:
                showPromoDialog();
                break;
        }
    }

    void pickStops() {

        Intent i = new Intent(mContext, AddStops.class);

        if (stopsAddList != null)
            i.putExtra("map", (Serializable) stopsAddList);

        startActivityForResult(i, 3);
    }

    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

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

            dateCalendar = day;
            calTime = getTimeInMillis(day, month, year);
            time.setText("");
            //LocalTime time = LocalTime.now();

            Log.e("GetDate", "MinDate " + dateCalendar + "  " + calTime);
            date.setText(mm + "-" + dd + "-" + yy);


            calendarSelected = Calendar.getInstance();


            selectedYY = Integer.valueOf(yy);
            selectedMM = Integer.valueOf(mm) - 1;
            selectedDD = Integer.valueOf(dd);

            calendarSelected.set(Calendar.AM_PM, Calendar.AM);


            calendarSelected.set(selectedYY, selectedMM, selectedDD);


        }
    }

    @SuppressLint("ValidFragment")
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            second = calendar.get(Calendar.SECOND);


            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));

            // Create a new instance of TimePickerDialog and return it
            return timePickerDialog;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int selectedMinute) {
            String hh = "";
            String mm = "";
            Calendar calendar = Calendar.getInstance();
            Calendar datetime = Calendar.getInstance();
            Log.e("GetDate", "MinDate1 " + calendar.getTime().getDate() + "  " + calendar.getTimeInMillis());


            if (dateCalendar == 0) {
                //Toast.makeText(getActivity(), "Please select date first", Toast.LENGTH_LONG).show();
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                builder.setTitle(getActivity().getString(R.string.app_name));
                builder.setMessage("Please select date first.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                time.setText("");
                return;
            }


            calendarCurrent = Calendar.getInstance();
            calendarCurrent.set(Calendar.SECOND, 0);

            calendarSelected = Calendar.getInstance();
            calendarSelected.set(selectedYY, selectedMM, selectedDD);

            calendarSelected.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendarSelected.set(Calendar.MINUTE, selectedMinute);
            calendarSelected.set(Calendar.SECOND, 0);


            Log.e(TAG, "calendarSelected1 " + calendarSelected.getTime());
            Log.e(TAG, "calendarSelectedgetTimeInMillis " + calendarSelected.getTimeInMillis());


            Log.e(TAG, "calendarCurrent1 " + calendarCurrent.getTime());
            Log.e(TAG, "calendarCurrentgetTimeInMillis " + calendarCurrent.getTimeInMillis());


            if (calendarSelected.getTimeInMillis() >= calendarCurrent.getTimeInMillis()) {
                Log.e(TAG, "AAAAAAAAAAAAA ");
            } else {
                Log.e(TAG, "BBBBBBBBBBBBB ");
                // Toast.makeText(getActivity(), "Invalid Time! Please Select greater than current time.", Toast.LENGTH_LONG).show();
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                builder.setTitle(getActivity().getString(R.string.app_name));
//                builder.setMessage("To select this time please select future date.")
                builder.setMessage("Please select a date in the future.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.show();


                time.setText("");
                return;
            }


//            if ((dateCalendar == calendar.getTime().getDate()) && (hourOfDay >= hour && selectedMinute >= minute)) {// && (calendar.getTimeInMillis() >= datetime.getTimeInMillis())
//
//            } else if (dateCalendar > calendar.getTime().getDate()) {
//                // int hour = hourOfDay % 12;
//            } else {
//                Toast.makeText(getActivity(), "Invalid Time! Please Select greater than current time.", Toast.LENGTH_LONG).show();
//                time.setText("");
//                return;
//            }

            if (hourOfDay < 10)
                hh = "0" + String.valueOf(hourOfDay);
            else
                hh = String.valueOf(hourOfDay);

            if (selectedMinute < 10)
                mm = "0" + String.valueOf(selectedMinute);
            else
                mm = String.valueOf(selectedMinute);

            String time_24 = "";
            time_24 = hh + ":" + mm;


            Log.e(TAG, hh + " HHMM " + mm);

            try {
                String _24HourTime = time_24;
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                Date _24HourDt = _24HourSDF.parse(_24HourTime);
//
//                String amPm = calendar.get(Calendar.HOUR)
//                        + ((calendar.get(Calendar.AM_PM) == Calendar.AM) ? "am" : "pm");
                //System.out.println(_24HourDt);
                //System.out.println(_12HourSDF.format(_24HourDt));

                time.setText(_12HourSDF.format(_24HourDt));

                Log.e(TAG, "_12HourSDF.format(_24HourDt) " + _12HourSDF.format(_24HourDt));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static long getTimeInMillis(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    public String getTimeStamp(long timeinMillies) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // modify format
        date = formatter.format(new Date(timeinMillies));
        System.out.println("Today is " + date);

        return date;
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
        Log.e(TAG,"ConnectionResult: "+connectionResult);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (Utils.global.mGoogleApiClient_pickup != null) {
            if (Utils.global.mGoogleApiClient_pickup.isConnected()) {
                Utils.global.mGoogleApiClient_pickup.stopAutoManage(getActivity());
                Utils.global.mGoogleApiClient_pickup.disconnect();
            }
        }

        if (Utils.global.mGoogleApiClient_drop != null) {
            if (Utils.global.mGoogleApiClient_drop.isConnected()) {
                Utils.global.mGoogleApiClient_drop.stopAutoManage(getActivity());
                Utils.global.mGoogleApiClient_drop.disconnect();
            }
        }
    }

    public void submit() {
        if (car_ManualT.isChecked()) {
            isCarML = "manual";
        } else {
            isCarML = "automatic";
        }

        if (status_BookingType.equalsIgnoreCase("1")) {
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat df1 = new SimpleDateFormat("hh:mm a");

            date_ride = df.format(c);
            time_ride = df1.format(c);

            Log.e("date 131233=====", date_ride);
            Log.e("Time 131233=====", time_ride);
        } else {
            date_ride = date.getText().toString();
            time_ride = time.getText().toString();
        }


        calendarCurrent = Calendar.getInstance();
        calendarCurrent.set(Calendar.SECOND, 0);


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy hh:mm a");
            Date date = sdf.parse(date_ride + " " + time_ride);

            long dateXX = date.getTime();

            Log.e(TAG, "AAAAAAAAAAAAA " + dateXX);


//            calendarSelected = Calendar.getInstance();
//            calendarSelected.set(selectedYY, selectedMM, selectedDD);
//
//            calendarSelected.set(Calendar.HOUR_OF_DAY, hourOfDay);
//            calendarSelected.set(Calendar.MINUTE, selectedMinute);
//            calendarSelected.set(Calendar.SECOND, 0);


            Date dateCur = calendarCurrent.getTime();
//            SimpleDateFormat dateFormatCur = new SimpleDateFormat("M-dd-yyyy hh:mm a");
            String strDate = sdf.format(dateCur);
            System.out.println("Converted String: " + strDate);


            //  SimpleDateFormat sdfCu = new SimpleDateFormat("M-dd-yyyy hh:mm a");
            Date dateCu = sdf.parse(strDate);


            long dateCurLong = dateCu.getTime();
            Log.e(TAG, "BBBBBBBBBBBBB " + dateCurLong);


            if (dateXX >= dateCurLong) {
                Log.e(TAG, "AAAAAAAAAAAAA ");
            } else {
                Log.e(TAG, "BBBBBBBBBBBBB ");
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                builder.setTitle(getActivity().getString(R.string.app_name));
                builder.setMessage("Please select a date in future.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Navigation.Instance.onBackPressed();
                            }
                        });
                builder.show();
                return;
            }


//
//            System.out.println(dateInString);
//            System.out.println("Date - Time in milliseconds : " + date.getTime());
        } catch (Exception e) {

        }


        new Utils(mContext);
        if (status_BookingType.equalsIgnoreCase("2") && date.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please select date.", mContext);
        } else if (status_BookingType.equalsIgnoreCase("2") && time.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please select time.", mContext);
        } else if (pickupLoc.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter pickup location.", mContext);
        } else if (dropLoc.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter drop location.", mContext);
        } else if (card_numberSelect.isEmpty()) {
            Utils.toastTxt("Please choose a credit card to continue.", mContext);
        } else {
            Log.e(TAG, "card_numberSelect: " + card_numberSelect);

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
            pref1.SavePref(mContext);
            String id = pref1.getUserId();

            try {
                json.put("userid", id);
                Log.e("Tagggg", payment_Id);
                json.put("nstops", no_stops.getText().toString());
                json.put("date", date_ride);
                json.put("time", time_ride);
                json.put("booking_type", status_BookingType);


                Log.e(TAG, "date_ride " + date_ride);
                Log.e(TAG, "time_ride " + time_ride);


                if (pickupBoolean == true) {
                    if (pickUpText != null) {
                        Log.e(TAG, "dropText11111 " + pickUpText);
                        json.put("pickup_address", pickUpText);
                    }
                } else {
                    if (!pickupLoc.getText().toString().equalsIgnoreCase("")) {
                        Log.e(TAG, "dropLoc33333 " + pickupLoc.getText().toString());
                        LatLng latLng = Utils.getLocationFromAddress(getActivity(), pickupLoc.getText().toString());

                        Log.e(TAG, "dropLoclatLng33333 " + latLng);
                        if (latLng != null) {
                            lat_pickup = latLng.latitude + "";
                            long_pick = latLng.longitude + "";
                        }

                        pickUpCityName = pickupLoc.getText().toString();
                        json.put("pickup_address", pickupLoc.getText().toString());
                    }
                }


                if (dropBoolean == true) {
                    if (dropText != null) {
                        Log.e(TAG, "dropText11111 " + dropText);
                        json.put("drop_address", dropText);
                    }
                } else {
                    if (!dropLoc.getText().toString().equalsIgnoreCase("")) {
                        Log.e(TAG, "dropLoc33333 " + dropLoc.getText().toString());
                        LatLng latLng = Utils.getLocationFromAddress2(getActivity(), dropLoc.getText().toString());

                        Log.e(TAG, "dropLoclatLng33333 " + latLng);
                        if (latLng != null) {
                            lat_drop = latLng.latitude + "";
                            long_drop = latLng.longitude + "";
                        }

                        dropCityName = dropLoc.getText().toString();
                        json.put("drop_address", dropLoc.getText().toString());
                    }
                }


                json.put("notes", etNote.getText().toString());

                json.put("platitude", lat_pickup);
                json.put("plongitude", long_pick);
                json.put("dlatitude", lat_drop);
                json.put("dlongitude", long_drop);

                json.put("transmission", isCarML);
                json.put("card_id", card_numberSelect);
                json.put("acctid", card_numberId);


                json.put("promo", promoCode);
                json.put("pickup_city", pickUpCityName);
                json.put("drop_city", dropCityName);
                json.put("version", "yes");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            map = new HashMap<>();
            map.put("json", json);
            map.put("jsonstops", jsonStops);


            try {
                SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy hh:mm a");
                Date date = sdf.parse(date_ride + " " + time_ride);

                long dateXX = date.getTime();

                Log.e(TAG, "FFFFFFFFF " + dateXX);

                String ffg = convertDate("" + dateXX, "EEEE dd MMMM yyyy hh:mm a");
                Log.e(TAG, "FFFFFFFFFXX " + ffg);


                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                builder.setTitle(getActivity().getString(R.string.app_name));
                builder.setMessage("Are you sure to book this ride for " + ffg + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                OnlineRequest.bookingRequest(mContext, map);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            } catch (Exception e) {

            }


            Log.e("Tagggg", map.toString());
        }
    }

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    public void estimatedRideCostRequest() {
        //Utils.startActivity(ActivityLogin.this,ActivityEvents.class);


        if (!pickupLoc.getText().toString().equalsIgnoreCase("")) {
            Log.e(TAG, "dropLoc33333 " + pickupLoc.getText().toString());
            LatLng latLng = Utils.getLocationFromAddress2(getActivity(), pickupLoc.getText().toString());

            Log.e(TAG, "dropLoclatLng33333 " + latLng);
            if (latLng != null) {
                lat_pickup = latLng.latitude + "";
                long_pick = latLng.longitude + "";
            }

        }

        if (!dropLoc.getText().toString().equalsIgnoreCase("")) {
            Log.e(TAG, "dropLoc33333 " + dropLoc.getText().toString());
            LatLng latLng = Utils.getLocationFromAddress2(getActivity(), dropLoc.getText().toString());

            Log.e(TAG, "dropLoclatLng33333 " + latLng);
            if (latLng != null) {
                lat_drop = latLng.latitude + "";
                long_drop = latLng.longitude + "";
            }

        }


        if (lat_pickup.toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter pickup location.", mContext);
        } else if (lat_drop.toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter destination location.", mContext);
        } else {


            date_ride = date.getText().toString();
            time_ride = time.getText().toString();


            if (date_ride.toString().trim().isEmpty()) {
                Utils.toastTxt("Please select date.", mContext);
                return;
            }

            if (time_ride.toString().trim().isEmpty()) {
                Utils.toastTxt("Please select time.", mContext);
                return;
            }

            Log.e("date 131233=====", date_ride);
            Log.e("Time 131233=====", time_ride);
            map = new HashMap<String, Object>();
            map.put("date", "" + date_ride);
            map.put("time", "" + time_ride);
            map.put("pick_lat", lat_pickup);
            map.put("pick_lng", long_pick);
            map.put("dest_lat", lat_drop);
            map.put("dest_lng", long_drop);
            map.put("promo", promoCode);
            map.put("count", stopsAddList != null ? stopsAddList.size() : 0);

            Log.e(TAG, "PPPPPPPPPP " + map.toString());

            OnlineRequest.estimatedPriceRequest(mContext, map);
        }
    }

    public void displayReceivedData(HashMap<String, Object> smap) {
        //txtData.setText("Data received: "+message);

        Utils.e("data123", smap.toString());
        dataMap = smap;

        if (dataMap != null && dataMap.size() > 0) {
            if (dataMap.containsKey("one") && !dataMap.get("one").toString().equalsIgnoreCase("")) {
                pickupLoc.setText(dataMap.get("one").toString());
                pickUpText = pickupLoc.getText().toString();
            }
            if (dataMap.containsKey("two") && !dataMap.get("two").toString().equalsIgnoreCase("")) {
                lat_pickup = dataMap.get("two").toString();
            }

            if (dataMap.containsKey("three") && !dataMap.get("three").toString().equalsIgnoreCase("")) {
                long_pick = dataMap.get("three").toString();
            }
            dropLoc.setText("");
            etNote.setText("");
            date.setText("");
            time.setText("");
            if (stopsAddList != null) {
                stopsAddList.clear();
            }
            Utils.e(Utils.Tag, dataMap.get("city_name").toString() + "  ");
            if (dataMap.get("city_name").toString() != null && dataMap.get("city_name").toString().length() > 0)
                pickUpCityName = dataMap.get("city_name").toString();
            //etBookingType.setText("AS SOON AS POSSIBLE");

            pickupBoolean = true;
        }
    }

    public void updateDropAddressList(List<HashMap<String, Object>> list) {
        List<HashMap<String, Object>> list1 = new ArrayList<>();
        HashMap<String, Object> dmap;

        for (int i = 0; i < list.size(); i++) {
            dmap = new HashMap<>();

            for (Map.Entry<String, Object> entry : list.get(i).entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (!value.toString().equalsIgnoreCase("null")) {
                    dmap.put(key, value);
                }
            }
            list1.add(dmap);
        }

        Intent i = new Intent(getActivity(), DropAddressList.class);
        i.putExtra("stopsList", (Serializable) list1);
        startActivityForResult(i, 1);
    }

    public static void getBookingResponce() {
        Log.e(TAG, "getBookingResponce");
        mListener.onFragmentInteraction(ConstVariable.ChangePassword);
    }

    public void showPromoDialog() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog_apply_promo, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
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
                    OnlineRequest.applyPromo(mContext, map);
                } else {
                    Utils.toastTxt("Please Enter Promo Code", mContext);
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

    public void showEstimatedPriceDialog(final HashMap<String, Object> data) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog8, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView distance = (TextView) dialog.findViewById(R.id.distance);
        TextView time = (TextView) dialog.findViewById(R.id.time);
        TextView price = (TextView) dialog.findViewById(R.id.price);
        Button ok = (Button) dialog.findViewById(R.id.ok);

        title.setText(R.string.app_name);

        if (data != null && data.size() > 0) {
            if (data.containsKey("distance") && !data.get("distance").toString().equalsIgnoreCase("")) {
                distance.setText(String.format("%.2f", Double.valueOf(data.get("distance").toString())) + " mi");
            }

            if (data.containsKey("estimate_time") && !data.get("estimate_time").toString().equalsIgnoreCase("null")) {
                time.setText(data.get("estimate_time").toString());

                if (data.containsKey("estimate_price") && !data.get("estimate_price").toString().equalsIgnoreCase("")) {
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

    public void showBookingSuccessDialog(final String message) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog8, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();


        final LinearLayout llDetails = dialog.findViewById(R.id.imagetxtview);
        final TextView bookingMsg = dialog.findViewById(R.id.tv_booking_msg);
        bookingMsg.setVisibility(View.VISIBLE);

        if (message.contains("Congratulations! your reservation has been completed")) {
            bookingMsg.setText(message);

            TextView tvPont1 = dialog.findViewById(R.id.tv_point_1);
            tvPont1.setVisibility(View.VISIBLE);
            tvPont1.setText("♦ Reservation is subject to our availability.");

            TextView tvPont2 = dialog.findViewById(R.id.tv_point_2);
            tvPont2.setVisibility(View.VISIBLE);
            tvPont2.setText("♦ Wait time will be charged $10 for every 15 minutes the driver is waiting.");

            TextView tvPont3 = dialog.findViewById(R.id.tv_point_3);
            tvPont3.setVisibility(View.VISIBLE);
            tvPont3.setText("♦ Any unplanned additional stop will be charged $10.");

            TextView tvPont4 = dialog.findViewById(R.id.tv_point_4);
            tvPont4.setVisibility(View.VISIBLE);
            tvPont4.setText("♦ Price does not include gratuity.");

            TextView tvPont5 = dialog.findViewById(R.id.tv_point_5);
            tvPont5.setVisibility(View.VISIBLE);
            tvPont5.setText("♦ 4 Hour Cancellation Policy from the time of pickup.");
        } else if (message.contains("It looks you are using older version")) {
            bookingMsg.setText(message);
        } else if (message.equalsIgnoreCase("Service not allowed") || message.equalsIgnoreCase("Bad card check digit")) {
            bookingMsg.setText("Invalid Card");
        } else {
            bookingMsg.setText(message);
        }

        final TextView title = dialog.findViewById(R.id.title);
        final Button ok = dialog.findViewById(R.id.ok);
        llDetails.setVisibility(View.GONE);

        title.setText(R.string.app_name);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (message.contains("It looks you are using older version")) {
                        final String appPackageName = getActivity().getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        }
                    } else if (message.equalsIgnoreCase("Service not allowed") || message.equalsIgnoreCase("Bad card check digit")) {

                    } else {

                        if (bookingAsap) {

                            Log.e(TAG, "AAAAAAAAAAAAA");

                            BookReservation_new.getBookingResponce();
//                        Home.Instance.updateRideStatus(Utils.global.mapMain);
                            Navigation.Instance.getFragment(1);
                        }
                    }


                    Log.e(TAG, "BBBBBBBBBBBBBB");

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
        requestsAdapter = new StopsAddressAdapter(mContext, requestsList, rv_loc, R.layout.stop_rowitem, ConstVariable.StopLocations);
        //set the adapter object to the Recyclerview
        //Utils.e(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_loc.setAdapter(requestsAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void getDistanceTime(String result) {
        Utils.e("TAGGG", result);
        new_lat_pickup = "";
        new_long_pick = "";
        new_lat_drop = "";
        new_long_drop = "";
    }

}