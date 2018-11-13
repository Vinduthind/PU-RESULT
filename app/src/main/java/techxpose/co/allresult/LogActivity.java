package techxpose.co.allresult;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import techxpose.co.allresult.Adapter.LogAdapter;
import techxpose.co.allresult.Model.LogModel;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity implements LogAdapter.OnItemClickListner
{
TextView recentlyupdated ,allResult,notification,contactus,dateSheet;
     DatabaseReference mdatabase;
    String value,   noticationString="No Notification Yet",noticationdate;
     AlertDialog mdialog;
     ProgressDialog progress;
     Toolbar mytoolbar;
     AdView mAdView;
     ArrayList<LogModel> logModelArrayList;
     RecyclerView recyclerView;
     LogAdapter logAdapter;
     List<LogModel> logList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        mytoolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("");
        //for adv purpose
        //MobileAds.initialize(getApplicationContext(), "ca-app-pub-7426325861660851~6486393593");
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7426325861660851~648639359");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        recyclerView = findViewById(R.id.rv1);
        logList= new ArrayList<>();
        recentlyupdated = (TextView)findViewById(R.id.today);
        allResult=(TextView)findViewById(R.id.allResult);
        notification=(TextView)findViewById(R.id.notifications);
        contactus=(TextView)findViewById(R.id.contactUs);
        dateSheet=(TextView)findViewById(R.id.dateSheet);
        mdialog = new ProgressDialog(this);
        progress = new ProgressDialog(this);
        mdatabase= FirebaseDatabase.getInstance().getReference();
        mdialog.setMessage("Loading Result...");
        mdialog.setCancelable(false);
        mdialog.show();
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value = dataSnapshot.child("lastpostdate").child("examination").getValue(String.class);
                noticationString =dataSnapshot.child("update").child("notification").getValue(String.class);
                noticationdate =dataSnapshot.child("update").child("date").getValue(String.class);
              recentlyupdated.setText(value+" Results");
              mdialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {



            }
        });
        //----------------------------------------------------------------------------------------------------------------------------------------------------------------------

        logModelArrayList = new ArrayList<>();
        String heads[] = {"Jobs", "My Profile", "Notification", "Applied Jobs", "Date Sheets", "Contact US", "Rate US","Invite Friends"};

        String subs[] = {"12 new jobs found", "75% complete", "2 new messages", "3 applies jobs", "Edit resume", "Set preferences","",""};

        int images[] = {R.drawable.find_jobs, R.drawable.profile, R.drawable.messages, R.drawable.applied_jobs,
                R.drawable.resume,R.drawable.settings, R.drawable.find_jobs, R.drawable.profile};

        for(int count = 0 ; count < heads.length ; count++)
        {
            LogModel logModel = new LogModel();
            logModel.setHead(heads[count]);
            logModel.setSub(subs[count]);
            logModel.setImage(images[count]);
            logModelArrayList.add(logModel);
            //this should be retrieved in our adapter
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        logAdapter = new LogAdapter(logModelArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(logAdapter);
        logAdapter.setOnItemClickListener(this);
        //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        allResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LogActivity.this,AllResultActivity.class);
                intent.putExtra("uid","Result");
                startActivity(intent);

            }
        });

        dateSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LogActivity.this,MainActivity.class);
                intent.putExtra("uid","DateSheet");
                startActivity(intent);


            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(LogActivity.this, "No Notifiaction Yet", Toast.LENGTH_LONG).show();
                Snackbar snackbar =  Snackbar.make(v, "Last Update on "+noticationdate+"\n\nMESSAGE:\n"+noticationString, Snackbar.LENGTH_INDEFINITE)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorAccent ));
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(20);
                snackbar.show();

            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogActivity.this,ContactusActivity.class));


            }
        });
        recentlyupdated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LogActivity.this,MainActivity.class);
                intent.putExtra("uid","Result");
                intent.putExtra("examination",value);
                startActivity(intent);

            }
        });
    }


    boolean doubleBackToExitPressedOnce = false;
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.action_contact) {
            startActivity(new Intent(LogActivity.this,ContactusActivity.class));
            return true;
        }
        if (id == R.id.action_rateus) {
          //  startActivity(new Intent(LogActivity.this,ContactusActivity.class));
            rateapp();
            return true;
        }

        if (id == R.id.action_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "PU Results");
                String sAux = "\nHey, I am using this app to see my Result.\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=techxpose.co.allresult \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //e.toString();
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {

        super.onRestart();
    }
    private void rateapp()
    {

        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=techxpose.co.allresult");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=techxpose.co.allresult")));
        }
    }

    @Override
    public void onItemClick(int position) {

        LogModel clickItem =logModelArrayList.get(position);
        Toast.makeText(this, clickItem.getHead()+" : "+clickItem.getSub(), Toast.LENGTH_LONG).show();
        System.out.println(clickItem.getHead());
//        Intent intent=new Intent(this,MainActivity.class);
//
//        this.startActivity(intent);
//        finish();
    }
}
