package techxpose.co.allresult;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class ShowResult extends AppCompatActivity {

    private WebView webView;
    private Button mShowResult,seeFullResult;
    private ProgressDialog mdialog;
    private EditText rollNo;
    private InterstitialAd interstitialAd;
    private AdView mAdView;
    private Spinner semesterSpinner,examTypeSpinner;
    private String[] SemArr, examTypeArr;
    private TextView regNo,sMark,sName,examSem,exam_name;
    private String res_link;
    private String errorMessage;
    private int count=1;
    private int countToCheckValidRollNumber=1;
    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        setContentView(R.layout.activity_show_result);
        //action perform by this page

        // for advertise pupose

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7426325861660851~6486393593");
        interstitialAd= new InterstitialAd(this);

        interstitialAd.setAdUnitId("ca-app-pub-7426325861660851/8005636620");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        //ads setup--------------------------------------------------
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //--------------------------------------------------

        res_link=getIntent().getStringExtra("result_link");
        String examType=getIntent().getStringExtra("exam_type");
        String examName=getIntent().getStringExtra("exam_name");
        regNo =findViewById(R.id.regNo);
        sName = findViewById(R.id.sName);
        sMark = findViewById(R.id.sMark);
        examSem = findViewById(R.id.examSem);
        mdialog = new ProgressDialog(this);
        mdialog.setMessage(examType+" Loading...");
        mdialog.setCancelable(false);
        mdialog.show();
        mShowResult= findViewById(R.id.showResult);
        seeFullResult = findViewById(R.id.seefullResult);
        rollNo = findViewById(R.id.rollNo);
        exam_name = findViewById(R.id.exam_name);
        exam_name.setText(examType);
        examSem = findViewById(R.id.examSem);
        examSem.setText(examName);
        semesterSpinner = findViewById(R.id.semester);
        examTypeSpinner = findViewById(R.id.examType);
        webView = findViewById(R.id.webView1);
        webView.setInitialScale(30);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.setWebViewClient(new mywebclient());
        WebSettings webSettings =webView.getSettings();
        webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new MyJavaScriptInterface(regNo,sName,sMark), "INTERFACE");
        webView.loadUrl(res_link);

        //Semester view spinner
        SemArr = getResources().getStringArray(R.array.sem_array);
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, SemArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(adapter);
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int index = adapterView.getSelectedItemPosition();
                String get = SemArr[index];
                if(get.equals("Select Semester"))
                {
                    get="-1";
                }
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementById(\"ctl00_cph1_ddlSemester\").value= "+get+";"+
                        "})()");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //ExamTYpe Spinner
        examTypeArr = getResources().getStringArray(R.array.examType_array);
        ArrayAdapter<String> examTypeAdapter =  new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, examTypeArr);
        examTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        examTypeSpinner.setAdapter(examTypeAdapter);
        examTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int index = adapterView.getSelectedItemPosition();
                String get = examTypeArr[index];
                if(get.equals("Full Subjects"))
                {
                    get="-1";
                }else if(get.equals("Re-appear Candidate/s")){
                    get="34";
                }else if(get.equals("Deficient Candidate/s")){
                    get="35";
                }else if(get.equals("Additional Subject/s")){
                    get="36";
                }else if(get.equals("Improvement of Performance")){
                    get="37";
                }else{
                    get="0";
                }
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementById(\"ctl00_cph1_ddlCollCode\").value= "+get+";"+
                        "})()");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rollNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementById(\"ctl00_cph1_txtRollNo\").value= "+ editable+";"+
                        "})()");
            }
        });

        mShowResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rollNo.getText().toString().length()>6)
                {
                    webView.loadUrl("javascript:(function() { " +
                            "document.getElementById('ctl00_cph1_btnShowResult').click()"+
                            "})()");
                    mdialog.setMessage("Result : "+rollNo.getText().toString()+" Loading...");
                    mdialog.setCancelable(true);
                    mdialog.show();
                    count=1;
                }
                else{
                    Toast.makeText(ShowResult.this, "Password Should be greater than 6 units", Toast.LENGTH_SHORT).show();
                }

            }
        });

        seeFullResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementById('ctl00_cph1_lbtnMarksDetail').click()"+
                        "})()");
                mdialog.show();
            }
        });


    }

    public class mywebclient extends WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPageFinished(WebView view, String url) {

            webView.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('menu')[0].style.display='none'; "+
                    "document.getElementsByClassName('logo')[0].style.display='none'; "+
                    "document.getElementsByTagName('ul')[0].style.display='none'; " +
                    "document.getElementsByTagName('li')[0].style.display='none'; " +
                    "document.getElementsByTagName('h1')[0].style.display='none'; " +
                    "document.getElementsByTagName('h2')[0].style.display='none'; " +
                    "document.getElementsByTagName('table')[0].setAttribute(\"style\",\"width:155%; cellspacing:40px; margin-left: -120px;font-size:35px; height:500px;\"); " +
                    "})()");
            view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementById('ctl00_cph1_lblRegNo').innerHTML,document.getElementById('ctl00_cph1_lblMarks').innerHTML,document.getElementById('ctl00_cph1_lblCName').innerHTML,document.getElementById('ctl00_cph1_lblMsg').innerHTML);");
            mdialog.dismiss();
            if(countToCheckValidRollNumber>2) {
                sName.setText("Please check Ceredentials");
            }
            if(count<2)
            {
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementById('ctl00_cph1_btnShowResult').click()"+
                        "})()");
                mdialog.setMessage("Result : "+rollNo.getText().toString()+" Loading...");
                mdialog.setCancelable(true);
                mdialog.show();
                count++;
                countToCheckValidRollNumber++;

            }
            if(!regNo.getText().toString().equals("00000000")){

                mShowResult.setVisibility(View.GONE);
                seeFullResult.setVisibility(View.VISIBLE);
            }
            if(url.contains("frmShowResultDetail.aspx?CN")){
                finish();
                Intent intent = new Intent(ShowResult.this,PdfReader.class);
                intent.putExtra("resLink",url);
                startActivity(intent);
            }
            //Toast.makeText(ShowResult.this, "Result loaded", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onReceivedHttpError (WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {



        }


    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if((keyCode==KeyEvent.KEYCODE_BACK)&& webView.canGoBack()){
//            webView.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (interstitialAd.isLoaded())
        {
            interstitialAd.show();
        }
        finish();
    }

    class MyJavaScriptInterface
    {
        private TextView regNo;
        private TextView stMark;
        private TextView stName;
        public MyJavaScriptInterface(TextView aContentView,TextView sMark,TextView sName)
        {
            regNo = aContentView;
            stName = sName;
            stMark = sMark;
        }


        @JavascriptInterface
        public void processContent(String content,String name, String mark,String errorMessage)
        {
            final String stContent = content;
            final String sutMark = mark;
            final String stuName = name;

            regNo.post(new Runnable()
            {
                public void run()
                {
                    regNo.setText(stContent);
                    //Toast.makeText(ShowResult.this, content, Toast.LENGTH_SHORT).show();
                }
            });
           stName.post(new Runnable()
            {
                public void run()
                {

                        stName.setText(stuName);

                    //Toast.makeText(ShowResult.this, content, Toast.LENGTH_SHORT).show();
                }
            });
            stMark.post(new Runnable()
            {
                public void run()
                {
                    stMark.setText(sutMark);
                    //Toast.makeText(ShowResult.this, content, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
