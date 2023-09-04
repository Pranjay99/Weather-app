package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class weatherRVadapter extends RecyclerView.Adapter<weatherRVadapter.ViewHolder> {
    private Context context;
    private ArrayList<weatherRVmodel> weatherRVmodelArrayList;

    public weatherRVadapter(Context context, ArrayList<weatherRVmodel> weatherRVmodelArrayList) {
        this.context = context;
        this.weatherRVmodelArrayList = weatherRVmodelArrayList;
    }

    @NonNull
    @Override
    public weatherRVadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_temp,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull weatherRVadapter.ViewHolder holder, int position) {

        weatherRVmodel model= weatherRVmodelArrayList.get(position);
        holder.txttemp.setText(model.getTemp() +"°c");
        Picasso.get().load("https:".concat(model.getIcon())).into(holder.cloud);
        holder.txtspeed.setText(model.getSpeed()+"Km/h");

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output= new SimpleDateFormat("hh:mm");
        try{
            Date t = input.parse(model.getTime());
            holder.txttime.setText(output.format(t));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }



    @Override
    public int getItemCount() {
        return weatherRVmodelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txttime, txttemp, txtspeed;
        private ImageView cloud;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txttime = itemView.findViewById(R.id.time);
            txttemp = itemView.findViewById(R.id.dailytemp);
            txtspeed = itemView.findViewById(R.id.speed);
            cloud = itemView.findViewById(R.id.cloud);
        }
    }
}
