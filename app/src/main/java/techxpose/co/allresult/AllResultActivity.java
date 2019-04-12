package techxpose.co.allresult;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import java.util.ArrayList;
import techxpose.co.allresult.Adapter.AllResultAdapter;
import techxpose.co.allresult.Model.AllResultModel;

public class AllResultActivity extends AppCompatActivity implements AllResultAdapter.OnItemClickListner{
     AdView mAdView;
     ArrayList<AllResultModel> allResultModelArrayList;
     RecyclerView recyclerView;
     AllResultAdapter allResultAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_result);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7426325861660851~6486393593");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        allResultModelArrayList = new ArrayList<AllResultModel>();
        String heading[] = {"December,2018","November,2018","May,2018","December,2017", "May,2017", "December,2016", "May,2016", "December,2015", "May,2015","December,2014"};
        for(int count = 0 ; count < heading.length ; count++)
        {
            AllResultModel allResultModel = new AllResultModel();
            allResultModel.setHeading(heading[count]);
            allResultModelArrayList.add(allResultModel);
            //this should be retrieved in our adapter
        }
        recyclerView = findViewById(R.id.rv2);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        allResultAdapter = new AllResultAdapter(allResultModelArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(allResultAdapter);
        allResultAdapter.setOnItemClickListener(this);
        ImageView navigationicon=findViewById(R.id.navigation_back);
        navigationicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onItemClick(int position) {
    AllResultModel allResultModel = allResultModelArrayList.get(position);
        Toast.makeText(this, allResultModel.getHeading(), Toast.LENGTH_SHORT).show();

    }
}
