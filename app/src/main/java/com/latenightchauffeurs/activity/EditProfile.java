package com.latenightchauffeurs.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.Settings;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.model.SavePref;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.fname)
    EditText fullNane;

    @BindView(R.id.mobile)
    EditText etMobile;

    @BindView(R.id.lname)
    EditText lname;

    @BindView(R.id.updateprofile)
    Button updateProfile;

    @BindView(R.id.userimage)
    ImageView pic;

    @BindView(R.id.upload)
    TextView imageupload;

    public static EditProfile Instance;

    String picturePath = "";

    public static String TAG = EditProfile.class.getName();
    public static final int RequestCodeCam = 1;
    public static final int MY_REQUEST_CODE = 3;
    public static final int RequestCodeLib = 2;
    public static Bitmap thumbnail = null;
    public static Uri uri;
    public static String fname = "", lnam = "", number = "";
    public static Context context;
    private String userChoosenTask;
    public String id = "";
    HashMap<String, Object> map;

    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};


    ImageLoader imageLoader;
    DisplayImageOptions options;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        ButterKnife.bind(this);

        Instance = this;


        imageLoader = (ImageLoader) Utils.getImageLoaderUser(EditProfile.this).get("imageLoader");
        options = (DisplayImageOptions) Utils.getImageLoaderUser(EditProfile.this).get("options");




        back.setOnClickListener(this);
        imageupload.setOnClickListener(this);
        updateProfile.setOnClickListener(this);

        back.setVisibility(View.VISIBLE);

        title.setText("Edit Profile");
        context = EditProfile.this;

        SavePref pref1 = new SavePref();
        pref1.SavePref(EditProfile.this);
        String fname = pref1.getUserFName();
        String llname = pref1.getUserLName();
        String unum = pref1.getMobile();
        String imag = pref1.getImage();

        if (!imag.toString().equalsIgnoreCase("")) {

            Log.e(TAG , "imagvvvv "+imag);

            //Picasso.with(EditProfile.this).load(imag).into(pic);
            Picasso.with(context).load(Settings.URLIMAGEBASE + imag).placeholder(R.drawable.appicon).into(pic);
            pic.setScaleType(ImageView.ScaleType.FIT_XY);


//            Glide.with(context)
//                    .load(Settings.URLIMAGEBASE + imag)
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(pic);
//
           // imageLoader.displayImage(Settings.URLIMAGEBASE + imag, pic, options);



//            Glide.with(fragment)
//                    .load(url)
//                    .CircleCrop()
//                    .into(imageView);


//            Glide.with(context).load("").asBitmap().centerCrop().into(new BitmapImageViewTarget(pic) {
//                @Override
//                protected void setResource(Bitmap resource) {
//                    RoundedBitmapDrawable circularBitmapDrawable =
//                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//                    circularBitmapDrawable.setCircular(true);
//                    pic.setImageDrawable(circularBitmapDrawable);
//                }
//            });
        }

        if (!fname.toString().equalsIgnoreCase("")) {
            fullNane.setText(fname);
        }
        if (!unum.toString().equalsIgnoreCase("")) {
            etMobile.setText(unum);
        }
        if (!llname.toString().equalsIgnoreCase("")) {
            lname.setText(llname);
        }



        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "imagAAAA "+picturePath);
                if(picturePath.equalsIgnoreCase("")){
                    Intent intent = new Intent(EditProfile.this , ImageZoom.class);
                    intent.putExtra("key" , "1");
                    intent.putExtra("imageLink" , ""+Settings.URLIMAGEBASE + imag);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(EditProfile.this , ImageZoom.class);
                    intent.putExtra("key" , "2");
                    intent.putExtra("imageLink" , ""+picturePath);
                    startActivity(intent);
                }
            }
        });

    }

    public void pickProfileImage() {
        LayoutInflater inflater = LayoutInflater.from(EditProfile.this);
        final View dialogLayout = inflater.inflate(R.layout.activity_imagepicker, null);
        final AlertDialog builder = new AlertDialog.Builder(EditProfile.this).create();
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setView(dialogLayout);
        builder.show();

        final TextView Choosegallary = (TextView) dialogLayout.findViewById(R.id.gallary);
        final TextView Takephoto = (TextView) dialogLayout.findViewById(R.id.takephoto);
        final TextView Deletephoto = (TextView) dialogLayout.findViewById(R.id.cancel);

        dialogLayout.post(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.ZoomIn).duration(500).playOn(dialogLayout);
            }
        });

        Takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // boolean result = Utility.checkPermission(EditProfile.this);
                builder.dismiss();
                userChoosenTask = Takephoto.getText().toString();

                if (!hasPermissions(EditProfile.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(EditProfile.this, PERMISSIONS, MY_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, RequestCodeCam);
                }

                /*if (result)
                {
                    if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(EditProfile.this,new String[]{Manifest.permission.CAMERA},MY_REQUEST_CODE);
                    }
                    else
                    {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,RequestCodeCam);
                    }
                }*/
            }
        });
        Choosegallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                userChoosenTask = Choosegallary.getText().toString();

                if (!hasPermissions(EditProfile.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(EditProfile.this, PERMISSIONS, MY_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RequestCodeLib);
                }
            }
        });

        Deletephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // profileImageDelete();
                builder.dismiss();
            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take a Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, RequestCodeCam);
                    } else if (userChoosenTask.equals("Choose Gallary")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, RequestCodeLib);
                    }
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodeCam:
                    try {
                        thumbnail = (Bitmap) data.getExtras().get("data");
                        uri = saveImageBitmap(thumbnail);
                        picturePath = getRealPathFromURI(uri);
                        pic.setImageBitmap(thumbnail);
                        uri = Uri.fromFile(new File(compressImage(picturePath)));
                    } catch (Exception e) {
                        Log.e("error123", e.getMessage());
                    }
                    break;
                case RequestCodeLib:
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = context.getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    picturePath = c.getString(columnIndex);
                    c.close();

                    try {
                        thumbnail = (BitmapFactory.decodeFile(picturePath));
                        //   Log.e("gallery.***********692." + thumbnail,picturePath);
                        // uri = Uri.fromFile(new File(picturePath));
                        uri = Uri.fromFile(new File(compressImage(picturePath)));
                        pic.setImageBitmap(thumbnail);
                    } catch (Exception e) {
                        // Log.e("gallery***********692.", "Exception==========Exception==============Exception");
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public static Uri saveImageBitmap(Bitmap bitmap) {
        String strDirectoy = context.getFilesDir().getAbsolutePath();
        //   Utils.e(Tag+"4149", ""+strDirectoy);
        String imageName = "usericon.PNG";

        OutputStream fOut = null;
        File file = new File(strDirectoy, imageName);
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }

    public void submit() {
        new Utils(EditProfile.this);

        if (fullNane.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter full name.", EditProfile.this);
        } else if (etMobile.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter mobile number.", EditProfile.this);
        } else if (etMobile.getText().toString().trim().length() < 10) {
            Utils.toastTxt("Please enter 10 digit mobile number.", EditProfile.this);
        } else {
            map = new HashMap<>();
            Utils.global.mapMain();
            map.put(ConstVariable.USERID, id);
            map.put("fname", fullNane.getText().toString().trim());
            map.put("mobile", etMobile.getText().toString().trim());
            map.put("lname", lname.getText().toString().trim());
            map.put("profilepic", thumbnail);
            OnlineRequest.editProfileRequest(EditProfile.this, uri, map);

            fname = fullNane.getText().toString().trim();
            lnam = lname.getText().toString().trim();
            number = etMobile.getText().toString().trim();
        }
    }

    public String compressImage(String imageUri) {
        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        options.inJustDecodeBounds = false;

        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {

            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);


            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }


    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.upload:
                if (!hasPermissions(EditProfile.this, PERMISSIONS)) {
                    showAlertDialog();
                } else {
                    pickProfileImage();
                }
                break;
            case R.id.updateprofile:
                submit();
                break;
        }

    }
    private void showAlertDialog() {
        try {
            String appName = getPackageManager().getApplicationLabel(
                    getPackageManager().getApplicationInfo(
                            getPackageName(),PackageManager.GET_META_DATA)).toString();
            AlertDialog.Builder dialog = new AlertDialog.Builder((this));
            dialog.setTitle( appName +
                    " Would Like to Access Your Photos");
            dialog.setMessage(appName + " only will upload photo you choose");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Allow", (dialog1, which) -> {
                dialog1.dismiss();
                pickProfileImage();
            });

            dialog.setNegativeButton("Don't Allow", (dialog12, which) -> dialog12.dismiss());
            dialog.show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}
