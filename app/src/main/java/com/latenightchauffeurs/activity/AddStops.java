package com.latenightchauffeurs.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.libraries.places.api.model.Place.Field;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.adapter.PlaceArrayAdapter;
import com.latenightchauffeurs.adapter.StopsAddressAdapter;
import com.latenightchauffeurs.model.modelItem;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddStops extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    public static RecyclerView rv_loc;
    Button addButton, submit;
    ImageView back;
    AutoCompleteTextView location;
    TextView title;
    public static AddStops Instance;
    public static Context mcontext;
    public static StopsAddressAdapter requestsAdapter;
    public static List<modelItem> requestsList;
    public HashMap<String, Object> map;
    public static List<HashMap<String, Object>> stopsList = new ArrayList<>();

    private static final int GOOGLE_API_CLIENT_ID_PICK = 13;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(37.398160,
            -122.180831), new LatLng(37.430610, -121.972090));
    private PlaceArrayAdapter mPlaceArrayAdapter_stop;
    String location_address = "";
    private Long lastClickTime = Long.valueOf(0);
    private List<Field> fields;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addstops);
        addButton = findViewById(R.id.add_stop);
        submit = (Button) findViewById(R.id.submit);
        location = (AutoCompleteTextView) findViewById(R.id.location);
        rv_loc = (RecyclerView) findViewById(R.id.rv_loc);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
