package com.latenightchauffeurs.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.latenightchauffeurs.R;


/**
 * Created by AnaadIT on 12/14/2017.
 */

public class ImageZoom extends AppCompatActivity {
    private static final String TAG = "ImageZoom";


    ImageView imageViewDelete;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_zoom);

        imageViewDelete = (ImageView) findViewById(R.id.imageViewDelete);


        Bundle bundle = getIntent().getExtras();
        String key = bundle.getString("key");
        String imageLink = bundle.getString("imageLink");



        final PhotoView photoView = findViewById(R.id.photo_view);

        if(key.equalsIgnoreCase("1")){
            RequestOptions options = new RequestOptions();
            options.fitCenter().placeholder(getResources().getDrawable(R.drawable.profile));
            Glide.with(this)
                    .asBitmap()
                    .load(imageLink)
                    .apply(options)
                    .into(photoView);
        }else{
                         RequestOptions options = new RequestOptions();
                        options.fitCenter().placeholder(getResources().getDrawable(R.drawable.profile));
                        Glide.with(this)
                                .asBitmap()
                                .load("file:///"+imageLink)
                                .apply(options)
                                .into(photoView);
        }

//        Picasso.with(this)
//                .load("http://pbs.twimg.com/media/Bist9mvIYAAeAyQ.jpg")
//                .into(photoView);
       // imageLoader.displayImage(imageLink , photoView, options);



        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        onStart(3);
//    }
//
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        onStop(3);
//    }
//

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult "+ requestCode);
        if(requestCode == 13 && resultCode == 3 && data != null) { // request
            Log.e(TAG, "onPostExecutelocationNOtification ");
        }
    }

}
