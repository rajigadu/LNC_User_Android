package com.latenightchauffeurs.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.*;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.AppManager;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.IonAppListners;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.Settings;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.activity.ActivityChat;
import com.latenightchauffeurs.activity.CancelRide;
import com.latenightchauffeurs.activity.Navigation;
import com.latenightchauffeurs.activity.Rating;
import com.latenightchauffeurs.adapter.PlaceArrayAdapter;
import com.latenightchauffeurs.model.DataParser;
import com.latenightchauffeurs.model.GPSTracker;
import com.latenightchauffeurs.model.SavePref;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Home extends Fragment implements OnMapReadyCallback, View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, IonAppListners {
    SendData sd;

    private Unbinder unbinder;

    @BindView(R.id.bookreserve)
    Button bookReservation;

    @BindView(R.id.pickup)
    AutoCompleteTextView pickup;

    // @BindView(R.id.card_pick)
    // CardView card_pick;

    @BindView(R.id.close11)
    ImageView close11;

    @BindView(R.id.driverimage)
    ImageView driverimage;

    @BindView(R.id.dname)
    TextView dname;

    @BindView(R.id.ridestatus)
    TextView ride_Status;

    @BindView(R.id.price)
    TextView price;

    @BindView(R.id.distance)
    TextView distance;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.rating)
    RatingBar rating;

    @BindView(R.id.dnumber)
    TextView dnumber;

    @BindView(R.id.cancel)
    Button cancel;

    @BindView(R.id.partner)
    Button partner;

    @BindView(R.id.call)
    ImageView call;

    @BindView(R.id.msg)
    ImageView msg;

    @BindView(R.id.newchatmsg)
    TextView ischatmsg;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static String TAG = Home.class.getName();
    public static Context mcontext;
    public static Home Instance;
    private GoogleMap mMap;
    GPSTracker gps;
    public static int i = 0;
    public Marker uMarker = null, dMarker;
    private static OnFragmentInteractionListenerHome mListener;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int RC_CALL_PERM = 101;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    public static String lat = "", lng = "", plocation = "", street_one = "", street_two = "", city = "", state = "", zip = "";
    private Marker mMarker;
    ArrayList<LatLng> sPoint;
    HashMap<String, Object> map, curentMdata;
    Polyline polylineFinal = null;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    ProgressDialog progressDialog;

    @BindView(R.id.bottom_sheet)
    RelativeLayout layoutBottomSheet;

    BottomSheetBehavior sheetBehavior;
    CardView layout_bottom;
    public static CountDownTimer mtimer_autocancel;
    public static CountDownTimer mtimer;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private int count = 0;

    @Override
    public void isAppOpen() {

    }

    public interface SendData {
        void sendData(HashMap<String, Object> smap);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_reservation, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        unbinder = ButterKnife.bind(this, v);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        layout_bottom = v.findViewById(R.id.card_driver);
        close11.setVisibility(View.GONE);
        layout_bottom.setVisibility(View.GONE);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        bookReservation.setOnClickListener(this);
        close11.setOnClickListener(this);
        partner.setOnClickListener(this);
        cancel.setOnClickListener(this);
        call.setOnClickListener(this);
        msg.setOnClickListener(this);

        mcontext = getActivity();
        Instance = this;

        sPoint = new ArrayList<>();

        pickup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    close11.setVisibility(View.VISIBLE);
                } else {
                    close11.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }

               /* switch (newState)
                {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                       *//* {
                        btnBottomSheet.setText("Close Sheet");
                    }*//*
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                       *//* {
                        btnBottomSheet.setText("Expand Sheet");
                    }*//*
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    }*/
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        sheetBehavior.setPeekHeight(0);

        if (Utils.global.mGoogleApiClient_1 == null) {
            try {
                Utils.global.mGoogleApiClient_1 = new GoogleApiClient.Builder(getActivity())
                        .addApi(Places.GEO_DATA_API)
                        .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                        .addConnectionCallbacks(this)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        pickup.setThreshold(3);
        pickup.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(mcontext, android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        pickup.setAdapter(mPlaceArrayAdapter);

        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setCancelable(false);

        getCurrentRideRequests();

        mtimer_autocancel = new CountDownTimer(20000, 1000) {
            public void onTick(long millisUntilFinished) {
                // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                try {
                    RideAutoCancelRequest(mcontext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mtimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        if (ActivityCompat.checkSelfPermission(mcontext, mPermission) != PackageManager.PERMISSION_GRANTED) {

                        } else {
                            gps = new GPSTracker(mcontext);

                            if (gps.canGetLocation()) {
                                // LatLng latLng = new LatLng(gps.getLatitude(),gps.getLongitude());
                                DriverLocationUpdateRequest();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    gps = new GPSTracker(mcontext);

                    if (gps.canGetLocation()) {
                        // LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
                        DriverLocationUpdateRequest();
                    }
                }
                if (mtimer != null)
                    mtimer.start();
            }
        };
    }

    public static void DriverLocationUpdateRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        // String tId=pref1.getdToken();
        String id = pref1.getDriverId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put(ConstVariable.DRIVERID, id);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_DRIVER_LOCATION_UPDATE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.DriverLocationUpdate);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void RideAutoCancelRequest(Context mcontext) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();
        String r_id = pref1.getautoRideId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("ride_id", r_id);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_RIDE_AUTO_CANCEL);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.RideAutoCancel);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            //Log.i(LOG_TAG, "Selected==============================: " + item.description);

            String[] items = item.toString().split(",");
            String value = items[0];

            pickup.post(new Runnable() {
                public void run() {
                    pickup.dismissDropDown();
                }
            });

            close11.setVisibility(View.GONE);
            //pickup.setText(String.valueOf(item.description));
            pickup.setText(value);
            pickup.setSelection(pickup.getText().length());
            plocation = pickup.getText().toString();

            if (!pickup.getText().toString().equalsIgnoreCase(""))
                street_one = pickup.getText().toString();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(Utils.global.mGoogleApiClient_1, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                return;
            }
            final Place place = places.get(0);
            Geocoder geocoder = new Geocoder(mcontext, Locale.getDefault());
            try {
                if (!String.valueOf(place.getLatLng().latitude).toString().equalsIgnoreCase("")) {
                    //Log.i("returmdata123", "Fetching details for ID:" + String.valueOf(place.getLatLng().latitude));

                    lat = String.valueOf(place.getLatLng().latitude);
                    lng = String.valueOf(place.getLatLng().longitude);

                    List<android.location.Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);

                    if (addresses != null && addresses.size() > 0) {
                        if (addresses.get(0).getAddressLine(0) != null) {
                            String fulladdress = addresses.get(0).getAddressLine(0).toString();
                            Log.d("fulladdress", fulladdress);
                            // city.setText(cityname);
                            street_two = fulladdress;
                        }
                        if (addresses.get(0).getLocality() != null) {
                            String cityname = addresses.get(0).getLocality();
                            //Log.d("city", cityname);
                            city = cityname;
                        }

                        if (addresses.get(0).getAdminArea() != null) {
                            String statename = addresses.get(0).getAdminArea();
                            // state.setText(statename);
                            //Log.d("state", statename);
                            state = statename;
                        }

                        if (addresses.get(0).getPostalCode() != null) {
                            String mzip = addresses.get(0).getPostalCode();
                            //Log.d("country", mcountry);
                            zip = mzip;
                        }

                        sPoint.clear();
                        sPoint.add(place.getLatLng());

                        LatLng latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                        uMarker.setPosition(latLng);
                        uMarker.setTitle("Current Location");
                        uMarker.showInfoWindow();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 14));

                        curentMdata = new HashMap<>();

                        if (!street_one.equalsIgnoreCase(""))
                            curentMdata.put("one", street_one);

                        if (!street_one.equalsIgnoreCase(""))
                            curentMdata.put("two", sPoint.get(0).latitude);

                        if (!city.equalsIgnoreCase(""))
                            curentMdata.put("three", sPoint.get(0).longitude);

                        if (!city.equalsIgnoreCase(""))
                            curentMdata.put("city_name", city);

                     /*   if (!state.equalsIgnoreCase(""))
                            curentMdata.put("four",state);

                        if (!zip.equalsIgnoreCase(""))
                            curentMdata.put("five",zip);

                        if (sPoint!=null&&sPoint.size()>0)
                            curentMdata.put("six",sPoint.get(0).latitude);

                        if (sPoint!=null&&sPoint.size()>0)
                            curentMdata.put("seven",sPoint.get(0).longitude);*/
                    } else {
                        new Utils(mcontext);
                        Utils.toastTxt("No address found,search again.", mcontext);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.isMyLocationEnabled();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        } else {
            gps = new GPSTracker(mcontext);

            if (gps.canGetLocation()) {
                LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("my Location");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                uMarker = mMap.addMarker(markerOptions);
                uMarker.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                UserLocationUpdateRequest(mcontext, String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));
                populateDefaultAddressFields(latLng);
            } else {
                gps.showSettingsAlert();
            }
        }
    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Utils.toastTxt("permissions granted",mContext);

            gps = new GPSTracker(mcontext);

            if (gps.canGetLocation()) {
                LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("my Location");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                uMarker = mMap.addMarker(markerOptions);
                uMarker.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

                UserLocationUpdateRequest(mcontext, String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));
                populateDefaultAddressFields(latLng);
            } else {
                gps.showSettingsAlert();
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(mcontext,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:
                        // Utils.toastTxt("permissions granted",mContext);

                        gps = new GPSTracker(mcontext);

                        if (gps.canGetLocation()) {
                            LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title("my Location");
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            uMarker = mMap.addMarker(markerOptions);
                            uMarker.showInfoWindow();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                            UserLocationUpdateRequest(mcontext, String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));
                            populateDefaultAddressFields(latLng);
                        } else {
                            gps.showSettingsAlert();
                        }
                    }
                    return;
                } else {
                    Utils.toastTxt("need permissions for location update", mcontext);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            break;
            case RC_CALL_PERM:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (requestCode == RC_CALL_PERM) {
                        SavePref pref1 = new SavePref();
                        pref1.SavePref(mcontext);
                        String cmap = pref1.getdrivermap();

                        if (!cmap.equalsIgnoreCase("")) {
                            Gson gson = new Gson();
                            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                            }.getType();
                            HashMap<String, Object> map = gson.fromJson(cmap, type);
                            getCall(map);
                        }
                    }
                }
                break;
        }
    }

    public void getCall(HashMap<String, Object> map) {
        if (map.containsKey("driverMobile") && !map.get("driverMobile").toString().equalsIgnoreCase("")) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", map.get("driverMobile").toString(), null)));
        } else if (map.containsKey("mobile") && !map.get("mobile").toString().equalsIgnoreCase("")) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", map.get("mobile").toString(), null)));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerHome) {
            mListener = (OnFragmentInteractionListenerHome) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        try {
            sd = (SendData) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        /*if (mGoogleApiClient != null)
            mGoogleApiClient.connect();*/
    }

    @Override
    public void onStop() {
        super.onStop();

        if (Utils.global.mGoogleApiClient_1 != null) {
            if (Utils.global.mGoogleApiClient_1.isConnected()) {
                Utils.global.mGoogleApiClient_1.stopAutoManage((getActivity()));
                Utils.global.mGoogleApiClient_1.disconnect();
            }
        }

        if (mtimer_autocancel != null)
            mtimer_autocancel.cancel();

        if (mtimer != null)
            mtimer.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bookreserve:
                pickup.setText("");
                lat = "";
                lng = "";
                sPoint.clear();
                try {
                    mListener.onFragmentInteraction(ConstVariable.BookReseration);
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                BookReservation_new.getBookingResponce();
//                        Home.Instance.updateRideStatus(Utils.global.mapMain);
//                Navigation.Instance.getFragment(1);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (curentMdata != null && curentMdata.size() > 0)
                            sd.sendData(curentMdata);
                    }
                }, 1000);
                Navigation naviAct = (Navigation) getActivity();
                if (naviAct != null) {
                    naviAct.checkBottomTabsVisibility(false);
                }


                break;
            case R.id.close11:
                pickup.setText("");
                street_one = "";
                street_two = "";
                city = "";
                state = "";
                zip = "";
                sPoint.clear();
                break;
            case R.id.cancel:
                SavePref pref2 = new SavePref();
                pref2.SavePref(mcontext);
                String rid = pref2.getRideId();

                if (!rid.isEmpty()) {
                    Intent i = new Intent(mcontext, CancelRide.class);
                    i.putExtra("rideid", rid);
                    startActivity(i);
                }
                break;
            case R.id.call:
                SavePref pref1 = new SavePref();
                pref1.SavePref(mcontext);
                String cmap = pref1.getdrivermap();

                if (!cmap.equalsIgnoreCase("")) {
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    HashMap<String, Object> map = gson.fromJson(cmap, type);
                    showMessage("Do You Want To Call?", map);
                }
                break;
            case R.id.msg:
                ischatmsg.setVisibility(View.GONE);
                Utils.startActivity(mcontext, ActivityChat.class);
                break;
            case R.id.partner:
                SavePref pref3 = new SavePref();
                pref3.SavePref(mcontext);
                String rmap = pref3.getdrivermap();

                if (!rmap.equalsIgnoreCase("")) {
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    HashMap<String, Object> map = gson.fromJson(rmap, type);
                    getPartnerDetails(map);
                }
                break;
        }
    }

    private void getPartnerDetails(HashMap<String, Object> pmap) {
        if (pmap.containsKey("rideid") && !pmap.get("rideid").toString().equalsIgnoreCase("")) {
            map = new HashMap<>();
            map.put("ride_id", pmap.get("rideid").toString());
            OnlineRequest.getPartnerDetailsRequest(mcontext, map);
        } else if (pmap.containsKey("id") && !pmap.get("id").toString().equalsIgnoreCase("")) {
            map = new HashMap<>();
            map.put("ride_id", pmap.get("id").toString());
            OnlineRequest.getPartnerDetailsRequest(mcontext, map);
        }
    }

    private void callPermission(HashMap<String, Object> map) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                getCall(map);
            } else {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, RC_CALL_PERM);
                // ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},RC_SMS_PERM);
            }
        } else {
            getCall(map);
        }
    }

    public void showMessage(String dlgText, final HashMap<String, Object> data) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog6, null);
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mcontext).create();
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
                    callPermission(data);
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

    public void showPartnerDetailsDilog(final HashMap<String, Object> data) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog7, null);
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        // final RelativeLayout rootlo = (RelativeLayout) dialog.findViewById(R.id.rootlo);
        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView textView = (TextView) dialog.findViewById(R.id.desc);
        final TextView name = (TextView) dialog.findViewById(R.id.name);
        final TextView number = (TextView) dialog.findViewById(R.id.number);
        final TextView email = (TextView) dialog.findViewById(R.id.email);
        final Button ok = (Button) dialog.findViewById(R.id.ok);

        title.setText(R.string.app_name);

        if (data != null && data.size() > 0) {
            if (data.containsKey("name") && !data.get("name").toString().equalsIgnoreCase("")) {
                name.setText(data.get("name").toString());
            }

            if (data.containsKey("phone") && !data.get("phone").toString().equalsIgnoreCase("")) {
                number.setText(data.get("phone").toString());
            }

            if (data.containsKey("email") && !data.get("email").toString().equalsIgnoreCase("")) {
                email.setText(data.get("email").toString());
            }
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    dialog.cancel();
                } catch (Exception e) {
                    // TODO: handle exception
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

    @Override
    public void onPause() {
        super.onPause();

        /*if (mGoogleApiClient!=null & mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(Utils.global.mGoogleApiClient_1);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public interface OnFragmentInteractionListenerHome {
        // TODO: Update argument type and name
        void onFragmentInteraction(int position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(mHandleMessageReceiver);
        mListener = null;
    }

    @Override
    public void onDestroyView() {
       /* Fragment parentFragment = getParentFragment();
        FragmentManager manager;
        if (parentFragment != null)
        {
            // If parent is another fragment, then this fragment is nested
            manager = parentFragment.getChildFragmentManager();
        } else {
            // This fragment is placed into activity
            manager = getActivity().getSupportFragmentManager();
        }
        manager.beginTransaction().remove(this).commitAllowingStateLoss();*/
        super.onDestroyView();
        unbinder.unbind();
    }

    public void UserLocationUpdateRequest(Context mcontext, String lat, String lng) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();

        map = new HashMap<>();
        map.put(ConstVariable.USERID, id);
        map.put("laitude", lat);
        map.put("longitude", lng);
        OnlineRequest.userLocationUpdateRequest(mcontext, map);
    }

    public void populateDefaultAddressFields(LatLng ll) {
        Geocoder geocoder = new Geocoder(mcontext, Locale.getDefault());
        try {
            if (!String.valueOf(ll.latitude).toString().equalsIgnoreCase("")) {
                //Log.i("returmdata123", "Fetching details for ID:" + String.valueOf(place.getLatLng().latitude));

                lat = String.valueOf(ll.latitude);
                lng = String.valueOf(ll.longitude);

                List<android.location.Address> addresses = geocoder.getFromLocation(ll.latitude, ll.longitude, 1);
                String cityname = "";
                if (addresses != null && addresses.size() > 0) {
                    if (addresses.get(0).getAddressLine(0) != null) {
                        String fulladdress = addresses.get(0).getAddressLine(0).toString();
                        Log.d("fulladdress", fulladdress);
                        // city.setText(cityname);
                        street_two = fulladdress;
                        street_one = fulladdress;
                    }

                    if (addresses.get(0).getLocality() != null) {
                        cityname = addresses.get(0).getLocality();
                        //Log.d("city", cityname);
                        city = cityname;
                    }

                    if (addresses.get(0).getAdminArea() != null) {
                        String statename = addresses.get(0).getAdminArea();
                        // state.setText(statename);
                        //Log.d("state", statename);
                        state = statename;
                    }

                    if (addresses.get(0).getPostalCode() != null) {
                        String mzip = addresses.get(0).getPostalCode();
                        //Log.d("country", mcountry);
                        zip = mzip;
                    }
                    sPoint.clear();
                    sPoint.add(ll);

                    LatLng latLng = new LatLng(ll.latitude, ll.longitude);
                    uMarker.setPosition(latLng);
                    uMarker.setTitle("Current Location");
                    uMarker.showInfoWindow();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 14));

                    curentMdata = new HashMap<>();

                       /* if (!street_one.toString().equalsIgnoreCase(""))
                            pickup.setText(street_one);*/

                    if (!street_one.toString().equalsIgnoreCase(""))
                        curentMdata.put("one", street_one);

                    if (sPoint != null && sPoint.size() > 0)
                        curentMdata.put("two", sPoint.get(0).latitude);

                    if (sPoint != null && sPoint.size() > 0)
                        curentMdata.put("three", sPoint.get(0).longitude);

                    if (!city.equalsIgnoreCase(""))
                        curentMdata.put("city_name", cityname);
                } else {
                    new Utils(mcontext);
                    Utils.toastTxt("No address found,search again.", mcontext);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // mGoogleApiClient.stopAutoManage((FragmentActivity) mContext);
        // mGoogleApiClient.disconnect();

    }

    @Override
    public void onResume() {
        super.onResume();
        AppManager.getInstance().setIonAppListners(this);
        getActivity().registerReceiver(mHandleMessageReceiver, new IntentFilter("OPEN_NEW_ACTIVITY"));
    }

    public final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String status = intent.getStringExtra("status");
                Utils.global.dmapMain();
                map = (HashMap<String, Object>) intent.getSerializableExtra("data");

                Log.e(TAG, "NotificationMessageBody: " + status + " \n " + map.toString());
                if (status.equalsIgnoreCase("5")) {
                    if (progressDialog != null)
                        progressDialog.cancel();

                    Utils.global.dmap = (HashMap<String, Object>) intent.getSerializableExtra("data");
                    // card_pick.setVisibility(View.GONE);
                    bookReservation.setVisibility(View.GONE);
                    layout_bottom.setVisibility(View.VISIBLE);

                   /* if (Utils.global.dmap.containsKey("photo")&&!Utils.global.dmap.get("photo").toString().equalsIgnoreCase(""))
                    {
                        Picasso.with(mContext).load(Utils.global.dmap.get("photo").toString()).placeholder(R.drawable.appicon).into(driverimage);
                        // userpic.setImageBitmap(getBitmapFromURL(image));
                    }

                    dname.setText(Utils.global.dmap.get("driverName").toString());
                    mnumber.setText(Utils.global.dmap.get("driverMobile").toString());

                    distance.setText(String.format("%.2f",Double.valueOf(Utils.global.dmap.get("arrival_distance").toString()))+" mi");

                    time.setText(Utils.global.dmap.get("arrival_time").toString());

                    SavePref pref1 = new SavePref();
                    pref1.SavePref(mContext);
                    pref1.setDriverId( Utils.global.dmap.get("driver_id").toString());
                    pref1.setfordriver("");

                    Gson gson = new Gson();
                    String hashMapString = gson.toJson( Utils.global.dmap);

                    pref1.setdrivermap(hashMapString);

                    mMap.clear();

                    LatLng origin=null,dest=null;

                    if(Utils.global.dmap.containsKey("picuplat")&&  !Utils.global.dmap.get("picuplat").toString().equalsIgnoreCase("null"))
                        origin=new LatLng(Double.valueOf(Utils.global.dmap.get("picuplat").toString()),Double.valueOf(Utils.global.dmap.get("pickuplng").toString()));

                    if(Utils.global.dmap.containsKey("driverLatitude")&&  !Utils.global.dmap.get("driverLatitude").toString().equalsIgnoreCase("null"))
                        dest=new LatLng(Double.valueOf(Utils.global.dmap.get("driverLatitude").toString()),Double.valueOf(Utils.global.dmap.get("driverLongitude").toString()));

                    MarkerOptions options = new MarkerOptions();
                    options.position(origin);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    mMap.addMarker(options);

                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.car38);
                    MarkerOptions options1 = new MarkerOptions();
                    options1.position(dest);
                    options1.icon(icon);
                    dMarker= mMap.addMarker(options1);
                    dMarker.setTitle(String.valueOf(Utils.global.dmap.get("arrival_time").toString()));
                    dMarker.showInfoWindow();

                    String url = getUrl(origin, dest);
                    FetchUrl FetchUrl = new FetchUrl();
                    FetchUrl.execute(url);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(origin);
                    builder.include(dest);

                    LatLngBounds bounds = builder.build();

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels;
                    int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                    mMap.animateCamera(cu);*/
                } else if (status.equalsIgnoreCase("9")) {
                    if (progressDialog != null)
                        progressDialog.cancel();

                    Utils.global.dmap = new HashMap<>();
                    Utils.global.dmap = (HashMap<String, Object>) intent.getSerializableExtra("data");

                    if (Utils.global.dmap.containsKey("ride") && Utils.global.dmap.get("ride").toString().
                            equalsIgnoreCase("future complete")) {
                        if (count == 0) {
                            Intent ii = new Intent(mcontext, Rating.class);
                            ii.putExtra("map", (Serializable) Utils.global.dmap);
                            getActivity().startActivity(ii);
                        }
                        count++;
                    }

                    if (Utils.global.dmap.containsKey("ride") && Utils.global.dmap.get("ride")
                            .toString().equalsIgnoreCase("newride")) {
                        ride_Status.setVisibility(View.GONE);
                        call.setClickable(true);
                        msg.setClickable(true);
                        cancel.setClickable(true);

                        bookReservation.setVisibility(View.GONE);
                        layout_bottom.setVisibility(View.VISIBLE);

                        if (Utils.global.dmap.get("photo") != "") {
                            if (Utils.global.dmap.containsKey("photo") && !Utils.global.dmap.get
                                    ("photo").toString().equalsIgnoreCase("")) {
                                Picasso.with(mcontext).load(Settings.URLIMAGEBASE +
                                        Utils.global.dmap.get("photo").toString()).placeholder
                                        (R.drawable.appicon).into(driverimage);
                                // userpic.setImageBitmap(getBitmapFromURL(image));
                            }
                        }

                        StringBuilder sb = new StringBuilder();

                        if (Utils.global.dmap.get("first_name") != "") {
                            if (Utils.global.dmap.containsKey("first_name") && !Utils.global.dmap.get("first_name").toString().equalsIgnoreCase("")) {
                                sb.append(Utils.global.dmap.get("first_name").toString());
                                if (mtimer_autocancel != null)
                                    mtimer_autocancel.cancel();
                            }
                        }

                        if (Utils.global.dmap.get("last_name") != "") {
                            if (Utils.global.dmap.containsKey("last_name") && !Utils.global.dmap.get("last_name").toString().equalsIgnoreCase("")) {
                                sb.append(" ");
                                sb.append(Utils.global.dmap.get("last_name").toString());
                                dname.setText(sb);
                            }
                        }

                        if (Utils.global.dmap.get("mobile") != "") {
                            if (Utils.global.dmap.containsKey("mobile") && !Utils.global.dmap.get("mobile").toString().equalsIgnoreCase("")) {
                                dnumber.setText(Utils.global.dmap.get(ConstVariable.MOBILE).toString());
                            }
                        }

                        if (Utils.global.dmap.get("driver_rating") != "") {
                            if (Utils.global.dmap.containsKey("driver_rating") && !Utils.global.dmap.get("driver_rating").toString().equalsIgnoreCase("")) {
                                rating.setRating(Float.valueOf(Utils.global.dmap.get("driver_rating").toString()));
                            }
                        }

                        if (Utils.global.dmap.get("distance") != "") {
                            if (Utils.global.dmap.containsKey("distance") && !Utils.global.dmap.get("distance").toString().equalsIgnoreCase("")) {
                                distance.setText(String.format("%.2f", Double.valueOf(Utils.global.dmap.get("distance").toString())) + " mi");
                            }
                        }

                        if (Utils.global.dmap.get("time") != "") {
                            if (Utils.global.dmap.containsKey("time") && !Utils.global.dmap.get("time").toString().equalsIgnoreCase("")) {
                                time.setText(Utils.global.dmap.get("time").toString());
                            }
                        }

                        if (Utils.global.dmap.get("estimate_price") != "") {
                            if (Utils.global.dmap.containsKey("estimate_price") && !Utils.global.dmap.get("estimate_price").toString().equalsIgnoreCase("")) {
                                price.setText("$" + Utils.global.dmap.get("estimate_price").toString());
                            }
                        }

                        SavePref pref1 = new SavePref();
                        pref1.SavePref(mcontext);
                        pref1.setDriverId(Utils.global.dmap.get("driverid").toString());
                        pref1.setisLocationUpdate("update");

                        Gson gson = new Gson();
                        String hashMapString = gson.toJson(Utils.global.dmap);
                        pref1.setdrivermap(hashMapString);
                        pref1.setRideId(Utils.global.dmap.get("rideid").toString());

                        mMap.clear();
                        LatLng origin = null, dest = null;

                        if (Utils.global.dmap.get("pickup_lat") != "") {
                            if (Utils.global.dmap.containsKey("pickup_lat") && !Utils.global.dmap.get("pickup_lat").toString().equalsIgnoreCase("")) {
                                origin = new LatLng(Double.parseDouble(Utils.global.dmap.get("pickup_lat").toString()), Double.parseDouble(Utils.global.dmap.get("pickup_long").toString()));
                            }

                            if (Utils.global.dmap.containsKey("lat") && !Utils.global.dmap.get("lat").toString().equalsIgnoreCase("")) {
                                dest = new LatLng(Double.parseDouble(Utils.global.dmap.get("lat").toString()), Double.parseDouble(Utils.global.dmap.get("lon").toString()));
                            }
                        }

                        if (origin != null && dest != null) {
                            MarkerOptions options = new MarkerOptions();
                            options.position(origin);
                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            mMap.addMarker(options);

                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.car38);
                            MarkerOptions options1 = new MarkerOptions();
                            options1.position(dest);
                            options1.icon(icon);
                            dMarker = mMap.addMarker(options1);

                            String url = getUrl(origin, dest);
                            FetchUrl FetchUrl = new FetchUrl();
                            FetchUrl.execute(url);

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(origin);
                            builder.include(dest);

                            LatLngBounds bounds = builder.build();

                            int width = getResources().getDisplayMetrics().widthPixels;
                            int height = getResources().getDisplayMetrics().heightPixels;
                            int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                            mMap.animateCamera(cu);

                            if (!pref1.getisLocationUpdate().equalsIgnoreCase("")) {
                                if (mtimer != null)
                                    mtimer.start();
                            }
                        }
                    } else if (Utils.global.dmap.containsKey("ride") && Utils.global.dmap.get("ride").toString().equalsIgnoreCase("Cancel")) {
                        SavePref pref1 = new SavePref();
                        pref1.SavePref(mcontext);

                        Gson gson = new Gson();
                        String hashMapString = gson.toJson(Utils.global.dmap);

                        if (progressDialog != null)
                            progressDialog.cancel();

                        Utils.global.dmap = new HashMap<>();
                        Utils.global.dmap = (HashMap<String, Object>) intent.getSerializableExtra("data");
                        layout_bottom.setVisibility(View.GONE);
                        call.setClickable(false);
                        msg.setClickable(false);
                        cancel.setClickable(false);

                        if (Utils.global.dmap != null && Utils.global.dmap.size() > 0) {
                            if (Utils.global.dmap.containsKey("message")) {
                                if (!Utils.global.dmap.get("message").toString().equalsIgnoreCase("") && !Utils.global.dmap.get("message").toString().equalsIgnoreCase("null")) {
                                    ride_Status.setVisibility(View.VISIBLE);
                                    ride_Status.setText(Utils.global.dmap.get("message").toString() + "  we are searching for other driver. please wait for a while.");
                                } else {
                                    ride_Status.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else if (Utils.global.dmap.containsKey("ride") && Utils.global.dmap.get("ride").toString().equalsIgnoreCase("complete")) {
                        updateui();
                        SavePref pref1 = new SavePref();
                        pref1.SavePref(mcontext);

                        if (pref1.getisLocationUpdate().equalsIgnoreCase("")) {
                            if (mtimer != null)
                                mtimer.cancel();
                        }
                        pref1.setisLocationUpdate("");

                        mMap.clear();

                        String location = pref1.getClocation();
                        String lat = pref1.getlocationlat();
                        String lng = pref1.getlocationlng();

                        if (!location.equalsIgnoreCase("")) {
                            LatLng latLng = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title(location);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            mMarker = mMap.addMarker(markerOptions);
                            mMarker.showInfoWindow();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                        }

                        //showFeedbackDialog(getActivity(),Utils.global.dmap);

                        //Gson gson = new Gson();
                        // String hashMapString = gson.toJson( Utils.global.dmap);

                        String ss = pref1.getdrivermap();

                        pref1.setRatingStatus("ok");
                        pref1.setdRatingmap(ss);

                        if (i == 0) {
                            Intent ii = new Intent(mcontext, Rating.class);
                            ii.putExtra("map", (Serializable) Utils.global.dmap);
                            getActivity().startActivity(ii);
                            i = 1;
                        }
                    } else if (Utils.global.dmap.containsKey("ride") && Utils.global.dmap.get("ride").toString().equalsIgnoreCase("nodriver")) {
                        if (mtimer_autocancel != null)
                            mtimer_autocancel.cancel();

                        SavePref pref1 = new SavePref();
                        pref1.SavePref(mcontext);

                        //card_pick.setVisibility(View.VISIBLE);
                        bookReservation.setVisibility(View.VISIBLE);
                        layout_bottom.setVisibility(View.GONE);
                        ride_Status.setVisibility(View.GONE);

                        if (Utils.global.dmap.get("message") != "") {
                            if (Utils.global.dmap.containsKey("message") && !Utils.global.dmap.get("message").toString().equalsIgnoreCase("")) {
                                Utils.toastTxt(Utils.global.dmap.get("message").toString(), mcontext);
                            }
                        }
                    }
                } else if (status.equalsIgnoreCase("6")) {
                    // card_pick.setVisibility(View.VISIBLE);
                    layout_bottom.setVisibility(View.GONE);

                    SavePref pref1 = new SavePref();
                    pref1.SavePref(mcontext);
                    pref1.setDriverId("");
                    pref1.setdrivermap("");
                } else if (status.equalsIgnoreCase("7")) {
                    Log.e("updatinggggg", "1223345666");

                    //mMap.clear();

                    LatLng dest = null;
                    String lat = intent.getStringExtra("lat");
                    String lon = intent.getStringExtra("lon");

                    String tim = "", dist = "", ride_status = "";

                    if (intent.hasExtra("eta_time"))
                        tim = intent.getStringExtra("eta_time");

                    if (intent.hasExtra("eta_distance"))
                        dist = intent.getStringExtra("eta_distance");

                    if (intent.hasExtra("start_ride_status"))
                        ride_status = intent.getStringExtra("start_ride_status");

                    dest = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));

                    if (!ride_status.equalsIgnoreCase("")) {
                        if (!tim.equalsIgnoreCase("")) {
                            time.setText(tim);
                            Utils.e("time test5345555----", tim);
                        }

                        if (!dist.equalsIgnoreCase("")) {
                            distance.setText(String.format("%.2f", Double.valueOf(dist)) + " mi");
                            Utils.e("time test5345555----", dist);
                        }
                    }

                    // dest = new LatLng(30.7055,76.8013);

                    if (dMarker != null)
                        dMarker.setPosition(dest);

                    if (mMarker != null && dMarker != null) {
                        String url = getUrl(mMarker.getPosition(), dMarker.getPosition());
                        FetchUrl FetchUrl = new FetchUrl();
                        FetchUrl.execute(url);
                    }
                }
                if (status.equalsIgnoreCase("9")) {
                    // String status = intent.getStringExtra("status");

                    HashMap<String, Object> data = new HashMap<>();
                    data = (HashMap<String, Object>) intent.getSerializableExtra("data");

                    if (data.containsKey("ride") && data.get("ride").toString().equalsIgnoreCase("newusermessage")) {
                        SavePref pref1 = new SavePref();
                        pref1.SavePref(mcontext);
                        pref1.setisnewmsg(data.get("ride").toString());

                        // DriverChat.driversChatListRequest();
                        String ss = pref1.getisnewmsg();

                        if (!ss.equalsIgnoreCase("")) {
                            ischatmsg.setVisibility(View.VISIBLE);
                        } else {
                            ischatmsg.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    };

    private void updateui() {
        // card_pick.setVisibility(View.VISIBLE);
        bookReservation.setVisibility(View.VISIBLE);
        layout_bottom.setVisibility(View.GONE);
    }

    public void updateRideStatus(HashMap<String, Object> map) {
        //card_pick.setVisibility(View.GONE);
        bookReservation.setVisibility(View.GONE);
        layout_bottom.setVisibility(View.GONE);

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);

        try {
            if (map != null && map.size() > 0) {
                if (map != null & map.containsKey("ride_id")) {
                    if (!map.get("ride_id").toString().equalsIgnoreCase(""))
                        pref1.setautoRideId(map.get("ride_id").toString());
                }
            }

            if (map != null & map.containsKey("ride") & map.get("ride").toString().equalsIgnoreCase("1")) {
                if (map.containsKey("msg") && !map.get("msg").toString().equalsIgnoreCase("")) {
                    progressDialog.setTitle("please wait");
                    progressDialog.setMessage(map.get("msg").toString());
                    progressDialog.show();

                    if (mtimer_autocancel != null)
                        mtimer_autocancel.start();
                }
            } else if (map != null && map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("2")) {
                if (map.containsKey("msg") && !map.get("msg").toString().equalsIgnoreCase("")) {
                    progressDialog.setTitle("please wait");
                    progressDialog.setMessage(map.get("msg").toString() + "...");
                    progressDialog.show();

                    if (mtimer_autocancel != null)
                        mtimer_autocancel.start();
                }
            } else if (map != null && map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("3")) {
                if (progressDialog != null)
                    progressDialog.cancel();

                bookReservation.setVisibility(View.VISIBLE);

                if (mtimer_autocancel != null)
                    mtimer_autocancel.cancel();

                Utils.toastTxt(map.get(ConstVariable.MESSAGE).toString(), mcontext);
            }
        } catch (Exception e) {
            Log.e("error123", e.getMessage());
        }
    }

    public static void getCurrentRideRequests() {
        SharedPreferences pref = mcontext.getApplicationContext().getSharedPreferences("lnctoken", 0);
        SharedPreferences.Editor editor = pref.edit();

        String tId = pref.getString("tokenid", null);

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        String vername = "";
        try {
            PackageInfo pInfo = mcontext.getPackageManager().getPackageInfo(mcontext.getPackageName(), 0);
            vername = pInfo.versionName;
            // verCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("devicetoken", tId);
        Utils.global.mapMain.put("device_type", "android");
        Utils.global.mapMain.put("version", vername);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_CURRENT_RIDE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.CCRide);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public void getCurrentRideDetails(HashMap<String, Object> vmap, String type) {
        //bookReservation.setVisibility(View.GONE);

        if (type.equalsIgnoreCase("1")) {
            Utils.global.dmap = vmap;

            if (mtimer != null)
                mtimer.start();

            //card_pick.setVisibility(View.GONE);
            bookReservation.setVisibility(View.GONE);
            layout_bottom.setVisibility(View.VISIBLE);

            if (Utils.global.dmap.get("profile_pic") != "") {
                if (Utils.global.dmap.containsKey("profile_pic") && !Utils.global.dmap.get("profile_pic").toString().equalsIgnoreCase("")) {
                    Picasso.with(mcontext).load(Settings.URLIMAGEBASE + Utils.global.dmap.get("profile_pic").toString()).placeholder(R.drawable.appicon).into(driverimage);
                    // userpic.setImageBitmap(getBitmapFromURL(image));
                }
            }

            StringBuilder sb = new StringBuilder();

            if (Utils.global.dmap.get("first_name") != "") {
                if (Utils.global.dmap.containsKey("first_name") && !Utils.global.dmap.get("first_name").toString().equalsIgnoreCase("")) {
                    sb.append(Utils.global.dmap.get("first_name").toString());
                }
            }

            if (Utils.global.dmap.get("last_name") != "") {
                if (Utils.global.dmap.containsKey("last_name") && !Utils.global.dmap.get("last_name").toString().equalsIgnoreCase("")) {
                    sb.append(" ");
                    sb.append(Utils.global.dmap.get("last_name").toString());
                    dname.setText(sb);
                }
            }

            if (Utils.global.dmap.get("mobile") != "") {
                if (Utils.global.dmap.containsKey("mobile") && !Utils.global.dmap.get("mobile").toString().equalsIgnoreCase(""))
                    dnumber.setText(Utils.global.dmap.get("mobile").toString());
            }

            if (Utils.global.dmap.get("driver_rating") != "") {
                if (Utils.global.dmap.containsKey("driver_rating") && !Utils.global.dmap.get("driver_rating").toString().equalsIgnoreCase("")) {
                    rating.setRating(Float.valueOf(Utils.global.dmap.get("driver_rating").toString()));
                    /*Drawable drawable = rating.getProgressDrawable();
                    drawable.setColorFilter(Color.parseColor("#6A9A28"), PorterDuff.Mode.SRC_ATOP);*/
                }
            }

            if (Utils.global.dmap.get("arrival_distance") != "") {
                if (Utils.global.dmap.containsKey("arrival_distance") && !Utils.global.dmap.get("arrival_distance").toString().equalsIgnoreCase("")) {
                    distance.setText(String.format("%.2f", Double.valueOf(Utils.global.dmap.get("arrival_distance").toString())) + " mi");
                }
            }

            if (Utils.global.dmap.get("arrival_time") != "") {
                if (Utils.global.dmap.containsKey("arrival_time") && !Utils.global.dmap.get("arrival_time").toString().equalsIgnoreCase("")) {
                    time.setText(Utils.global.dmap.get("arrival_time").toString());
                }
            }

            if (Utils.global.dmap.get("estimate_price") != "") {
                if (Utils.global.dmap.containsKey("estimate_price") && !Utils.global.dmap.get("estimate_price").toString().equalsIgnoreCase("")) {
                    price.setText("$" + Utils.global.dmap.get("estimate_price").toString());
                }
            }

            SavePref pref1 = new SavePref();
            pref1.SavePref(mcontext);
            pref1.setDriverId(Utils.global.dmap.get("driverid").toString());
            Gson gson = new Gson();
            String hashMapString = gson.toJson(Utils.global.dmap);
            pref1.setdrivermap(hashMapString);
            pref1.setRideId(Utils.global.dmap.get("id").toString());
            pref1.setisLocationUpdate("update");

            String ss = pref1.getisnewmsg();

            if (!ss.equalsIgnoreCase("")) {
                ischatmsg.setVisibility(View.VISIBLE);
            } else {
                ischatmsg.setVisibility(View.GONE);
            }

            mMap.clear();
            LatLng origin = null, dest = null;

            if (Utils.global.dmap.get("pickup_lat") != "") {
                if (Utils.global.dmap.containsKey("pickup_lat") && !Utils.global.dmap.get("pickup_lat").toString().equalsIgnoreCase("")) {
                    origin = new LatLng(Double.parseDouble(Utils.global.dmap.get("pickup_lat").toString()), Double.parseDouble(Utils.global.dmap.get("pickup_long").toString()));
                }

                if (Utils.global.dmap.containsKey("latitude") && !Utils.global.dmap.get("latitude").toString().equalsIgnoreCase("")) {
                    dest = new LatLng(Double.parseDouble(Utils.global.dmap.get("latitude").toString()), Double.parseDouble(Utils.global.dmap.get("longitude").toString()));
                }
            }

            if (origin != null && dest != null) {
                MarkerOptions options = new MarkerOptions();
                options.position(origin);
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMap.addMarker(options);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.car38);
                MarkerOptions options1 = new MarkerOptions();
                options1.position(dest);
                options1.icon(icon);
                dMarker = mMap.addMarker(options1);

                String url = getUrl(origin, dest);
                FetchUrl FetchUrl = new FetchUrl();
                FetchUrl.execute(url);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(origin);
                builder.include(dest);

                LatLngBounds bounds = builder.build();

                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                mMap.animateCamera(cu);
            }
        } else {
            if (vmap.get("cancel_status").toString().equalsIgnoreCase("0")) {
                //card_pick.setVisibility(View.VISIBLE);
                bookReservation.setVisibility(View.VISIBLE);
                layout_bottom.setVisibility(View.GONE);

                SavePref pref1 = new SavePref();
                pref1.SavePref(mcontext);
                pref1.setDriverId("");
                pref1.setdrivermap("");
                pref1.setRideId("");
            } else if (vmap.get("cancel_status").toString().equalsIgnoreCase("1")) {
                bookReservation.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                ride_Status.setVisibility(View.VISIBLE);

                ride_Status.setText("Ride cancelled by driver and we are searching for other drivers,please stay");
            }
        }
    }

    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            //Log.d("downloadUrl", data.toString());
            br.close();
        } catch (Exception e) {
            //Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                //Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                //Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                //Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                // Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                // Log.d("ParserTask","Executing routes");
                //Log.d("ParserTask",routes.toString());
            } catch (Exception e) {
                //Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            if (result != null) {
                ArrayList<LatLng> points;
                PolylineOptions lineOptions = null;

                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(5);
                    lineOptions.color(Color.BLUE);

                    //Log.d("onPostExecute","onPostExecute lineoptions decoded");
                }

                // Drawing polyline in the Google Map for the i-th route
                if (lineOptions != null) {
                    if (polylineFinal != null)
                        polylineFinal.remove();
                    polylineFinal = mMap.addPolyline(lineOptions);
                } else {
                    // Log.d("onPostExecute","without Polylines drawn");
                }
            }
        }
    }

}