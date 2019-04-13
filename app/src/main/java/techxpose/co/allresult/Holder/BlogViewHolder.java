package techxpose.co.allresult.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import techxpose.co.allresult.R;

public class BlogViewHolder extends RecyclerView.ViewHolder {

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
    public View getMview() {
        return mview;
    }

    public  void setResultlink(String resultlink)
    {
        //TextView result_link = (TextView)mview.findViewById(R.id.link);
        //result_link.setText(resultlink);
    }
}
