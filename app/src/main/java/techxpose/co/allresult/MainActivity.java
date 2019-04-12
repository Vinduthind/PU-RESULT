package techxpose.co.allresult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import techxpose.co.allresult.Model.BlogViewHolder;
import techxpose.co.allresult.Adapter.AutoFillTextViewAdapter;
import techxpose.co.allresult.Model.AutoFillTextViewModel;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mbloglist;
    private DatabaseReference mdatabase;
    private Query mQuery;
    private ImageView navigationicon;
    private TextView titleText;
    private String value;
    private android.support.v7.widget.Toolbar mytoolbar;
    private InterstitialAd interstitialAd;
    private ProgressDialog mdialog;
    private AppCompatImageButton searchButton ;
    private String uidw=null, examination=null;
    private ArrayList<AutoFillTextViewModel> list = new ArrayList<>();
    AppCompatAutoCompleteTextView autoCompleteTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mbloglist = findViewById(R.id.blog);
        mytoolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        mdialog = new ProgressDialog(this);
        autoCompleteTextView= findViewById(R.id.searchField);
        searchButton= findViewById(R.id.searchButton);
        // for advertise pupose
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7426325861660851~6486393593");
        interstitialAd= new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-7426325861660851/8005636620");
        interstitialAd.loadAd(new AdRequest.Builder().build());

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

        autoCompleteTextView.setCursorVisible(false);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setCursorVisible(true);
                autoCompleteTextView.setSelectAllOnFocus(true);
                autoCompleteTextView.selectAll();

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchContent = autoCompleteTextView.getText().toString();
                if (searchContent.equals("") && examination == null) { //mdatabase= FirebaseDatabase.getInstance().getReference().child(uidw);
                    mQuery = mdatabase;
                    recyclepostwork();
                } else if (examination != null && searchContent.equals("")) {
                    mQuery = mdatabase.orderByChild("examination").equalTo(examination);
                    //Toast.makeText(this, date+"  "+uidw, Toast.LENGTH_SHORT).show();
                    recyclepostwork();
                } else {

                    // mQuery= mdatabase.orderByKey().startAt("#"+searchContent).endAt("#"+searchContent)
//                   //here search work
//                   mdialog.setMessage("wait...");
//                   mdialog.show();
//                   mdatabase.addValueEventListener(new ValueEventListener() {
//                       @Override
//                       public void onDataChange(DataSnapshot dataSnapshot) {
//                           for(DataSnapshot data: dataSnapshot.getChildren()){
//                               String usernames=data.getKey();
//                               value=dataSnapshot.child(usernames).child("examination").getValue(String.class);
//                             //  Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
//
//                               mdialog.dismiss();
//                           }
//                       }
//                       @Override
//                       public void onCancelled(DatabaseError databaseError) {
//                       }
//                   });
                    mQuery = mdatabase.orderByChild("branchname").startAt(searchContent).endAt(searchContent + "\uf8ff");
                    recyclepostwork();
                    mdialog.dismiss();

                }
            }});

        mdatabase.keepSynced(true);

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(examination!=null) {
            mQuery = mdatabase.orderByChild("examination").equalTo(examination);
        }else{
            mQuery=mdatabase;
        }
            autoCompleteList(mQuery);
            recyclepostwork();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    private void autoCompleteList(Query query){

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    list.add(new AutoFillTextViewModel(child.child("branchname").getValue().toString()));

                }
                Collections.reverse(list);
                AutoFillTextViewAdapter adapter=  new AutoFillTextViewAdapter(MainActivity.this, R.layout.auto_fill_list, list);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setTextColor(Color.BLACK);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
            viewHolder.getMview().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent myIntent = new Intent(MainActivity.this,ShowResult.class);
                        myIntent.putExtra("result_link",model.getResultlink().toString());
                        myIntent.putExtra("exam_type",model.getBranchname().toString());
                        myIntent.putExtra("exam_name",model.getExamination().toString());
                                   // Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getResultlink()));
                        startActivity(myIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "No result Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }}
            });
        }};
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

        private void changeResult()
        {
            AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
            View mview = getLayoutInflater().inflate(R.layout.annoucedresultlist,null);
            mbuilder.setView(mview);
            final AlertDialog dialog = mbuilder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
            TextView dec2018 =mview.findViewById(R.id.dec2018);
            TextView nov2018 =mview.findViewById(R.id.nov2018);
            TextView may2018 =mview.findViewById(R.id.may2018);
            TextView dec2017 =mview.findViewById(R.id.dec2017);
            TextView may2017 =mview.findViewById(R.id.may2017);
            TextView dec2016 =mview.findViewById(R.id.dec2016);
            TextView may2016 =mview.findViewById(R.id.may2016);
            TextView dec2015 =mview.findViewById(R.id.dec2015);
            TextView may2015 =mview.findViewById(R.id.may2015);
            TextView dec2014 =mview.findViewById(R.id.dec2014);
            dec2018.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQuery = mdatabase.orderByChild("examination").equalTo("December,2018");
                    //Toast.makeText(this, date+"  "+uidw, Toast.LENGTH_SHORT).show();
                    autoCompleteList(mQuery);
                    recyclepostwork();
                    titleText.setText("   December 2018 Results");
                    dialog.dismiss();

                }
            });
            nov2018.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mQuery = mdatabase.orderByChild("examination").equalTo("November,2018");
                    //Toast.makeText(this, date+"  "+uidw, Toast.LENGTH_SHORT).show();
                    autoCompleteList(mQuery);
                    recyclepostwork();
                    titleText.setText("   November 2018 Results");
                    dialog.dismiss();

                }
            });
            may2018.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQuery = mdatabase.orderByChild("examination").equalTo("May,2018");
                    //Toast.makeText(this, date+"  "+uidw, Toast.LENGTH_SHORT).show();

                    autoCompleteList(mQuery);
                    recyclepostwork();
                    titleText.setText("   May 2018 Results");
                    dialog.dismiss();

                }
            });
            dec2017.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQuery = mdatabase.orderByChild("examination").equalTo("December,2017");
                    //Toast.makeText(this, date+"  "+uidw, Toast.LENGTH_SHORT).show();
                    autoCompleteList(mQuery);
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
                    autoCompleteList(mQuery);
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
                    autoCompleteList(mQuery);
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
                    autoCompleteList(mQuery);
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
                    autoCompleteList(mQuery);
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
                    autoCompleteList(mQuery);
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
                    autoCompleteList(mQuery);
                    recyclepostwork();
                    dialog.dismiss();
                }
            });
        }

}


