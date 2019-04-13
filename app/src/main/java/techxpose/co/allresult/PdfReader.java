package techxpose.co.allresult;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.FileNotFoundException;

public class PdfReader extends AppCompatActivity {
    private ProgressDialog mdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mdialog = new ProgressDialog(this);
        mdialog.setMessage("Loading...");
        mdialog.setCancelable(false);
        mdialog.show();

        setContentView(R.layout.activity_pdf_reader);
        String link = getIntent().getStringExtra("link");
        String resLink = getIntent().getStringExtra("resLink");
        WebView webView = findViewById(R.id.webview);
        if(link!=null)
            webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+link);
        else
            webView.loadUrl(resLink);

        webView.setInitialScale(30);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
         webView.setWebViewClient(new Mywebclient());
        WebSettings webSettings =webView.getSettings();
        webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

    class Mywebclient extends WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPageFinished(WebView view, String url) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mdialog.dismiss();

        }
        @Override
        public void onReceivedHttpError (WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            //Toast.makeText(PdfReader.this, errorResponse.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
