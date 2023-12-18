package com.example.geosnap.databases;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.geosnap.R;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<DatabaseData> dataList;
    public MyAdapter(Context context, List<DatabaseData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImagesUrl().get(0)).into(holder.recImage);
        holder.recTag.setText(dataList.get(position).getTag());
        holder.recDesc.setText(dataList.get(position).getDesc());
        holder.recDate.setText(dataList.get(position).getDateTime());
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getImagesUrl().get(0));
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDesc());
                intent.putExtra("Tag", dataList.get(holder.getAdapterPosition()).getTag());
                intent.putExtra("Date", dataList.get(holder.getAdapterPosition()).getDateTime());
                intent.putStringArrayListExtra("Images", dataList.get(holder.getAdapterPosition()).getImagesUrl());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void searchDataList(ArrayList<DatabaseData> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView recImage;
    TextView recTag, recDesc, recDate;
    CardView recCard;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recDesc = itemView.findViewById(R.id.recDesc);
        recDate = itemView.findViewById(R.id.recDate);
        recTag = itemView.findViewById(R.id.recTag);
    }
}
