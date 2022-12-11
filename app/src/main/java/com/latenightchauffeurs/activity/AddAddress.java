package com.latenightchauffeurs.activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.latenightchauffeurs.model.SavePref;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by narayana on 4/3/2018.
 */

public class AddAddress extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks
{
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

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

    private static final int GOOGLE_API_CLIENT_ID = 0;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    public String lat="",longi="";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    public static String addresstype;

    @BindView(R.id.agroups)
    RadioGroup group;

    RadioButton radioevent;
    HashMap<String,Object> map,emap;
    String eid="";
    public String type="";
    public static AddAddress Instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaddress);
        unbinder = ButterKnife.bind(this);

        Instance=this;

        title.setText("Drop Address");

        if(getIntent().hasExtra("map"))
        {
            emap=new HashMap<>();
            emap=(HashMap<String, Object>) getIntent().getSerializableExtra("map");

            if (emap.containsKey("id"))
            {
                eid=emap.get("id").toString();
            }
        }
        else if(getIntent().hasExtra("type"))
        {
            type=getIntent().getStringExtra("type");
        }
        if (!eid.equalsIgnoreCase(""))
        {
            if (emap!=null)
            {
                if (emap.size()>0)
                {
                    if (emap.containsKey("street_address"))
                    {
                        if (!emap.get("street_address").toString().equalsIgnoreCase(""))
                        {
                            street_one.setText(emap.get("street_address").toString());
                            lat=emap.get("lat").toString();
                            longi=emap.get("lon").toString();
                        }
                    }

                    if (emap.containsKey("address2"))
                    {
                        if (!emap.get("address2").toString().equalsIgnoreCase(""))
                            street_two.setText(emap.get("address2").toString());
                    }

                    if (emap.containsKey("city"))
                    {
                        if (!emap.get("city").toString().equalsIgnoreCase(""))
                            city_drop.setText(emap.get("city").toString());
                    }

                    if (emap.containsKey("state"))
                    {
                        if (!emap.get("state").toString().equalsIgnoreCase(""))
                            state_drop.setText(emap.get("state").toString());
                    }

                    if (emap.containsKey("zipcode"))
                    {
                        if (!emap.get("zipcode").toString().equalsIgnoreCase(""))
                            zip_drop.setText(emap.get("zipcode").toString());
                    }

                    if (emap.containsKey("notes"))
                    {
                        if (!emap.get("notes").toString().equalsIgnoreCase(""))
                            notes.setText(emap.get("notes").toString());
                    }
                }
            }
            add.setText("Update Address");
        }
        else
        {
            add.setText("Add Address");
        }

        add.setOnClickListener(this);
        back.setOnClickListener(this);

       Utils.global.mGoogleApiClient_address = new GoogleApiClient.Builder(AddAddress.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(AddAddress.this, GOOGLE_API_CLIENT_ID, AddAddress.this)
                .addConnectionCallbacks(this)
                .build();

        street_one.setThreshold(3);
        street_one.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(AddAddress.this,android.R.layout.simple_list_item_1,BOUNDS_MOUNTAIN_VIEW, null);
        street_one.setAdapter(mPlaceArrayAdapter);

        int eventselectedId = group.getCheckedRadioButtonId();
        radioevent = (RadioButton) findViewById(eventselectedId);
        addresstype=radioevent.getText().toString();

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group,int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.radio1:
                        int hostselectedId = group.getCheckedRadioButtonId();
                        radioevent = (RadioButton) findViewById(hostselectedId);
                        addresstype=radioevent.getText().toString();
                        Toast.makeText(AddAddress.this,addresstype,Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio2:
                        int hostselected = group.getCheckedRadioButtonId();
                        radioevent = (RadioButton) findViewById(hostselected);
                        addresstype=radioevent.getText().toString();
                        Toast.makeText(AddAddress.this,addresstype,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public  void  editDropAddress( String eeid)
    {
        new Utils(AddAddress.this);

        if (street_one.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter street for drop location.",AddAddress.this);
        }
        else if (street_two.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter full drop address.",AddAddress.this);
        }
        else if (city_drop.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter drop location city.",AddAddress.this);
        }
        else if (state_drop.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter drop location state.",AddAddress.this);
        }
        else if (zip_drop.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter drop location zipcode.",AddAddress.this);
        }
        else
        {
            map=new HashMap<>();
            map.put("id",eeid);
            map.put("street_address",street_one.getText().toString());
            map.put("address2",street_two.getText().toString());
            map.put("city",city_drop.getText().toString());
            map.put("state",state_drop.getText().toString());
            map.put("zipcode",zip_drop.getText().toString());
            map.put("latitude",lat);
            map.put("longitude",longi);
            map.put("address_type",addresstype);
            map.put("notes",notes.getText().toString());
            OnlineRequest.editDropAddressRequest(AddAddress.this,map);
        }
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

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(Utils.global.mGoogleApiClient_address, placeId);
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

            Geocoder geocoder = new Geocoder(AddAddress.this);
            try
            {
                List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude,1);

                if (addresses!=null && addresses.size()>0)
                {
                    if (addresses.get(0).getAddressLine(0) != null)
                    {
                        String fulladdress = addresses.get(0).getAddressLine(0).toString();
                        Log.d("fulladdress", fulladdress);
                        // city.setText(cityname);

                        street_two.setText(fulladdress);
                    }

                    if (addresses.get(0).getLocality() != null)
                    {
                        String cityname = addresses.get(0).getLocality();
                        Log.d("city", cityname);
                        // city.setText(cityname);

                        city_drop.setText(cityname);
                    }

                    if (addresses.get(0).getAdminArea() != null)
                    {
                        String statename = addresses.get(0).getAdminArea();
                        // state.setText(statename);
                        Log.d("state", statename);

                        state_drop.setText(statename);
                    }

                    if (addresses.get(0).getPostalCode() != null)
                    {
                        String zipname = addresses.get(0).getPostalCode();
                        //Log.d("country", zipname);
                        zip_drop.setText(zipname);
                    }

                    if (addresses.get(0).getCountryName() != null)
                    {
                        String mcountry = addresses.get(0).getCountryName();
                        //Log.d("country", mcountry);
                        // country=mcountry;
                        //Utils.e("map======="+"105========",country);
                    }
                }
                else
                {
                    new Utils(AddAddress.this);
                    Utils.toastTxt("No location found with these address.",AddAddress.this);
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

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.add:
                if (!eid.equalsIgnoreCase(""))
                {

                    editDropAddress(eid);
                }
                else
                {
                    submit();
                }

                break;
            case R.id.back:
                finish();
                break;
        }
    }
    private void submit()
    {
        new Utils(AddAddress.this);

        if (street_one.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter street for drop location.",AddAddress.this);
        }
        else if (street_two.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter full drop address.",AddAddress.this);
        }
        else if (city_drop.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter drop location city.",AddAddress.this);
        }
        else if (state_drop.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter drop location state.",AddAddress.this);
        }
        else if (zip_drop.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter drop location zipcode.",AddAddress.this);
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
            map.put("type",type);
            OnlineRequest.addDropAddressRequest(AddAddress.this,map);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        mPlaceArrayAdapter.setGoogleApiClient(Utils.global.mGoogleApiClient_address);
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
    public void onDestroy()
    {
        super.onDestroy();

        if (Utils.global.mGoogleApiClient_address!=null)
        {
            if (Utils.global.mGoogleApiClient_address.isConnected())
            {
                Utils.global.mGoogleApiClient.stopAutoManage(AddAddress.this);
                Utils.global.mGoogleApiClient.disconnect();
            }
        }
        eid="";
    }

    public  void closeActivity()
    {
        finish();
    }


}
