package com.latenightchauffeurs.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.dbh.base.BaseActivity;
import com.latenightchauffeurs.dbh.fragments.DbhRideInfoFragment;
import com.latenightchauffeurs.fragment.BookReservation_new;
import com.latenightchauffeurs.fragment.Cards;
import com.latenightchauffeurs.fragment.FRides;
import com.latenightchauffeurs.fragment.Home;
import com.latenightchauffeurs.fragment.Rides;
import com.latenightchauffeurs.fragment.Settings;
import com.latenightchauffeurs.model.ItemBanner;
import com.latenightchauffeurs.model.SavePref;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Navigation extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, Serializable,
        Home.OnFragmentInteractionListenerHome, Home.SendData, BookReservation_new.OnFragmentInteractionListenerBooking {


    private BadgeStyle style = ActionItemBadge.BadgeStyles.RED.getStyle();
    private BadgeStyle bigStyle = ActionItemBadge.BadgeStyles.DARK_GREY_LARGE.getStyle();


    Menu menuChoose = null;

    private static final int REFRESH_REQUEST = 122;


    private static final String TAG = "Navigation";
    String title = "";
    public static int nid = 0;
    public static Navigation Instance;
    public static boolean BookNowBack = true, BookNewNowBack = true;
    boolean checkBack = false;
    List<Fragment> fragmentList = new ArrayList<Fragment>();
    public static boolean isBooking = false;
    public static ImageView adUnitBanner;
    static int bannerLeftCounter = 0;
    static int delayLeftTime = 0;
    static Handler handler;
    public static Context mcontext;
    static List<ItemBanner> adBanners = new ArrayList<>();
    static TextView fRide_Time;
    private RelativeLayout rlBottomTabs;

    private HashMap<String, Object> notifHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        rlBottomTabs = findViewById(R.id.bottom_rl);
        rlBottomTabs.setVisibility(View.GONE);

        adUnitBanner = (ImageView) findViewById(R.id.adunit);
        fRide_Time = (TextView) findViewById(R.id.status_fride);

        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(Navigation.this, DividerItemDecoration.VERTICAL));

        Instance = this;
        isBooking = false;
        mcontext = this;
        handler = new Handler();

        View header = navigationView.getHeaderView(0);

        ImageView pic = header.findViewById(R.id.imageView);
        TextView username = header.findViewById(R.id.username);
        TextView mobile = header.findViewById(R.id.mobile);

        SavePref pref1 = new SavePref();
        pref1.SavePref(Navigation.this);
        String fname = pref1.getUserFName();
        String llname = pref1.getUserLName();
        String unum = pref1.getMobile();
        String imag = pref1.getImage();

        if (!imag.toString().equalsIgnoreCase("")) {
            //Picasso.with(EditProfile.this).load(imag).into(pic);
            Picasso.with(Navigation.this).load(com.latenightchauffeurs.Utils.Settings.URLIMAGEBASE +
                    imag).placeholder(R.drawable.appicon).into(pic);
        }

        StringBuilder sb = new StringBuilder();

        if (!fname.toString().equalsIgnoreCase("")) {
            sb.append(fname);
        }
        if (!unum.toString().equalsIgnoreCase("")) {
            mobile.setText(unum);
        }
        if (!llname.toString().equalsIgnoreCase("")) {
            sb.append(" ");
            sb.append(llname);
            username.setText(sb);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        printHashKey(this);

        navigationView.setNavigationItemSelectedListener(this);

        title = getString(R.string.breservation);
        fragmentList.clear();

        fragmentList.add(Fragment.instantiate(this, Home.class.getName()));
        fragmentList.add(Fragment.instantiate(this, Rides.class.getName()));
        fragmentList.add(Fragment.instantiate(this, FRides.class.getName()));
        fragmentList.add(Fragment.instantiate(this, Cards.class.getName()));
        //fragmentList.add(Fragment.instantiate(this, ServiceInfo.class.getName()));
        // fragmentList.add(Fragment.instantiate(this, DropAddressList.class.getName()));
        fragmentList.add(Fragment.instantiate(this, Settings.class.getName()));
        fragmentList.add(Fragment.instantiate(this, BookReservation_new.class.getName()));

        if (nid == 1) {
            getFragment(1);
        }else if (nid == 2) {
            getFragment(2);
        } else if (nid == 3) {
            getFragment(3);
        } else if (nid == 4) {
            getFragment(4);
        } else {
            getFragment(0);
        }

        // String rstatus=pref1.getRatingStatus();
        // String rmap=pref1.getRatingmap();

       /* if(rstatus.equalsIgnoreCase(""))
        {
            if (!rstatus.equalsIgnoreCase(""))
            {
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>()
                {
                }.getType();
                HashMap<String, Object> map = gson.fromJson(rmap,type);

                Intent i=new Intent(Navigation.this,Rating.class);
                i.putExtra("map",(Serializable)map);
                startActivity(i);
            }
        }*/

        adUnitBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adBanners.size() > 0) {
                    if (!adBanners.get(bannerLeftCounter).getLink().toString().equalsIgnoreCase("")) {
                        String urlS = adBanners.get(bannerLeftCounter).getLink();

                       /* Intent i = new Intent(Navigation.this,WebViewAll.class);
                        i.putExtra("webpage", urlS);
                        i.putExtra("pageheading","");
                        startActivity(i);*/

                        try {
                            Uri webpage = Uri.parse(urlS);

                            if (!urlS.startsWith("http://") && !urlS.startsWith("https://")) {
                                webpage = Uri.parse("http://" + urlS);
                            }

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, webpage);
                            startActivity(browserIntent);
                        } catch (Exception e) {
                            // TODO: handle exception
                            // String data = e.getMessage();
                        }
                    }
                }
            }
        });
        if (getIntent().getExtras() != null) {
            notifHashMap = (HashMap<String, Object>) getIntent().getExtras().getSerializable("map");
            if (notifHashMap != null) {
                if (notifHashMap.containsKey("ride") && notifHashMap.get("ride").toString().
                        equalsIgnoreCase("future complete")) {
                    Intent ii = new Intent(this, Rating.class);
                    ii.putExtra("map", (Serializable) notifHashMap);
                    startActivity(ii);
                }
            }
        }


        //handler.postDelayed(runnableBanner, delayLeftTime);

        //HashMap<String, Object> map = new HashMap<>();
        //HashMap<String, Object> map1 = new HashMap<>();

       /* map.put("id","112");
        map.put("advertisement_id","17");
        map.put("advertisement_type","6");
        map.put("location_id","43");
        map.put("fee","150");
        map.put("name","Kyle Kelly");
        map.put("email","kkellyny@gmail.com");
        map.put("banner_logo","http://www.qwikrides.com/admin/advertisement_banner_logo/ypYsDAgatnrLmmovetopatchogue.png");
        map.put("big_banner","null");
        map.put("pdf_logo","ypYsDAgatnrLmmovetopatchogue.png");
        map.put("url","http://www.movetopatchogue.com");
        map.put("delay","10000");
        map.put("etMobile","6312891400");
        map.put("description","null");
        map.put("recieved","1");
        map.put("expire_date","2018-07-29");
        map.put("status","2");
        map.put("validity_status","3");
        map.put("dat","2018-06-29 08:25:45");

        map1.put("id","30");
        map1.put("advertisement_id","17");
        map1.put("advertisement_type","6");
        map1.put("location_id","41");
        map1.put("fee","150");
        map1.put("name","Dan Cantelmo");
        map1.put("email","info@lncli.com");
        map1.put("banner_logo","http://qwikrides.com/admin/advertisement_banner_logo/logo-1.png");
        map1.put("big_banner","null");
        map1.put("pdf_logo","ypYsDAgatnrLmmovetopatchogue.png");
        map1.put("url","http://latenightchauffeurs.com");
        map1.put("delay","10000");
        map1.put("etMobile","6312891400");
        map1.put("description","null");
        map1.put("recieved","1");
        map1.put("expire_date","2018-07-29");
        map1.put("status","2");
        map1.put("validity_status","3");
        map1.put("dat","2018-06-29 08:25:45");*/

        //Utils.global.bannerslist.add(map);
        //Utils.global.bannerslist.add(map1);
        //loaddata(Utils.global.bannerslist);

        handler.postDelayed(runnableBanner, 2000);

        Utils.startPowerSaverIntent(this);
        Utils.ifHuaweiAlert(this);
    }

    private Runnable runnableBanner = new Runnable() {
        @Override
        public void run() {
            adsRequest();
        }
    };

    public void adsRequest() {
        //String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

        new Utils(Navigation.this);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("datetime", df.format(c));
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_BANNER_ADS);

        if (Utils.isNetworkAvailable(Navigation.this)) {
            JsonPost.getNetworkResponse(Navigation.this, null, Utils.global.mapMain, ConstVariable.AdsList);
        } else {
            Utils.showInternetErrorMessage(Navigation.this);
        }
    }

    public static void loaddata(List<HashMap<String, Object>> data) {
        Utils.e("" + "85", "data=1111111111=== " + data);

        adBanners = getItemBanner(data);

        if (data != null) {
            adUnitBanner.setVisibility(View.VISIBLE);
            Picasso.with(mcontext).load(com.latenightchauffeurs.Utils.Settings.URLIMAGEBASEADS +
                    adBanners.get(bannerLeftCounter).getUrl()).placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder).into(adUnitBanner);

            // int timeDelay = 2000;
            try {
                delayLeftTime = Integer.parseInt(adBanners.get(bannerLeftCounter).getDelay());
            } catch (Exception e) {

            }
            handler.postDelayed(runnableCurrentAds, delayLeftTime);
            fRide_Time.setVisibility(View.GONE);
        } else {
            adUnitBanner.setVisibility(View.GONE);
        }
    }

    private static Runnable runnableCurrentAds = new Runnable() {
        @Override
        public void run() {
            final int itemBannerLeftBanner = adBanners.size() - 1;

            if (bannerLeftCounter == itemBannerLeftBanner) {
                bannerLeftCounter = 0;
                Picasso.with(mcontext).load(com.latenightchauffeurs.Utils.Settings.URLIMAGEBASEADS + adBanners.get(bannerLeftCounter).getUrl())
                        .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(adUnitBanner);
            } else {
                bannerLeftCounter++;
                Picasso.with(mcontext).load(com.latenightchauffeurs.Utils.Settings.URLIMAGEBASEADS + adBanners.get(bannerLeftCounter).getUrl())
                        .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(adUnitBanner);
            }
            handler.postDelayed(runnableCurrentAds, delayLeftTime);
        }
    };

    public static ArrayList<ItemBanner> getItemBanner(List<HashMap<String, Object>> data) {
        ArrayList<ItemBanner> arrayList = new ArrayList<>();

        try {
            for (int i = 0; i < data.size(); i++) {
                String id = data.get(i).get("id").toString();
                String image = data.get(i).get("banner_logo").toString();
                String url = data.get(i).get("url").toString();
                String delay = data.get(i).get("delay").toString();

                ItemBanner itemBanner = new ItemBanner();
                itemBanner.setId(id);
                itemBanner.setUrl(image);
                itemBanner.setLink(url);
                itemBanner.setDelay(delay);
                arrayList.add(itemBanner);
            }
        } catch (Exception e) {

        }
        return arrayList;
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e("keyhash1234545666", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("", "printHashKey()", e);
        }
    }

    private static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    public static void assignServiceTime(HashMap<String, Object> data) {
        if (data != null) {
            if (data.containsKey("time_left") && !data.get("time_left").toString().equalsIgnoreCase("")) {
                fRide_Time.setVisibility(View.VISIBLE);
                fRide_Time.setText(Html.fromHtml("Your next reservation is in :<br /> " + data.get("time_left").toString()));
                fRide_Time.setTextColor(Color.WHITE);
            }
        } else {
            fRide_Time.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_reservation) {
            getFragment(0);
            // Handle the camera action
            adsRequest();
        } else if (id == R.id.nav_bookings) {
            getFragment(1);
        }

        else if (id == R.id.nav_cards) {
            getFragment(3);
        }

        else if (id == R.id.nav_settings) {
            getFragment(4);
        }

        else if (id == R.id.nav_logout) {
            getFragment(5);
        }

        else if (id == R.id.nav_dbh_bookings) {
            getFragment(6);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public Fragment getFragment(Integer navItemIndex) {
        Fragment fragment = null;

        title = getString(R.string.app_name);
        Utils.hideSoftKeyboard(this);
        rlBottomTabs.setVisibility(View.GONE);

        Log.e(TAG , "getFragmentVVVVVVVV "+navItemIndex);


        switch (navItemIndex) {

            case 0:
                rlBottomTabs.setVisibility(View.VISIBLE);
                title = getString(R.string.breservation);
                fragment = new Home();
                fragmenttransactions(fragment);
                break;

            case 1:
                rlBottomTabs.setVisibility(View.GONE);
                title = getString(R.string.bookings);
                fragment = new Rides();
                fragmenttransactions(fragment);
                break;

            case 2:
                rlBottomTabs.setVisibility(View.GONE);
                title = getString(R.string.futurerides);
                fragment = new FRides();
                fragmenttransactions(fragment);
                break;
            case 3:
                rlBottomTabs.setVisibility(View.GONE);
                title = getString(R.string.cards);
                fragment = new Cards();
                fragmenttransactions(fragment);
                break;
          /*  case 3:
                title = getString(R.string.service);
                fragment= new ServiceInfo();
                fragmenttransactions(fragment);
                break;*/
            case 4:
                rlBottomTabs.setVisibility(View.GONE);
                title = getString(R.string.settings);
                fragment = new Settings();
                fragmenttransactions(fragment);
                break;
           /* case 4:
                title = getString(R.string.payments);
                fragment= new PaymentHistory();
                fragmenttransactions(fragment);
                break;*/
            case 5:
                logoutRequest();
                break;
            case 6:
                rlBottomTabs.setVisibility(View.GONE);
                title = getString(R.string.dbh_booking);
                fragment = new DbhRideInfoFragment();
                fragmenttransactions(fragment);
                break;
            default:
                break;
        }
        return fragment;
    }

    public void logoutRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(this);
        String id = pref1.getUserId();

        Utils.global.mapMain();
        Utils.global.mapMain.put(ConstVariable.USERID, id);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_LOGOUT);

        if (Utils.isNetworkAvailable(Navigation.this)) {
            JsonPost.getNetworkResponse(Navigation.this, null, Utils.global.mapMain, ConstVariable.Logout);
        } else {
            Utils.showInternetErrorMessage(Navigation.this);
        }
    }

    public void afterLogoutStatus() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(this);
        pref1.clear();

        SharedPreferences preferences = getSharedPreferences("ProtectedApps", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

        disconnectFromFacebook();
        Utils.toastTxt("You Logout Successfully", Navigation.this);
        com.latenightchauffeurs.Utils.Settings.NETWORK_STATUS = "";
        com.latenightchauffeurs.Utils.Settings.USERNAME = "";
        Utils.startActivity(Navigation.this, Login.class);
        finish();
    }

    private void fragmenttransactions(final Fragment fragment) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    String backStateName = fragment.getClass().getName();
                    FragmentManager manager = getSupportFragmentManager();
                    boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

                    if (!fragmentPopped) { //fragment not in checkBack stack, create it.
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.add(R.id.frame, fragment, backStateName);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.addToBackStack(backStateName);
                        ft.commit();
                        //android.app.Fragment fragment = getFragmentManager().findFragmentByTag(backStateName);
                        //Log.e("frag___123",fragment.getTag());
                    }
                    getSupportActionBar().setTitle(title);
                } catch (Exception e) {
                    Log.e("error123", e.getMessage());
                }
            }
        }, 250);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        Utils.hideSoftKeyboard(this);

        if (fragment != null && fragment instanceof BookReservation_new) {
            BookReservation_new bookReservationNew = (BookReservation_new) fragment;
            bookReservationNew.checkVisibility();
        }

        if (fragment instanceof Home) {
            rlBottomTabs.setVisibility(View.VISIBLE);
        } else {
            rlBottomTabs.setVisibility(View.GONE);
        }

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 1) {
            Utils.e("108", "onBackPressed finished");

            if (checkBack) {
                finish();
            } else {
                Utils.toastTxt("Please press once again to go into background.", Navigation.this);
            }
            checkBack = true;
           // rlBottomTabs.setVisibility(View.VISIBLE);
        } else {
            Utils.e("112", "onBackPressed");



            if (BookNowBack == true) {
                Log.e(TAG, "countqqqq "+count);
//
//               String fragName = fragment.getClass().getSimpleName();
//                if (fragName.equalsIgnoreCase("Home")) {
                title = getString(R.string.breservation);
                getSupportActionBar().setTitle(title);
                    rlBottomTabs.setVisibility(View.VISIBLE);
//                } else {
//                    rlBottomTabs.setVisibility(View.GONE);
//                }
//                Log.e(TAG, "countqqqqDDD "+fragName);

                super.onBackPressed();
            }else{
                Log.e(TAG, "countqqqq ELSE "+count);
                rlBottomTabs.setVisibility(View.GONE);
               // super.onBackPressed();
                //getFragment(0);

            }
        }



    }

    public void checkBottomTabsVisibility(boolean isVisible) {
        //rlBottomTabs.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onFragmentInteraction(int page) {
        if (page == ConstVariable.Login) {
            title = "";
            fragmenttransactions(fragmentList.get(3));
        } else if (page == ConstVariable.BookReseration) {
            title = getString(R.string.breservation);
            fragmenttransactions(fragmentList.get(5));
        } else if (page == ConstVariable.ChangePassword) {
            title = getString(R.string.breservation);
            fragmenttransactions(fragmentList.get(0));
            isBooking = true;
        }

        rlBottomTabs.setVisibility(View.GONE);
    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }

    @Override
    public void sendData(HashMap<String, Object> smap) {
        try {
            String tag = BookReservation_new.class.getName();
            BookReservation_new f = (BookReservation_new) getSupportFragmentManager().findFragmentById(R.id.frame);

            // android.app.Fragment fragment = getFragmentManager().findFragmentByTag(tag);

            if (f != null)
                f.displayReceivedData(smap);
        } catch (Exception e) {
            // Log.e("error123",e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment page = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (page != null){
            page.onActivityResult(requestCode, resultCode, data);
        }


        if(requestCode == REFRESH_REQUEST){
            updateBadge();
        }
    }

    HashMap<String, Object> map;

    public void registrationIDtoServer() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("lnctoken", 0);
        SharedPreferences.Editor editor = pref.edit();

        String tId = pref.getString("tokenid", null);

        SavePref pref1 = new SavePref();
        pref1.SavePref(Navigation.this);
        String id = pref1.getUserId();

        if (tId != "") {
            map = new HashMap<>();
            map.put(ConstVariable.USERID, id);
            map.put("devicetoken", tId);
            map.put("devicetype", "android");
            OnlineRequest.deviceTokenRequest(Navigation.this, map);
        }
    }

    public final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String isid = intent.getStringExtra("isid");

                // Log.d("", "Notification Message Body: " + "oijjj");

                if (isid.equalsIgnoreCase("9")) {
                    String status = intent.getStringExtra("status");

                    map = new HashMap<>();
                    map = (HashMap<String, Object>) intent.getSerializableExtra("data");


                    if (map.containsKey("user") && map.get("user").toString().equalsIgnoreCase("richnotification")) {

                        Log.e(TAG , "richnotification DDDD ");

                        updateBadge();
                    }


                   /* if(map.containsKey("ride")&&map.get("ride").toString().equalsIgnoreCase("Cancel"))
                    {
                        getFragment(0);
                        if(map!=null&&map.size()>0)
                        {
                            if(map.containsKey("reason"))
                            {
                                if(map.get("reason").toString().equalsIgnoreCase(""))
                                {
                                    showCancelRideStatusDialog(map.get("message").toString(),"",Navigation.this);
                                }
                                else
                                {
                                    showCancelRideStatusDialog(map.get("message").toString(),map.get("reason").toString(),Navigation.this);
                                }
                            }
                        }
                    }*/
                }
            }
        }
    };

    public static void showCancelRideStatusDialog(String mtitle, String dlgText, Context mcontext) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog1, null);
        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(mcontext, android.R.style.Theme_Material_Dialog_Alert).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        final RelativeLayout rootlo = (RelativeLayout) dialog.findViewById(R.id.rootlo);
        // rootlo.getLayoutParams().width = getHeightWidth("width") - getHeightWidth("width")/4;
        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView textView = (TextView) dialog.findViewById(R.id.desc);
        final Button buttonok = (Button) dialog.findViewById(R.id.buttonOk);
        // final ImageView iconimage = (ImageView) dialog .findViewById(R.id.imageView);
        // iconimage.setVisibility(View.VISIBLE);
        textView.setText(dlgText);
        title.setText(mtitle);
