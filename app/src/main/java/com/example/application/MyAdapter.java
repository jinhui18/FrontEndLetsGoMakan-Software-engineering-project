package com.example.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    ArrayList<String> arrayList;

    public MyAdapter(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }




    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.card_restaurant,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        holder.textView_restaurant_name1.setText(arrayList.get(position).toString());
        Picasso.get()
                .load("https://www.greendot.sg/wp-content/themes/Greendot/images/banner-connect-mobile.jpg")
                .fit()
                .into(holder.imageView_restaurant1);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView,textView_restaurant_name1,textView_restaurant_address1,textView_restaurant_time1;
        private ImageView imageView_bg1,imageView_restaurant1;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_restaurant_name1 = itemView.findViewById(R.id.textView_restaurant_name1);
            textView_restaurant_address1 = itemView.findViewById(R.id.textView_restaurant_address1);
            textView_restaurant_time1 = itemView.findViewById(R.id.textView_restaurant_time1);
            imageView_bg1 = itemView.findViewById(R.id.imageView_bg1);
            imageView_restaurant1 = itemView.findViewById(R.id.imageView_restaurant1);

        }

    }
}
