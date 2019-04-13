package techxpose.co.allresult.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import techxpose.co.allresult.AllResultActivity;
import techxpose.co.allresult.ContactusActivity;
import techxpose.co.allresult.DateSheetActivity;
import techxpose.co.allresult.LogActivity;
import techxpose.co.allresult.MainActivity;
import techxpose.co.allresult.Model.LogModel;
import techxpose.co.allresult.PdfReader;
import techxpose.co.allresult.R;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    ArrayList<LogModel> dashModelArrayList;
    Intent intent=null;
    private static Context context;
    OnItemClickListner itemClickListner;
    public interface OnItemClickListner
    {
        public void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListner itemClickListner)//make one function to set Click Listener
    {
        this.itemClickListner=itemClickListner;
    }
    public LogAdapter(ArrayList<LogModel> logModelArrayList) {
        this.dashModelArrayList = logModelArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String ret_head = dashModelArrayList.get(position).getHead();
        holder.header.setText(ret_head);

        String ret_sub = dashModelArrayList.get(position).getSub();
        holder.sub_header.setText(ret_sub);

        int ret_image = dashModelArrayList.get(position).getImage();
        holder.images.setImageResource(ret_image);



    }

    @Override
    public int getItemCount() {
        return dashModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView header,sub_header;
        ImageView images;
        View myView;


//        public void setheader(String h)
//        {
//            header = myView.findViewById(R.id.header);
//            header.setText(h);
//        }
//
//        public void set_sub(String s)
//        {
//            sub_header = myView.findViewById(R.id.sub_header);
//            sub_header.setText(s);
//        }
//        public void set_image(int i)
//        {
//            images = myView.findViewById(R.id.dash_image);
//            images.setImageResource(i);
//        }

        public ViewHolder(View itemView) {
            super(itemView);
           // myView = itemView;
            context = itemView.getContext();
            header = itemView.findViewById(R.id.header);
            images = itemView.findViewById(R.id.dash_image);
            sub_header = itemView.findViewById(R.id.sub_header);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListner!=null)
                    {
//                        Integer position = getAdapterPosition();
//                        Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show();
                        Integer position = getAdapterPosition();
                        switch(position) {
                            case 0 :
                            {
                                intent = new Intent(context,MainActivity.class);
                                intent.putExtra("uid","Result");
                                intent.putExtra("examination","December,2018");
                                context.startActivity(intent);
                                break;
                            }
                            case 1 :
                            {
                                intent = new Intent(context,AllResultActivity.class);
                                context.startActivity(intent);
                                break;
                            }
                            case 3 :
                            {
                                intent = new Intent(context,DateSheetActivity.class);
                                intent.putExtra("uid","DateSheet");
                                context.startActivity(intent);
                                break;
                            }
                            case 4 :
                            {
                                intent = new Intent(context,ContactusActivity.class);
                                context.startActivity(intent);
                                break;
                            }
                            case 5 :
                            {
                                rateapp();
                                break;
                            }
                            case 6 :
                            {
                                try {
                                    Intent i = new Intent(Intent.ACTION_SEND);
                                    i.setType("text/plain");
                                    i.putExtra(Intent.EXTRA_SUBJECT, "PU Results");
                                    String sAux = "\nHey, I am using this app to see my Result.\n\n";
                                    sAux = sAux + "https://play.google.com/store/apps/details?id=techxpose.co.allresult \n\n";
                                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                                    context.startActivity(Intent.createChooser(i, "choose one"));
                                } catch(Exception e) {
                                    //e.toString();
                                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                            default:
                            {
                                Toast.makeText(context, "No New Notification", Toast.LENGTH_SHORT).show();
                            }


                        }
//                        intent = new Intent(context,AllResultActivity.class);
//                        context.startActivity(intent);
                        //Toast.makeText(context, "working", Toast.LENGTH_SHORT).show();
//                        int position = getAdapterPosition();
//                        if(position!=RecyclerView.NO_POSITION)
//                        {
//                           // itemClickListner.onItemClick(position);
//                            intent =  new Intent(log, ContactusActivity.class);
//
//                            log.startActivity(intent);
//                        }
                    }
                }
            });

        }

    }

    public void rateapp()
    {

        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=techxpose.co.allresult");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=techxpose.co.allresult")));
        }
    }
}