//        Utils.setFontStyle(context, buttonok);
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {

                } catch (Exception e) {
                    // TODO: handle exception
                } finally {
                    dialog.cancel();
                }
            }
        });
        dialog.show();

       /* dialogLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                YoYo.with(Techniques.ZoomIn).duration(500).playOn(dialogLayout);
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mHandleMessageReceiver, new IntentFilter("OPEN_NEW_ACTIVITY"));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mHandleMessageReceiver);
        Utils.e("101", "destroy");
        super.onDestroy();
    }





    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ic_menu_notifications, menu);
        menuChoose = menu;

        updateBadge();


        return true;
    }



    private void updateBadge() {
        SavePref pref = new SavePref();
        pref.SavePref(Navigation.this);

        if(pref.getBadgeCount() > 0){
            Drawable myDrawable = getResources().getDrawable(R.mipmap.msg_i);
            ActionItemBadge.update(Navigation.this, menuChoose.findItem(R.id.item_bolo_alert_badge), myDrawable, style, pref.getBadgeCount());
        }else{
            Drawable myDrawable = getResources().getDrawable(R.mipmap.msg_i);
            ActionItemBadge.update(Navigation.this, menuChoose.findItem(R.id.item_bolo_alert_badge), myDrawable, style, null);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_bolo_alert_badge:
                startActivityForResult(new Intent(Navigation.this, Notifications.class), REFRESH_REQUEST);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}