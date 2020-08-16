package com.example.scanner.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanner.OptionsActivity;
import com.example.scanner.R;
import com.example.scanner.model.StationsItem;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.ViewHoder> {
    Context context;
    ArrayList<StationsItem> itemsArray;
    public StationsAdapter(Context context, ArrayList<StationsItem> itemsArray) {
        this.context = context;
        this.itemsArray = itemsArray;
    }
    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customitem,null);
        return new ViewHoder(view);
    }
    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHoder holder, final int position) {
       // final StationsItem  stationsItem=filterList.get(position);
        String color = "#FECC29";
        //holder.items.setBackgroundColor(Color.parseColor(color));
        holder.stationname.setText(itemsArray.get(position).getStationName());
      // holder.linenme.setText(String.valueOf(itemsArray.get(position).getLineName()));
    }
    @Override
    public int getItemCount() {
        return itemsArray.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView stationname,linenme;
        public ViewHoder(@NonNull final View itemView) {
            super(itemView);
            stationname=itemView.findViewById(R.id.stationname);
          // linenme=itemView.findViewById(R.id.linename);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String  s = itemsArray.get(getAdapterPosition()).getStationName();
                    String n=String.valueOf(itemsArray.get(getAdapterPosition()).getId());
                    String n2=String.valueOf(itemsArray.get(getAdapterPosition()).getId());
                    String line = String.valueOf(itemsArray.get(getPosition()).getLineName());
                    Intent intent = new Intent(context, OptionsActivity.class);
                    intent.putExtra("start_station", s);
                    intent.putExtra("end_station",s);
                    intent.putExtra("station_id",n);
                    intent.putExtra("lineName",line);
                    SharedPreferences.Editor editor = context.getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                    editor.putString("start",n);
                    editor.putString("end",n2);
                    editor.apply();
                    context.startActivity(intent);
                    /*if (context instanceof Activity)
                        ((Activity) context).setResult(Activity.RESULT_OK, intent);
                    ((Activity)context).finish();*/
                }
            });

        }
    }
    public void filterList(ArrayList<StationsItem> filteredList) {
        itemsArray = filteredList;
        notifyDataSetChanged();
    }

}
