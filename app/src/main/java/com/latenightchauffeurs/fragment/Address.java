package com.latenightchauffeurs.fragment;

import android.content.Context;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.adapter.PlaceArrayAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Address extends Fragment implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks
{
    private Unbinder unbinder;
    @BindView(R.id.street)
    AutoCompleteTextView street_one;

    @BindView(R.id.ainel2)
    EditText street_two;

    @BindView(R.id.city)
    EditText city_drop;

    @BindView(R.id.state)
    EditText state_drop;

    @BindView(R.id.zipcode)
    EditText zip_drop;

    @BindView(R.id.notes)
    EditText notes;

    @BindView(R.id.add)
    Button add;

    public Context mcontext;
    private static final int GOOGLE_API_CLIENT_ID =9;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    public String lat="",longi="";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    public static String addresstype;

    @BindView(R.id.agroups)
    RadioGroup group;

    RadioButton radioevent;
    HashMap<String,Object> map;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_address, container, false);

        if (Utils.global.mGoogleApiClient==null)
        {
            Log.e("connected","connected");
            Utils.global.mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(getActivity(),GOOGLE_API_CLIENT_ID, this)
                    .addConnectionCallbacks(this)
                    .build();
        }
        return rootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(final View v, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(v, savedInstanceState);
        unbinder = ButterKnife.bind(this,v);

        mcontext=getContext();

        add.setOnClickListener(this);

        street_one.setThreshold(3);
        street_one.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(mcontext,android.R.layout.simple_list_item_1,BOUNDS_MOUNTAIN_VIEW, null);
        street_one.setAdapter(mPlaceArrayAdapter);

        int eventselectedId = group.getCheckedRadioButtonId();
        radioevent = (RadioButton) v.findViewById(eventselectedId);
        addresstype=radioevent.getText().toString();

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.radio1:
                        int hostselectedId = group.getCheckedRadioButtonId();
                        radioevent = (RadioButton) v.findViewById(hostselectedId);
                        addresstype=radioevent.getText().toString();
                        //Toast.makeText(mContext,addresstype,Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio2:
                        int hostselected = group.getCheckedRadioButtonId();
                        radioevent = (RadioButton) v.findViewById(hostselected);
                        addresstype=radioevent.getText().toString();
                       // Toast.makeText(mContext,addresstype,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            //Log.i(LOG_TAG, "Selected==============================: " + item.description);

            String[] items=item.toString().split(",");
            String value=items[0];

            street_one.post(new Runnable()
            {
                public void run()
                {
                    street_one.dismissDropDown();
                }});

            street_one.setText(value);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(Utils.global.mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            //Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>()
    {
        @Override
        public void onResult(PlaceBuffer places)
        {
            if (!places.getStatus().isSuccess())
            {
                //Log.e(LOG_TAG, "Place query did not complete. Error: " +places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            // CharSequence attributions = places.getAttributions();

            Geocoder geocoder = new Geocoder(mcontext);
            try
            {
                List<android.location.Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude, 1);

                if (addresses!=null && addresses.size()>0)
                {
                    if (addresses.get(0).getAddressLine(0) != null)
                    {
                        String fulladdress = addresses.get(0).getAddressLine(0).toString();
                        Log.d("fulladdress", fulladdress);
                        // city.setText(cityname);

                        street_two.setText(fulladdress);
                    }

                    if(addresses.get(0).getLocality() != null)
                    {
                        String cityname = addresses.get(0).getLocality();
                        Log.d("city", cityname);
                        // city.setText(cityname);

                        city_drop.setText(cityname);
                    }

                    if(addresses.get(0).getAdminArea() != null)
                    {
                        String statename = addresses.get(0).getAdminArea();
                        // state.setText(statename);
                        Log.d("state", statename);

                        state_drop.setText(statename);
                    }

                    if(addresses.get(0).getPostalCode() != null)
                    {
                        String zipname = addresses.get(0).getPostalCode();
                        //Log.d("country", zipname);
                        zip_drop.setText(zipname);
                    }

                    if(addresses.get(0).getCountryName() != null)
                    {
                        String mcountry = addresses.get(0).getCountryName();
                        //Log.d("country", mcountry);
                        // country=mcountry;
                        //Utils.e("map======="+"105========",country);
                    }
                }
                else
                {
                    new Utils(mcontext);
                    Utils.toastTxt("No location found with these address.",mcontext);
                }
            }
            catch (IOException e)
            {
                Log.e("error123",e.getMessage());
                e.printStackTrace();
            }

            lat=String.valueOf(place.getLatLng().latitude);
            longi=String.valueOf(place.getLatLng().longitude);
        }
    };

    private void submit()
    {
        new Utils(mcontext);

        if (street_one.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter street for drop location.",mcontext);
        }
        else if (street_two.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter full drop address.",mcontext);
        }
        else if (city_drop.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter drop location city.",mcontext);
        }
        else if (state_drop.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter drop location state.",mcontext);
        }
        else if (zip_drop.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter drop location zipcode.",mcontext);
        }
        else
        {
            map=new HashMap<>();
            map.put("street_address",street_one.getText().toString());
            map.put("address2",street_two.getText().toString());
            map.put("city",city_drop.getText().toString());
            map.put("state",state_drop.getText().toString());
            map.put("zipcode",zip_drop.getText().toString());
            map.put("latitude",lat);
            map.put("longitude",longi);
            map.put("address_type",addresstype);
            map.put("notes",notes.getText().toString());
            map.put("type","2");

            OnlineRequest.addDropAddressRequest(mcontext,map);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        mPlaceArrayAdapter.setGoogleApiClient(Utils.global.mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    @Override
    public void onStop()
    {
        super.onStop();
        Log.e("stop","stop");

        if (Utils.global.mGoogleApiClient!=null)
        {
            if (Utils.global.mGoogleApiClient.isConnected())
            {
                Utils.global.mGoogleApiClient.stopAutoManage((getActivity()));
                Utils.global.mGoogleApiClient.disconnect();
            }
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        Log.e("pause","pause");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.e("destory","destroy");
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
        Log.e("destroyview","destroyview");

    }

    @Override
    public void onResume()
    {
        super.onResume();

        Log.e("resume","resme");

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.add:
                submit();
                //Utils.startActivity(getActivity(),AddAddress.class);
                break;
        }
    }
}