package techxpose.co.allresult;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import android.content.ActivityNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.content.Intent;
import android.opengl.Visibility;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {
        private RecyclerView mbloglist;
        private DatabaseReference mdatabase;
        EditText searchbar;
        private  Query mQuery;
        ImageView navigationicon;
        TextView titleText;
        android.support.v7.widget.Toolbar mytoolbar;
        private InterstitialAd interstitialAd;

    private String uidw=null, examination=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mbloglist = findViewById(R.id.blog);
        mytoolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);

        // for advertise pupose
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7426325861660851~6486393593");
        interstitialAd= new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-7426325861660851/8005636620");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        searchbar = findViewById(R.id.searchField);
        titleText= findViewById(R.id.titletext);
        navigationicon=findViewById(R.id.navigation_back);
        uidw = getIntent().getExtras().getString("uid");
        examination = getIntent().getExtras().getString("examination");
        //Toast.makeText(this, examination, Toast.LENGTH_SHORT).show();
        titleText.setText("   "+uidw);
        mbloglist.setHasFixedSize(true);
        mbloglist.setLayoutManager(new LinearLayoutManager(this));
        mdatabase= FirebaseDatabase.getInstance().getReference().child(uidw);

        navigationicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbar.setCursorVisible(true);


            }
        });

        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchContent = searchbar.getText().toString();
               if(searchContent.equals("") && examination==null)
               { //mdatabase= FirebaseDatabase.getInstance().getReference().child(uidw);
                   mQuery=mdatabase;
                   recyclepostwork();
               }else if(examination!=null && searchContent.equals("")) {
                   mQuery = mdatabase.orderByChild("examination").equalTo(examination);
                   //Toast.makeText(this, date+"  "+uidw, Toast.LENGTH_SHORT).show();
                   recyclepostwork();
               }
               else{
                  // mQuery= mdatabase.orderByKey().startAt("#"+searchContent).endAt("#"+searchContent)

                       mQuery = mdatabase.orderByChild("branchname").startAt(searchContent).endAt(searchContent + "\uf8ff");
                       recyclepostwork();


            }}

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mdatabase.keepSynced(true);
    }



    @Override
    protected void onStart() {
        super.onStart();
       // Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
        if(examination!=null) {
            mQuery = mdatabase.orderByChild("examination").equalTo(examination);
            //Toast.makeText(this, date+"  "+uidw, Toast.LENGTH_SHORT).show();

        }else{

            mQuery=mdatabase;
        }
            recyclepostwork();
    }

    private void recyclepostwork() {
        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecycleAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,
                R.layout.blog_list,
                BlogViewHolder.class,
                mQuery

        ) {
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, final Blog model, int position) {


                viewHolder.setBranchname(model.getBranchname());
                viewHolder.setResultlink(model.getResultlink());
                viewHolder.setdate(model.getResultDate());
                viewHolder.setExamination(model.getExamination());
               // viewHolder.setYear(model.getYear());
                //Toast.makeText(MainActivity.this, model.getBranchname(), Toast.LENGTH_SHORT).show();
                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getResultlink()));
                            startActivity(myIntent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(MainActivity.this, "No result Found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                });

            }
        };

        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.canScrollVertically();
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        mbloglist.setLayoutManager(mLinearLayoutManager);
        firebaseRecycleAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                // int friendlyMessageCount = firebaseRecyclerAdapter.getItemCount();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.\

                mLinearLayoutManager.scrollToPosition(positionStart);
            }
        });
        mbloglist.setAdapter(firebaseRecycleAdapter);

    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mview;
        public BlogViewHolder(View itemView) {
            super(itemView);
        mview=itemView;
        }

        public void setExamination(String examination)
        {
            TextView setexamination = mview.findViewById(R.id.examination);
            setexamination.setText("Examination : "+examination);
        }
        public void setdate(String resultDate)
        {
            TextView branch_name = mview.findViewById(R.id.annoucedon);
            branch_name.setText("Announced on " +resultDate);

        }
        public  void setBranchname(String Branchname)
        {
            TextView branch_name = mview.findViewById(R.id.branchname);

            branch_name.setText(Branchname);

        }

        public  void setResultlink(String resultlink)
        {
            //TextView result_link = (TextView)mview.findViewById(R.id.link);
            //result_link.setText(resultlink);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Filter) {

           changeResult();


            return true;
        }

        if (id == R.id.action_contact) {

            startActivity(new Intent(MainActivity.this,ContactusActivity.class));
            return true;
        }
        if (id==R.id.action_Datesheets)
        {
            mdatabase= FirebaseDatabase.getInstance().getReference().child("DateSheet");
            mQuery=mdatabase;
            recyclepostwork();
            titleText.setText("   DateSheets");
            return  true;
        }
        if (id==R.id.action_Moreresult)
        {
            changeResult();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onRestart() {
        //Toast.makeText(this, "restart", Toast.LENGTH_SHORT).show();
        if (interstitialAd.isLoaded())
        {
            interstitialAd.show();
        }

        super.onRestart();
    }

    private void changeResult()
    {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        View mview = getLayoutInflater().inflate(R.layout.annoucedresultlist,null);
        mbuilder.setView(mview);
        final AlertDialog dialog =mbuilder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
       TextView dec2017 =mview.findViewById(R.id.dec2017);
        TextView may2017 =mview.findViewById(R.id.may2017);
        TextView dec2016 =mview.findViewById(R.id.dec2016);
        TextView may2016 =mview.findViewById(R.id.may2016);
        TextView dec2015 =mview.findViewById(R.id.dec2015);
        TextView may2015 =mview.findViewById(R.id.may2015);
        TextView dec2014 =mview.findViewById(R.id.dec2014);
        dec2017.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuery = mdatabase.orderByChild("examination").equalTo("December 2017");
                //Toast.makeText(this, date+"  "+uidw, Toast.LENGTH_SHORT).show();
                recyclepostwork();
                titleText.setText("   December 2017 Results");
                dialog.dismiss();

            }
        });
        may2017.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuery = mdatabase.orderByChild("examination").equalTo("May,2017");
                //Toast.makeText(this, date+"  "+uidw, Toast.LENGTH_SHORT).show();
                recyclepostwork();
                titleText.setText("   May 2017 Results");
                dialog.dismiss();
            }
        });
        dec2016.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuery = mdatabase.orderByChild("examination").equalTo("December,2016");
                //Toast.makeText(this, date+"  "+uidw, Toast.LENGTH_SHORT).show();
                recyclepostwork();
                titleText.setText("   December 2016 Results");
                dialog.dismiss();
            }
        });
        may2016.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuery = mdatabase.orderByChild("examination").equalTo("May,2016");
                //Toast.makeText(this, date+"  "+uidw, Toast.LENGTH_SHORT).show();
                recyclepostwork();
                titleText.setText("   May 2016 Results");
                dialog.dismiss();
            }
        });
        dec2015.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuery = mdatabase.orderByChild("examination").equalTo("December,2015");
                //Toast.makeText(this, date+"  "+uidw, Toast.LENGTH_SHORT).show();
                recyclepostwork();
                titleText.setText("   December 2015 Results");
                dialog.dismiss();
            }
        });
        may2015.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuery = mdatabase.orderByChild("examination").equalTo("May,2015");
                //Toast.makeText(this, date+"  "+uidw, Toast.LENGTH_SHORT).show();
                recyclepostwork();
                titleText.setText("   May 2015 Results");
                dialog.dismiss();
            }
        });
        dec2014.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuery = mdatabase.orderByChild("examination").equalTo("December,2014");
                //Toast.makeText(this, date+"  "+uidw, Toast.LENGTH_SHORT).show();
                recyclepostwork();
                dialog.dismiss();
            }
        });
    }
}


