package techxpose.co.allresult.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;
import techxpose.co.allresult.Model.AutoFillTextViewModel;
import techxpose.co.allresult.R;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.regex.Pattern;

public class AutoFillTextViewAdapter extends ArrayAdapter<AutoFillTextViewModel> {

    private Context context;
    private int resourceId;
    private List<AutoFillTextViewModel> items, tempItems, suggestions;
    private View mview;
    public AutoFillTextViewAdapter(@NonNull Context context, int resource, ArrayList<AutoFillTextViewModel> items) {
        super(context, resource, items);
        this.items = items;
        this.context = context;
        this.resourceId = resource;
        this.tempItems = new ArrayList<>(items);
        this.suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
            this.mview=convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            AutoFillTextViewModel autoFillTextViewModel = getItem(position);
            TextView BranchName = view.findViewById(R.id.textView);
            BranchName.setText(autoFillTextViewModel.getBranchName());

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (position % 2 == 1) {
            view.setBackgroundColor(Color.WHITE);
        } else {
            view.setBackgroundColor(Color.parseColor("#5000ACF0"));
        }

        return view;
    }


    @Nullable
    @Override
    public AutoFillTextViewModel getItem(int position) {

        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return autoTextFilter;
    }

    private Filter autoTextFilter = new Filter() {


        @Override
        public CharSequence convertResultToString(Object resultValue) {
            AutoFillTextViewModel brachName = (AutoFillTextViewModel) resultValue;
            return brachName.getBranchName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                String[] strArr = charSequence.toString().split(" ");
                StringBuilder regexp = new StringBuilder();
                for (String string:strArr) {
                    regexp.append("(?=.*").append(string.toLowerCase()).append(")");
                }
                Pattern pattern = Pattern.compile(regexp.toString());
                for (AutoFillTextViewModel autoFillTextViewModel: tempItems) {
                    if (pattern.matcher(autoFillTextViewModel.getBranchName().toLowerCase()).find()) {
                        suggestions.add(autoFillTextViewModel);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            ArrayList<AutoFillTextViewModel> tempValues =null;
            try {

                 tempValues = (ArrayList<AutoFillTextViewModel>) filterResults.values;

            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (AutoFillTextViewModel autoFillTextViewModel : tempValues) {
                    add(autoFillTextViewModel);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }


            }catch (ConcurrentModificationException e)
            {
                try {
                    tempValues.clear();
                    tempValues = (ArrayList<AutoFillTextViewModel>) filterResults.values;
                }
                catch (ConcurrentModificationException e1){
                    e1.fillInStackTrace();
                }
            }catch (NullPointerException e){

            }
        }

    };
}
