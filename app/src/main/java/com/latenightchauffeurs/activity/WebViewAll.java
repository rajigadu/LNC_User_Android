package com.latenightchauffeurs.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.latenightchauffeurs.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewAll extends AppCompatActivity
{
    @BindView(R.id.webview1)
    WebView myWebView;

    @BindView(R.id.progressBar1)
    ProgressBar progressBar;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);


        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);

        title.setText("Forgot Password");
        myWebView = (WebView) findViewById(R.id.webview1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        back.setVisibility(View.VISIBLE);

        myWebView.setWebViewClient(new WebViewClient());

        myWebView.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView view, int progress)
            {
                if (progress < 100)
                {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }
                else if (progress == 100)
                {
                    progressBar.setVisibility(ProgressBar.GONE);
                  //  Utils.e("weburl 45", "URL=" + myWebView.getUrl());
                    String Urlpath = myWebView.getUrl();
                  //  Utils.e("weburl 42", "URL=" + Urlpath.substring(Urlpath.lastIndexOf("/") + 1));
//                    if(Urlpath.substring(Urlpath.lastIndexOf("/") + 1).toString().equalsIgnoreCase("success")){
//                        finish();
//                    }
                }
                progressBar.setProgress(progress);
            }
        });
        back.setVisibility(View.VISIBLE);

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            String googleUrl = extras.getString("webpage");

            title.setText(extras.getString("pageheading"));
            if(googleUrl != null) myWebView.loadUrl(googleUrl);
           // Toast.makeText(WebViewAll.this,googleUrl,Toast.LENGTH_LONG).show();
        }
        myWebView.getSettings().setJavaScriptEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        finishAffinity();
        super.onBackPressed();
    }
}
