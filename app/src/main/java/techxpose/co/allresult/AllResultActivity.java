package techxpose.co.allresult;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AllResultActivity extends AppCompatActivity {
AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_result);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7426325861660851~6486393593");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);





        TextView dec2017 =findViewById(R.id.dec2017);
        TextView may2017 =findViewById(R.id.may2017);
        TextView dec2016 =findViewById(R.id.dec2016);
        TextView may2016 =findViewById(R.id.may2016);
        TextView dec2015 =findViewById(R.id.dec2015);
        TextView may2015 =findViewById(R.id.may2015);
        TextView dec2014 =findViewById(R.id.dec2014);
        ImageView navigationicon=findViewById(R.id.navigation_back);
        navigationicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dec2017.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(AllResultActivity.this,MainActivity.class);
                intent.putExtra("uid","Result");
                intent.putExtra("examination","December,2017");
                startActivity(intent);
            }
        });

        may2017.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent =new Intent(AllResultActivity.this,MainActivity.class);
                intent.putExtra("uid","Result");
                intent.putExtra("examination","May,2017");
                startActivity(intent);

            }
        });
        dec2016.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent =new Intent(AllResultActivity.this,MainActivity.class);
                intent.putExtra("uid","Result");
                intent.putExtra("examination","December,2016");
                startActivity(intent);
            }
        });
        may2016.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent =new Intent(AllResultActivity.this,MainActivity.class);
                intent.putExtra("uid","Result");
                intent.putExtra("examination","May,2016");
                startActivity(intent);


            }
        });
        dec2015.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(AllResultActivity.this,MainActivity.class);
                intent.putExtra("uid","Result");
                intent.putExtra("examination","December,2015");
                startActivity(intent);
            }
        });
        may2015.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(AllResultActivity.this,MainActivity.class);
                intent.putExtra("uid","Result");
                intent.putExtra("examination","May,2016");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
