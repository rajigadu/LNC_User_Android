package com.latenightchauffeurs.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.*;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.activity.AddStops;
import com.latenightchauffeurs.activity.CardsList;
import com.latenightchauffeurs.activity.DropAddressList;
import com.latenightchauffeurs.adapter.PlaceArrayAdapter;
import com.latenightchauffeurs.model.SavePref;
import com.library.NavigationBar;
import com.library.NvTab;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookReservation extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, NavigationBar.OnTabSelected, NavigationBar.OnTabClick {
    public static BookReservation Instance;
    public static List<String> typesList, stopsList;

    private NavigationBar bar;
    private int position = 0;
    // public int rideCount=0;

    private Unbinder unbinder;

    @BindView(R.id.ptitle)
    TextView card_ptitle;

    @BindView(R.id.stopstatus)
    TextView status_stops;

    @BindView(R.id.dropinfotitle)
    TextView card_droptitle;

    @BindView(R.id.pickupinfotitle)
    TextView card_pickuptitle;

    @BindView(R.id.pinfo)
    CardView card_pinfo;

    @BindView(R.id.card_dropinfo)
    CardView card_dropinfo;

    @BindView(R.id.card_pickup)
    CardView card_pickup;

    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.price)
    Button estimatePrice;

    @BindView(R.id.pdate)
    EditText date_pickup;

    @BindView(R.id.ptime)
    EditText time_pickup;

    @BindView(R.id.nstops)
    EditText no_stops;

    @BindView(R.id.street)
    AutoCompleteTextView street_one_pickup;

    @BindView(R.id.ainel2)
    EditText street_two_pickup;

    @BindView(R.id.city)
    EditText city_pickup;

    @BindView(R.id.state)
    EditText state_pickup;

    @BindView(R.id.zipcode)
    EditText zip_pickup;

    @BindView(R.id.type)
    EditText type;

    @BindView(R.id.street1)
    AutoCompleteTextView street_one_drop;

    @BindView(R.id.ainel21)
    EditText street_two_drop;

    @BindView(R.id.city1)
    EditText city_drop;

    @BindView(R.id.state1)
    EditText state_drop;

    @BindView(R.id.zipcode1)
    EditText zip_drop;

    @BindView(R.id.note)
    EditText note;

    @BindView(R.id.savedrop)
    CheckBox save_drop;

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

    @BindView(R.id.close22)
    ImageView close22;

    @BindView(R.id.close11)
    ImageView close11;

    @BindView(R.id.card_choose)
    Button chooseCard;

    private int mYear, mMonth, mDay, mHour, mMinute;
    public static Context mcontext;
    public HashMap<String, Object> dataMap;

    private static final int GOOGLE_API_CLIENT_ID_PICK = 5, GOOGLE_API_CLIENT_ID_DROP = 6;
    private PlaceArrayAdapter mPlaceArrayAdapter_pickup, mPlaceArrayAdapter_drop;
    public String lat_pickup = "", long_pick = "", lat_drop = "", long_drop = "", payment_Id = "";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    HashMap<String, Object> map, dropMap, cardMap;
    List<HashMap<String, Object>> stopsAddList;
    JSONObject json, jsonStops;
    public int saveDropLocation = 0;

    private static OnFragmentInteractionListenerBooking mListener;

    public interface OnFragmentInteractionListenerBooking {
        // TODO: Update argument type and name
        void onFragmentInteraction(int position);
    }

    @Override
    public void onTabClick(int touchPosition, NvTab prev, NvTab nvTab) {
        // rideCount=touchPosition+1;
        //Toast.makeText(getContext(), "Selected position: " + touchPosition, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTabSelected(int touchPosition, NvTab prev, NvTab nvTab) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_bookreservation, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        unbinder = ButterKnife.bind(this, v);

        mcontext = getContext();
        Instance = this;

        status_stops.setText("No stops added.");
        status_stops.setTextColor(getResources().getColor(R.color.white_full));
        status_stops.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        card_pinfo.setVisibility(View.GONE);
        card_pickup.setVisibility(View.VISIBLE);
        card_dropinfo.setVisibility(View.VISIBLE);

        card_ptitle.setOnClickListener(this);
        card_pickuptitle.setOnClickListener(this);
        card_droptitle.setOnClickListener(this);
        date_pickup.setOnClickListener(this);
        time_pickup.setOnClickListener(this);
        save_drop.setOnClickListener(this);
        submit.setOnClickListener(this);
        estimatePrice.setOnClickListener(this);
        choose.setOnClickListener(this);
        type.setOnClickListener(this);
        no_stops.setOnClickListener(this);
        chooseCard.setOnClickListener(this);

        card_number.setVisibility(View.GONE);
        emonthtitle.setVisibility(View.GONE);
        emonth.setVisibility(View.GONE);
        eyeartitle.setVisibility(View.GONE);
        eyear.setVisibility(View.GONE);

        typesList = new ArrayList<>();
        typesList.add("Home");
        typesList.add("Office");
        type.setText(typesList.get(0));

        stopsList = new ArrayList<>();

        stopsList.add("No");
        stopsList.add("Yes");
        no_stops.setText(stopsList.get(0));

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
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

        if (!cnumber.toString().equalsIgnoreCase("")) {
            card_number.setText(cnumber);
            payment_Id = cid;
        }
        if (!cmonth.toString().equalsIgnoreCase("")) {
            emonth.setText(cmonth);
        }
        if (!cyear.toString().equalsIgnoreCase("")) {
            eyear.setText(cyear);
        }

        Utils.global.mGoogleApiClient_pickup = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID_PICK, this)
                .addConnectionCallbacks(this)
                .build();

        Utils.global.mGoogleApiClient_drop = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID_DROP, this)
                .addConnectionCallbacks(this)
                .build();

        street_one_pickup.setThreshold(3);
        street_one_pickup.setOnItemClickListener(mAutocompleteClickListener_pick);
        mPlaceArrayAdapter_pickup = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        street_one_pickup.setAdapter(mPlaceArrayAdapter_pickup);

        street_one_drop.setThreshold(3);
        street_one_drop.setOnItemClickListener(mAutocompleteClickListener_drop);
        mPlaceArrayAdapter_drop = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        street_one_drop.setAdapter(mPlaceArrayAdapter_drop);

        if (save_drop.isChecked()) {
            saveDropLocation = 1;
        } else {
            saveDropLocation = 0;
        }

        pref1.SavePref(mcontext);
        String fnam = pref1.getUserFName();
        String llname = pref1.getUserLName();
        String unum = pref1.getMobile();
        String emai = pref1.getEmail();

        StringBuilder sb = new StringBuilder();

       /* if (!fnam.toString().equalsIgnoreCase(""))
        {
            sb.append(fnam);
        }

        if (!unum.toString().equalsIgnoreCase(""))
        {
            mobile.setText(unum);
        }
        if (!emai.toString().equalsIgnoreCase(""))
        {
            email.setText(emai);
        }
        if (!llname.toString().equalsIgnoreCase(""))
        {
            sb.append(" ");
            sb.append(llname);
            fname.setText(sb);
        }*/

        bar = (NavigationBar) v.findViewById(R.id.navBar);
        bar.setOnTabClick(this);
        setup(true, 5);
        bar.setOnTabSelected(this);

        //rideCount=1;
    }

    public void polulateActiveCardInfo(Context context, List<HashMap<String, Object>> viewList) {
        if (viewList != null) {
            card_number.setVisibility(View.VISIBLE);
            emonthtitle.setVisibility(View.VISIBLE);
            emonth.setVisibility(View.VISIBLE);
            eyeartitle.setVisibility(View.VISIBLE);
            eyear.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
        } else {
            card_number.setVisibility(View.GONE);
            emonthtitle.setVisibility(View.GONE);
            emonth.setVisibility(View.GONE);
            eyeartitle.setVisibility(View.GONE);
            eyear.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }
    }

    private void setup(boolean reset, int count) {
        if (reset)
            bar.resetItems();
        bar.setTabCount(count);
        bar.animateView(3000);
        bar.setCurrentPosition(position <= 0 ? 0 : position);
    }

    @Override
    public void onPause() {
        super.onPause();

       /* mGoogleApiClient_pickup.stopAutoManage(getActivity());
        mGoogleApiClient_pickup.disconnect();

        mGoogleApiClient_drop.stopAutoManage(getActivity());
        mGoogleApiClient_drop.disconnect();*/
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

            street_one_pickup.post(new Runnable() {
                public void run() {
                    street_one_pickup.dismissDropDown();
                }
            });

            street_one_pickup.setText(value);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(Utils.global.mGoogleApiClient_pickup, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_pick);
            //Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
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
                        String fulladdress = addresses.get(0).getAddressLine(0).toString();
                        Log.d("fulladdress", fulladdress);
                        // city.setText(cityname);

                        street_two_pickup.setText(fulladdress);
                    }

                    if (addresses.get(0).getLocality() != null) {
                        String cityname = addresses.get(0).getLocality();
                        Log.d("city", cityname);
                        // city.setText(cityname);

                        city_pickup.setText(cityname);
                    }

                    if (addresses.get(0).getAdminArea() != null) {
                        String statename = addresses.get(0).getAdminArea();
                        // state.setText(statename);
                        Log.d("state", statename);

                        state_pickup.setText(statename);
                    }

                    if (addresses.get(0).getPostalCode() != null) {
                        String zipname = addresses.get(0).getPostalCode();
                        //Log.d("country", zipname);
                        zip_pickup.setText(zipname);
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

            lat_pickup = String.valueOf(place.getLatLng().latitude);
            long_pick = String.valueOf(place.getLatLng().longitude);

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


    private AdapterView.OnItemClickListener mAutocompleteClickListener_drop
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter_drop.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            //Log.i(LOG_TAG, "Selected==============================: " + item.description);

            String[] items = item.toString().split(",");
            String value = items[0];

            street_one_drop.post(new Runnable() {
                public void run() {
                    street_one_drop.dismissDropDown();
                }
            });

            street_one_drop.setText(value);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(Utils.global.mGoogleApiClient_drop, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_drop);
            //Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
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
                        String fulladdress = addresses.get(0).getAddressLine(0).toString();
                        Log.d("fulladdress", fulladdress);
                        // city.setText(cityname);

                        street_two_drop.setText(fulladdress);
                    }

                    if (addresses.get(0).getLocality() != null) {
                        String cityname = addresses.get(0).getLocality();
                        Log.d("city", cityname);
                        // city.setText(cityname);

                        city_drop.setText(cityname);
                    }

                    if (addresses.get(0).getAdminArea() != null) {
                        String statename = addresses.get(0).getAdminArea();
                        // state.setText(statename);
                        Log.d("state", statename);

                        state_drop.setText(statename);
                    }

                    if (addresses.get(0).getPostalCode() != null) {
                        String zipname = addresses.get(0).getPostalCode();
                        //Log.d("country", zipname);
                        zip_drop.setText(zipname);
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
                // bottomSheetAddress.setText("Not Available.");
                e.printStackTrace();
            }

            lat_drop = String.valueOf(place.getLatLng().latitude);
            long_drop = String.valueOf(place.getLatLng().longitude);

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
        //unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                submit();
                break;
            case R.id.price:
                estimatedRideCostRequest();
                break;
            case R.id.pdate:
                datePickupAddress();
                break;
            case R.id.ptime:
                timePickupAddress();
                break;
            case R.id.type:
                pickType();
                break;
            case R.id.nstops:
                pickStops();
                break;
            case R.id.ptitle:
                if (card_pinfo.getVisibility() == View.VISIBLE) {
                    card_pinfo.setVisibility(View.GONE);
                } else {
                    card_pinfo.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.pickupinfotitle:
               /* if (card_pickup.getVisibility()==View.VISIBLE)
                {
                    card_pickup.setVisibility(View.GONE);
                }
                else
                {
                    card_pickup.setVisibility(View.VISIBLE);
                }*/
                break;
            case R.id.dropinfotitle:
                /*if (card_dropinfo.getVisibility()==View.VISIBLE)
                {
                    card_dropinfo.setVisibility(View.GONE);
                }
                else
                {
                    card_dropinfo.setVisibility(View.VISIBLE);
                }*/
                break;
            case R.id.savedrop:
                if (save_drop.isChecked()) {
                    saveDropLocation = 1;
                } else {
                    saveDropLocation = 0;
                }
                break;
            case R.id.choose:
                Intent i = new Intent(mcontext, DropAddressList.class);
                startActivityForResult(i, 1);
                break;
            case R.id.card_choose:
                Intent j = new Intent(mcontext, CardsList.class);
                startActivityForResult(j, 2);
                break;
        }
    }

    void pickType() {
        new Utils(mcontext);
        // Utils.toastTxt("ok",UserSignup.this);

        final CharSequence[] items;
        items = typesList.toArray(new CharSequence[typesList.size()]);

        if (items.length > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
            builder.setTitle("Address Type");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    type.setText(items[item]);

                    if (type.getText().toString().equalsIgnoreCase("Home")) {
                        Intent i = new Intent(mcontext, DropAddressList.class);
                        startActivityForResult(i, 1);
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    void pickStops() {
        new Utils(mcontext);
        // Utils.toastTxt("ok",UserSignup.this);

        final CharSequence[] items;
        items = stopsList.toArray(new CharSequence[stopsList.size()]);

        if (items.length > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
            builder.setTitle("Stop Address Options");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    no_stops.setText(items[item]);

                    if (no_stops.getText().toString().equalsIgnoreCase("Yes")) {
                        Intent i = new Intent(mcontext, AddStops.class);
                        startActivityForResult(i, 3);
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void datePickupAddress() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(mcontext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String month = "", day = "";

                        if ((monthOfYear + 1) < 10)
                            month = "0" + String.valueOf(monthOfYear + 1);
                        else
                            month = String.valueOf(monthOfYear + 1);

                        if ((dayOfMonth) < 10)
                            day = "0" + String.valueOf(dayOfMonth);
                        else
                            day = String.valueOf(dayOfMonth);

                        date_pickup.setText((year) + "-" + month + "-" + day);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void timePickupAddress() {
        // Get Current Time

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(mcontext,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm = "";

                        final Calendar c = Calendar.getInstance();
                        // mHour = c.get(Calendar.HOUR_OF_DAY);
                        //  mMinute = c.get(Calendar.MINUTE);

                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);

                        if (c.get(Calendar.AM_PM) == Calendar.AM)
                            am_pm = "AM";
                        else if (c.get(Calendar.AM_PM) == Calendar.PM) {
                            am_pm = "PM";
                        }

                        //startTime.setText(hourOfDay + ":" + minute);
                        String minutes = "";

                        if (minute < 10)
                            minutes = "0" + String.valueOf(minute);
                        else
                            minutes = String.valueOf(minute);

                        String strHrsToShow = "";  // = (hourOfDay== 0) ?"12":c.get(Calendar.HOUR)+"";

                        int hour = hourOfDay % 12;
                        if (hour == 0)
                            hour = 12;

                        if (hour < 10)
                            strHrsToShow = "0" + String.valueOf(hour);
                        else
                            strHrsToShow = String.valueOf(hour);

                        time_pickup.setText(strHrsToShow + ":" + minutes + " " + am_pm);
                        //startTime.setText(String.format("%02d:%02d", strHrsToShow,c.get(Calendar.MINUTE))+""+am_pm);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
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
        // Utils.startActivity(ActivityLogin.this,ActivityEvents.class);
        new Utils(mcontext);

       /* if (fname.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter full name.",mContext);
        }
        else if (mobile.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter mobile number.",mContext);
        }
        else if (email.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter email address.",mContext);
        }
        else if(!Utils.isValidEmail(email.getText().toString().trim()))
        {
            Utils.toastTxt("Please enter valid email address.",mContext);
        }
        */
        if (street_one_pickup.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter street for pickup location.", mcontext);
        } else if (street_two_pickup.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter full pickup address.", mcontext);
        } else if (city_pickup.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter pickup city.", mcontext);
        } else if (state_pickup.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter pickup state.", mcontext);
        } else if (zip_pickup.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter pickup zipcode.", mcontext);
        } else if (street_one_drop.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter street for drop location.", mcontext);
        } else if (street_two_pickup.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter full drop address.", mcontext);
        } else if (city_pickup.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter drop location city.", mcontext);
        } else if (state_pickup.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter drop location state.", mcontext);
        } else if (zip_pickup.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter drop location zipcode.", mcontext);
        } else if (card_number.getText().toString().trim().isEmpty()) {
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
                // json.put("name", fname.getText().toString());
                // json.put("mobile", mobile.getText().toString());
                //json.put("email", email.getText().toString());
                json.put("nstops", no_stops.getText().toString());
                json.put("pstreet", street_one_pickup.getText().toString());
                json.put("pstreet2", street_two_pickup.getText().toString());
                json.put("pcity", city_pickup.getText().toString());
                json.put("pstate", state_pickup.getText().toString());
                json.put("pzip", zip_pickup.getText().toString());
                json.put("platitude", lat_pickup);
                json.put("plongitude", long_pick);
                //json.put("ridecount", rideCount);
                json.put("address_type", type.getText().toString());
                json.put("dstreet", street_one_drop.getText().toString());
                json.put("dstreet2", street_two_drop.getText().toString());
                json.put("dcity", city_drop.getText().toString());
                json.put("dstate", state_drop.getText().toString());
                json.put("dzip", zip_drop.getText().toString());
                json.put("etNote", note.getText().toString());
                json.put("dlatitude", lat_drop);
                json.put("dlongitude", long_drop);
                json.put("savedrop", saveDropLocation);
                json.put("card_id", payment_Id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            map = new HashMap<>();
            map.put("json", json);
            map.put("jsonstops", jsonStops);
            OnlineRequest.bookingRequest(mcontext, map);
        }
    }

    public void estimatedRideCostRequest() {
        // Utils.startActivity(ActivityLogin.this,ActivityEvents.class);
        if (lat_pickup.toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter pickup location.", mcontext);
        } else if (lat_drop.toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter destination location.", mcontext);
        } else {
            map = new HashMap<>();
            map.put("pick_lat", lat_pickup);
            map.put("pick_lng", long_pick);
            map.put("dest_lat", lat_drop);
            map.put("dest_lng", long_drop);
            OnlineRequest.estimatedPriceRequest(mcontext, map);
        }
    }


    public void displayReceivedData(HashMap<String, Object> smap) {
        //txtData.setText("Data received: "+message);

        Utils.e("data123", smap.toString());
        dataMap = smap;

        if (dataMap != null && dataMap.size() > 0) {
            if (dataMap.containsKey("one") && !dataMap.get("one").toString().equalsIgnoreCase("")) {
                street_one_pickup.setText(dataMap.get("one").toString());
            }

            if (dataMap.containsKey("two") && !dataMap.get("two").toString().equalsIgnoreCase("")) {
                street_two_pickup.setText(dataMap.get("two").toString());
            }

            if (dataMap.containsKey("three") && !dataMap.get("three").toString().equalsIgnoreCase("")) {
                city_pickup.setText(dataMap.get("three").toString());
            }

            if (dataMap.containsKey("four") && !dataMap.get("four").toString().equalsIgnoreCase("")) {
                state_pickup.setText(dataMap.get("four").toString());
            }

            if (dataMap.containsKey("five") && !dataMap.get("five").toString().equalsIgnoreCase("")) {
                zip_pickup.setText(dataMap.get("five").toString());
            }

            if (dataMap.containsKey("six") && !dataMap.get("six").toString().equalsIgnoreCase("")) {
                lat_pickup = dataMap.get("six").toString();
            }

            if (dataMap.containsKey("seven") && !dataMap.get("seven").toString().equalsIgnoreCase("")) {
                long_pick = dataMap.get("seven").toString();
            }
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
        // BookReservation.startActivityForResult(i,1);
        startActivityForResult(i, 1);

    }

    private final int CREATE_NEW_CARD = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {
                    dropMap = new HashMap<>();

                    dropMap = (HashMap<String, Object>) data.getSerializableExtra("map");

                    if (dropMap != null && dropMap.size() > 0) {
                        if (dropMap.containsKey("street_address") && !dropMap.get("street_address").toString().equalsIgnoreCase("")) {
                            street_one_drop.setText(dropMap.get("street_address").toString());
                        }
                        if (dropMap.containsKey("address2") && !dropMap.get("address2").toString().equalsIgnoreCase("")) {
                            street_two_drop.setText(dropMap.get("address2").toString());
                        }
                        if (dropMap.containsKey("city") && !dropMap.get("city").toString().equalsIgnoreCase("")) {
                            city_drop.setText(dropMap.get("city").toString());
                        }
                        if (dropMap.containsKey("state") && !dropMap.get("state").toString().equalsIgnoreCase("")) {
                            state_drop.setText(dropMap.get("state").toString());
                        }
                        if (dropMap.containsKey("zipcode") && !dropMap.get("zipcode").toString().equalsIgnoreCase("")) {
                            zip_drop.setText(dropMap.get("zipcode").toString());
                        }

                        if (dropMap.containsKey("notes") && !dropMap.get("notes").toString().equalsIgnoreCase("")) {
                            note.setText(dropMap.get("notes").toString());
                        }
                        if (dropMap.containsKey("address_type") && !dropMap.get("address_type").toString().equalsIgnoreCase("")) {
                            type.setText(dropMap.get("address_type").toString());
                        }
                        if (dropMap.containsKey("lat") && !dropMap.get("lat").toString().equalsIgnoreCase("")) {
                            lat_drop = dropMap.get("lat").toString();
                            long_drop = dropMap.get("lon").toString();
                        }
                    }
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
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

                    if (cardMap.containsKey("last4") && !cardMap.get("last4").toString().equalsIgnoreCase("")) {
                        card_number.setText("XXXXXXXXXXXX" + cardMap.get("last4").toString());
                        payment_Id = cardMap.get("carid").toString();
                    }
                    if (cardMap.containsKey("exp_month") && !cardMap.get("exp_month").toString().equalsIgnoreCase("")) {
                        emonth.setText(cardMap.get("exp_month").toString());
                    }
                    if (cardMap.containsKey("exp_year") && !cardMap.get("exp_year").toString().equalsIgnoreCase("")) {
                        eyear.setText(cardMap.get("exp_year").toString());
                    }

                    SavePref pref1 = new SavePref();
                    pref1.SavePref(mcontext);
                    pref1.setCardId(payment_Id);
                    pref1.setCardNumber(card_number.getText().toString());
                    pref1.setCardExMonth(emonth.getText().toString());
                    pref1.setCardExYear(eyear.getText().toString());
                }
            } else if (requestCode == 3) {
                stopsAddList = new ArrayList<>();
                stopsAddList = (List<HashMap<String, Object>>) data.getSerializableExtra("map");

                status_stops.setText("Added.");
                status_stops.setTextColor(getResources().getColor(R.color.green));
                status_stops.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tick, 0, 0, 0);
            }
        }
    }

    public static void getBookingResponce() {
        mListener.onFragmentInteraction(ConstVariable.ChangePassword);
    }

    public void showEstimatedPriceDilog(final HashMap<String, Object> data) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog8, null);
        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView distance = (TextView) dialog.findViewById(R.id.distance);
        final TextView time = (TextView) dialog.findViewById(R.id.time);
        final TextView price = (TextView) dialog.findViewById(R.id.price);
        final Button ok = (Button) dialog.findViewById(R.id.ok);

        title.setText(R.string.app_name);

        if (data != null && data.size() > 0) {
            if (data.containsKey("distance") && !data.get("distance").toString().equalsIgnoreCase("")) {
                distance.setText(String.format("%.2f", Double.valueOf(data.get("distance").toString())) + " mi");
            }

            if (data.containsKey("estimate_time") && !data.get("estimate_time").toString().equalsIgnoreCase("")) {
                time.setText(data.get("estimate_time").toString());
            }

            if (data.containsKey("estimate_price") && !data.get("estimate_price").toString().equalsIgnoreCase("")) {
                price.setText(data.get("estimate_price").toString() + " $");
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
}