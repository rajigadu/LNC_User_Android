package com.latenightchauffeurs.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.StrictMode;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.Settings;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.model.SavePref;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    CallbackManager mCallbackManager;
//    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = Login.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.signin)
    Button signin;

    @BindView(R.id.signup)
    TextView signup;

    @BindView(R.id.forgot)
    TextView forgot;

    @BindView(R.id.noaccount)
    TextView tvNoAccount;

    HashMap<String, Object> map;
    public static Login Instance;

    @BindView(R.id.fb)
    ImageView fb;
    @BindView(R.id.goo)
    ImageView goo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //Toast.makeText(Login.this, "success", Toast.LENGTH_LONG).show();
                        //Log.e("Success",loginResult.toString());
                        RequestData();
                    }

                    @Override
                    public void onCancel() {
                        // Toast.makeText(Login.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // Toast.makeText(Login.this, exception.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Instance = this;

        signin.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgot.setOnClickListener(this);
        fb.setOnClickListener(this);
        goo.setOnClickListener(this);
        tvNoAccount.setOnClickListener(Login.this);
        tvNoAccount.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);





        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.EMAIL))
                .requestEmail()
                .build();

//        mGoogleApiClient = new GoogleApiClient.Builder(Login.this)
//                .enableAutoManage(Login.this, (GoogleApiClient.OnConnectionFailedListener) Login.this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:
                Utils.startActivity(Login.this, SignUp.class);
                break;
            case R.id.forgot:
                Utils.startActivity(Login.this, Forgot.class);
                break;
            case R.id.signin:
                submit();
                break;
            case R.id.fb:
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("email"));
                break;
            case R.id.goo:
                //signIn();
                break;
            case R.id.noaccount:
                signup.performClick();
                break;
        }
    }

    public void submit() {
        // Utils.startActivity(ActivityLogin.this,ActivityEvents.class);
        new Utils(Login.this);

        if (email.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter email address.", Login.this);
        } else if (!Utils.isValidEmail(email.getText().toString().trim())) {
            Utils.toastTxt("Please enter valid email address.", Login.this);
        } else if (password.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter password.", Login.this);
        } else {
            map = new HashMap<>();
            map.put("emailid", email.getText().toString());
            map.put("password", password.getText().toString());
            OnlineRequest.loginRequest(Login.this, map);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void closeActivity() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.e("outside","1234567");
        super.onActivityResult(requestCode, resultCode, data);

        // Log.e("outside","123");

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Log.e("inside", "123");
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//                handleSignInResult(result);
            }
        } else {
            if (mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
                return;
            }

            // Use Facebook callback manager here
            // CallbackManager::onActivityResult(requestCode::int, resultCode::int, data::Intent);
        }
    }

    public void RequestData() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        System.out.println("Json getToken :" + accessToken.getToken());

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject();
                Log.e("fbJsondata :", json.toString());

                try {
                    String id = json.getString("id");
                    String name = json.getString("name");
                    String email = json.getString("email");
                    JSONObject picture = json.getJSONObject("picture");
                    JSONObject data = picture.getJSONObject("data");
                    String url = data.getString("url");

                    //Utils.e("1234567"+"127", "jhgkyjghjghghjgjh "+url);
                    //Utils.e("1234567"+"127", "jhgkyjghjghghjgjh "+picture);
                    //Toast.makeText(Login.this,email,Toast.LENGTH_LONG).show();

                    SavePref pref1 = new SavePref();
                    pref1.SavePref(Login.this);
                    pref1.setsocialmail(email);
                    pref1.setUserFName(name);
                    pref1.setImage(url);

                    checkemailvalidity("facebook");
                    /*new Utils(Login.this);
                    Utils.startActivity(Login.this,FBSignup.class);
*/
                    if (name.contains(" ")) {
                        String nameSplit[] = name.split(" ");
                        // myIds(id, nameSplit[0], nameSplit[1], email, url, "fb");
                    } else {
                        //myIds(id, name, "", email, url, "fb");
                    }
                } catch (Exception e) {

                }

                //  new ShowMsg().createDialog(Login.this,  "data = "+json);
                // myIds(id, first_name, last_name, email, imageUrl, "1");
            }
        });

        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,link,email,picture,birthday,gender,hometown");
        parameters.putString("fields", "id,name,email,gender,birthday,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void checkemailvalidity(String type) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(Login.this);
        String fmail = pref1.getsocilamail();
        //String uname=pref1.getUserName();

        new Utils(Login.this);
        Utils.global.mapMain();
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_SOCIAL_STATUS);
        Utils.global.mapMain.put("emailid", fmail);
        //Utils.global.mapMain.put(ConstVariable.NAME,uname);
        //Utils.global.mapMain.put(ConstVariable.TYPE,type);

        if (Utils.isNetworkAvailable(Login.this)) {
            JsonPost.getNetworkResponse(Login.this, null, Utils.global.mapMain,
                    ConstVariable.SocialStatus);
        } else {
            Utils.showInternetErrorMessage(Login.this);
        }
    }

//    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
//        if (result.isSuccess()) {
//            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();
//
//            Log.e(TAG, "display name: " + acct.getDisplayName());
//
//            String personName = acct.getDisplayName();
//            String email = acct.getEmail();
//            String url = String.valueOf(acct.getPhotoUrl());
//
//
//            Log.e(TAG, "Name: " + personName + ", email: " + email + ", Image123: " + url);
//
//            //Toast.makeText(Login.this,email,Toast.LENGTH_LONG).show();
//
//           /* txtName.setText(personName);
//            txtEmail.setText(email);
//            Glide.with(getApplicationContext()).load(personPhotoUrl)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imgProfilePic);*/
//
//            // updateUI(true);
//
//            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                    new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(Status status) {
//
//                        }
//                    });
//
//            SavePref pref1 = new SavePref();
//            pref1.SavePref(Login.this);
//            pref1.setsocialmail(email);
//            pref1.setUserFName(personName);
//            pref1.setImage(url);
//
//            checkemailvalidity("googleplus");
//        } else {
//            // Signed out, show unauthenticated UI.
//            // updateUI(false);
//        }
//    }

//    private void signIn() {
//        try {
//            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//            startActivityForResult(signInIntent, RC_SIGN_IN);
//        } catch (Exception e) {
//            Log.e("error123", e.getMessage());
//        }
//    }

    public void registrationIDtoServer() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("lnctoken", 0);
        SharedPreferences.Editor editor = pref.edit();

        String tId = pref.getString("tokenid", null);

        SavePref pref1 = new SavePref();
        pref1.SavePref(Login.this);
        String id = pref1.getUserId();

        if (tId != "") {
            map = new HashMap<>();
            map.put(ConstVariable.USERID, id);
            map.put("devicetoken", tId);
            map.put("device_type", "android");
            OnlineRequest.deviceTokenRequest(Login.this, map);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
