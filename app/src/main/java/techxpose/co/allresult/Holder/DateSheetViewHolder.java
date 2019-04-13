package techxpose.co.allresult.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import techxpose.co.allresult.R;

public class DateSheetViewHolder extends RecyclerView.ViewHolder {


    View view;
    public DateSheetViewHolder(View itemView) {
        super(itemView);
        view=itemView;
    }
    public void setExamination(String examination)
    {
        TextView examinationTextView = view.findViewById(R.id.examination);
        examinationTextView.setText(examination);

    }
    public void setDesc(String desc)
    {
        TextView descTextView = view.findViewById(R.id.description);
        descTextView.setText(desc);

    }
    public  void setName(String name)
    {
        TextView nameTextView = view.findViewById(R.id.name);

        nameTextView.setText(name);

    }
    public View getView() {
        return view;
    }

}