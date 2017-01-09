package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.githubconnect.githubconnect.R;
import com.githubconnect.githubconnect.SingleRepo;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import GithubFiles.GithubApp;
import network.RepoData;

/**
 * Created by haseeb on 7/1/17.
 */
public class RepoAdaptor  extends RecyclerView.Adapter<RepoAdaptor.ViewHolder> {
    private static final int TYPE_LOAD_MORE = 0;
    private static final int TYPE_ITEM = 1;

    List<RepoData> data = new ArrayList<RepoData>();
    LayoutInflater inflater;
    static Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        int Holderid;
        TextView title, description;


        public ViewHolder(View itemView, int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);

            if (ViewType == TYPE_ITEM){
                Holderid = 1;

                title = (TextView) itemView.findViewById(R.id.title);
                description = (TextView) itemView.findViewById(R.id.description);
            }
            else {
                Holderid = 0;
            }


        }

    }


    public RepoAdaptor(List<RepoData> data, Context context) { // MyAdapter Constructor with titles and icons parameter
        this.data = data;
        RepoAdaptor.context = context;
    }


    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_placholder, parent, false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loadmore_placholder, parent, false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object
        }

    }


    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (holder.Holderid == 1){
            final RepoData item = data.get(position);

            holder.title.setText(item.getName());
            if (item.getDescription() != null) {
                holder.description.setVisibility(View.VISIBLE);
                holder.description.setText(item.getDescription());
            }
            else {
                holder.description.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent repo = new Intent(context, SingleRepo.class);
                    repo.putExtra("title", item.getName());
                    repo.putExtra("description", item.getDescription());
                    context.startActivity(repo);

                }
            });

        }

    }


    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (data.get(position) != null) {
            return TYPE_ITEM;
        } else {
            return TYPE_LOAD_MORE;

        }
    }


}
