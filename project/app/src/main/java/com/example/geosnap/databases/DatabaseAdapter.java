package com.example.geosnap.databases;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.geosnap.R;

import java.util.ArrayList;

public class DatabaseAdapter extends RecyclerView.Adapter<DatabaseAdapter.MyViewHolder> {

    private ArrayList<DatabaseData> dataList;
    private Context context;

    public DatabaseAdapter(ArrayList<DatabaseData> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImageUrl()).into(holder.recyclerImage);
        holder.recyclerCaption.setText(dataList.get(position).getImageUrl());
        holder.recyclerCaption.setText(dataList.get(position).getName());
        holder.recyclerCaption.setText(dataList.get(position).getDescription());

        holder.recyclerCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DetailActivity.class);
                intent.putExtra("Image",dataList.get(holder.getAdapterPosition()).getImageUrl());
                intent.putExtra("Desciption",dataList.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("Name",dataList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("key",dataList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("Date",dataList.get(holder.getAdapterPosition()).getDate());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void  searchDataList(ArrayList<DatabaseData> searchlist){
        dataList = searchlist;
        notifyDataSetChanged();

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView recyclerImage;
        TextView recyclerCaption;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerImage=itemView.findViewById(R.id.recyclerImage);
            recyclerCaption=itemView.findViewById(R.id.recyclerCaption);
        }
    }
}
