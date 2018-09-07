package techxpose.co.allresult;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class ShowResult extends AppCompatActivity {

    private WebView webView;
    ProgressBar bar;
    Button mShowResult;
    private InterstitialAd interstitialAd;
    private AdView mAdView,madView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        setContentView(R.layout.activity_show_result);
        //action perform by this page

        // for advertise pupose

        //MobileAds.initialize(getApplicationContext(), "ca-app-pub-7426325861660851~6486393593");
        interstitialAd= new InterstitialAd(this);

        interstitialAd.setAdUnitId("ca-app-pub-7426325861660851/8005636620");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        //ads setup--------------------------------------------------
        //MobileAds.initialize(getApplicationContext(), "ca-app-pub-7426325861660851~6486393593");
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7426325861660851~648639359");
        mAdView = findViewById(R.id.adView);
        madView2 = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        madView2.loadAd(adRequest);
//--------------------------------------------------




        String res_link=getIntent().getStringExtra("result_link");
        bar = (ProgressBar)findViewById(R.id.processBar);
        mShowResult= findViewById(R.id.showResult);
        webView = (WebView) findViewById(R.id.webView1);
        webView.setInitialScale(30);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.setWebViewClient(new mywebclient());
        WebSettings webSettings =webView.getSettings();
        webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
        //   webView.getSettings().setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        //       webView.setVerticalScrollBarEnabled(true);
        //     webView.setHorizontalScrollbarOverlay(true);


        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(res_link);

        mShowResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementById('ctl00_cph1_btnShowResult').click()"+
                        "})()");

            }
        });

    }
    public class mywebclient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {

            webView.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('menu')[0].style.display='none'; "+
                    "document.getElementsByClassName('logo')[0].style.display='none'; "+
                    //  "var head = document.getElementsByClassName('header').style.display='none'; " +

                    "document.getElementsByTagName('ul')[0].style.display='none'; " +
                    "document.getElementsByTagName('li')[0].style.display='none'; " +
                    "document.getElementsByTagName('h1')[0].style.display='none'; " +
                    "document.getElementsByTagName('h2')[0].style.display='none'; " +
                    "document.getElementsByTagName('table')[0].setAttribute(\"style\",\"width:155%; cellspacing:40px; margin-left: -120px;font-size:35px; height:500px;\"); " +
                    //  "document.getElementsById(\"header\")[0].setAttribute(\"style\",\"width:0%; height:0px;\"); " +
                    //"var head = document.getElementsByTagName('ul').style.display='none'; " +
                    //"document.getElementsByTagName('header')[0].style.display=\"none\"; " +
                    //"document.getElementsByClassName('header')[0].style.display='none'; " +
                    //"document.getElementsById('header')[0].style.display='none'; " +

                    "})()");
            bar.setVisibility(view.GONE);
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode==KeyEvent.KEYCODE_BACK)&& webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (interstitialAd.isLoaded())
        {
            interstitialAd.show();
        }
        finish();
    }
}