//        String title11 = "<font color='#ffffff'>Stop&nbsp; </font><font color='#8bbc50'> Address</font>";
//        title.setText(Html.fromHtml(title11));
        title.setText("Stop Address");

        initializeAutoCompleteIntent();

        /*Utils.global.mGoogleApiClient_stopaddress = new GoogleApiClient.Builder(AddStops.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(AddStops.this, GOOGLE_API_CLIENT_ID_PICK, (GoogleApiClient.OnConnectionFailedListener) AddStops.this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .build();

        location.setThreshold(3);
        location.setOnItemClickListener(mAutocompleteClickListener_stop);
        mPlaceArrayAdapter_stop = new PlaceArrayAdapter(AddStops.this, android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        location.setAdapter(mPlaceArrayAdapter_stop);*/

        location.setOnTouchListener((v, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return false;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                launchPlaceIntentBuilder(007);
            }
            return false;
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!location.getText().toString().isEmpty()) {
                    map = new HashMap<>();
                    if(!location_address.equalsIgnoreCase("")){
                        map.put("location", location_address);
                        location_address = "";
                    }else{
                        map.put("location", location.getText().toString());
                    }
                    stopsList.add(map);
                    location.setText("");
                } else {
                    Utils.toastTxt("please enter location address.", AddStops.this);
                }
                if (stopsList != null && stopsList.size() > 0)
                    loadRequestsList(mcontext, stopsList, "");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopsList != null) {
                    //if (stopsList.size() > 0) {
                    Utils.hideSoftKeyboard(AddStops.this);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("map", (Serializable) stopsList);
                    ((Activity) mcontext).setResult(Activity.RESULT_OK, returnIntent);
                    ((Activity) mcontext).finish();
                    /*} else {
                        Utils.toastTxt("add atleast one stop address location.", AddStops.this);
                    }*/
                }
            }
        });

        Instance = this;
        mcontext = this;

        rv_loc.setHasFixedSize(true);
        rv_loc.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));

           /* if (Utils.global.dropAddresslist!=null&&Utils.global.dropAddresslist.size()>0)
                loadRequestsList(mContext,Utils.global.dropAddresslist,"");*/

        if (getIntent().hasExtra("map")) {
            stopsList = (List<HashMap<String, Object>>) getIntent().getSerializableExtra("map");

            if (stopsList != null)
                loadRequestsList(mcontext, stopsList, "");
        }
    }

    private void launchPlaceIntentBuilder(int requestCode) {
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).build(this);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 007) {
                com.google.android.libraries.places.api.model.Place place = Autocomplete.getPlaceFromIntent(data);
                location.setText(place.getAddress());
            }
        }
    }

    private void initializeAutoCompleteIntent() {
        /** Initializing the Places API with the help of our API_KEY*/
        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(
                    getApplicationContext(),
                    getString(R.string.google_map_key));
        }
        /**
         * Set the fields to specify which types of place data to
         * return after the user has made a selection.
         * */
        fields = Arrays.asList(
                com.google.android.libraries.places.api.model.Place.Field.ID,
                com.google.android.libraries.places.api.model.Place.Field.LAT_LNG,
                com.google.android.libraries.places.api.model.Place.Field.ADDRESS,
                com.google.android.libraries.places.api.model.Place.Field.NAME);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.hideSoftKeyboard(AddStops.this);
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
        requestsAdapter = new StopsAddressAdapter(mcontext, requestsList, rv_loc, R.layout.stop_rowitem, ConstVariable.Login);
        //set the adapter object to the Recyclerview
        //Utils.e(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_loc.setAdapter(requestsAdapter);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener_stop
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter_stop.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            //Log.i(LOG_TAG, "Selected==============================: " + item.description);

            String[] items = item.toString().split(",");
            String value = items[0];

            location.post(new Runnable() {
                public void run() {
                    location.dismissDropDown();
                }
            });

            location.setText(value);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(Utils.global.mGoogleApiClient_stopaddress, placeId);
            placeResult.setResultCallback(mAutocompleteClickListenerp);
            //Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mAutocompleteClickListenerp
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
                        String fulladdress = addresses.get(0).getAddressLine(0).toString();
                        Log.d("fulladdress", fulladdress);
                        // city.setText(cityname);

                        //street_two_pickup.setText(fulladdress);
                        location_address = fulladdress;
                    }

                    if (addresses.get(0).getLocality() != null) {
                        String cityname = addresses.get(0).getLocality();
                        Log.d("city", cityname);
                        // city.setText(cityname);

                        //city_pickup.setText(cityname);
                    }

                    if (addresses.get(0).getAdminArea() != null) {
                        String statename = addresses.get(0).getAdminArea();
                        // state.setText(statename);
                        Log.d("state", statename);

                        // state_pickup.setText(statename);
                    }

                    if (addresses.get(0).getPostalCode() != null) {
                        String zipname = addresses.get(0).getPostalCode();
                        //Log.d("country", zipname);
                        // zip_pickup.setText(zipname);
                    }

                    if (addresses.get(0).getCountryName() != null) {
                        String mcountry = addresses.get(0).getCountryName();
                        //Log.d("country", mcountry);
                        // country=mcountry;
                        //Utils.e("map======="+"105========",country);
                    }
                } else {
                    new Utils(mcontext);
                    Utils.toastTxt("No location found with these address.", mcontext);
                }
            } catch (IOException e) {
                Log.e("error123", e.getMessage());
                e.printStackTrace();
            }
            //lat_pickup=String.valueOf(place.getLatLng().latitude);
            //long_pick=String.valueOf(place.getLatLng().longitude);

                   /* mNameTextView.setText(Html.fromHtml(place.getName() + ""));
                    mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
                    mIdTextView.setText(Html.fromHtml(place.getId() + ""));
                    mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
                    mWebTextView.setText(place.getWebsiteUri() + "");
                    if (attributions != null)
                    {
                        mAttTextView.setText(Html.fromHtml(attributions.toString()));
                    }*/

        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter_stop.setGoogleApiClient(Utils.global.mGoogleApiClient_stopaddress);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter_stop.setGoogleApiClient(null);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (Utils.global.mGoogleApiClient_stopaddress != null) {
            if (Utils.global.mGoogleApiClient_stopaddress.isConnected()) {
                Utils.global.mGoogleApiClient_stopaddress.stopAutoManage(AddStops.this);
                Utils.global.mGoogleApiClient_stopaddress.disconnect();
            }
        }
    }
}
