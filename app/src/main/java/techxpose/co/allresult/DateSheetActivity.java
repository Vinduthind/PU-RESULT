package techxpose.co.allresult;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import techxpose.co.allresult.Holder.DateSheetViewHolder;
import techxpose.co.allresult.Model.Blog;
import techxpose.co.allresult.Holder.BlogViewHolder;
import techxpose.co.allresult.Model.DateSheetModel;

public class DateSheetActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference mdatabase;
    private Query mQuery;
    private String[] dateSheetType;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_sheet);
        recyclerView = findViewById(R.id.blog);
        mdatabase= FirebaseDatabase.getInstance().getReference().child("DateSheet");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mdatabase.keepSynced(true);
        spinner = findViewById(R.id.dateSheetType);
        
        dateSheetType = getResources().getStringArray(R.array.dateSheetType);
        ArrayAdapter<String> examTypeAdapter =  new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, dateSheetType);
        examTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(examTypeAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override             
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int index = adapterView.getSelectedItemPosition();
                Toast.makeText(DateSheetActivity.this, dateSheetType[i], Toast.LENGTH_SHORT).show();
                mQuery = mdatabase.orderByChild("course").equalTo(dateSheetType[index]);
               // Toast.makeText(DateSheetActivity.this, mdatabase.toString(), Toast.LENGTH_SHORT).show();
                recyclepostwork();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
      //  mdatabase= FirebaseDatabase.getInstance().getReference().child("DateSheet");
//        mQuery = mdatabase;
//        recyclepostwork();

    }

    private void recyclepostwork() {

        FirebaseRecyclerAdapter<DateSheetModel, DateSheetViewHolder> firebaseRecycleAdapter = new FirebaseRecyclerAdapter<DateSheetModel, DateSheetViewHolder>(

                DateSheetModel.class,
                R.layout.datesheet_list,
                DateSheetViewHolder.class,
                mQuery
        ) {
            @Override
            protected void populateViewHolder(final DateSheetViewHolder viewHolder, final DateSheetModel model, int position) {

                viewHolder.setName(model.getName());
               // viewHolder.setLink(model.getLink());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setExamination(model.getExamination());
                viewHolder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            Intent myIntent = new Intent(DateSheetActivity.this,PdfReader.class);
                            myIntent.putExtra("link",model.getLink().toString());
                            startActivity(myIntent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(DateSheetActivity.this, "No result Found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }}
                });
            }};
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.canScrollVertically();
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        firebaseRecycleAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                mLinearLayoutManager.scrollToPosition(positionStart);
            }
        });
        recyclerView.setAdapter(firebaseRecycleAdapter);

    }


}
