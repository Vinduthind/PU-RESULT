package techxpose.co.allresult.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import techxpose.co.allresult.Model.AllResultModel;
import techxpose.co.allresult.Model.LogModel;
import techxpose.co.allresult.R;

public class AllResultAdapter extends RecyclerView.Adapter<AllResultAdapter.ViewHolder> {
    ArrayList<AllResultModel> allResultArrayList;
    OnItemClickListner itemClickListner;
    public interface OnItemClickListner
    {
        public void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListner itemClickListner)//make one function to set Click Listener
    {
        this.itemClickListner=itemClickListner;
    }
    public AllResultAdapter(ArrayList<AllResultModel> logModelArrayList) {
        this.allResultArrayList = logModelArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allresult_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String heading = allResultArrayList.get(position).getHeading();
        holder.heading.setText(heading);
    }

    @Override
    public int getItemCount() {
        return allResultArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView heading;
        Context context = itemView.getContext();
        public ViewHolder(View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Working", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
