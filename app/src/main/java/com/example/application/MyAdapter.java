package com.example.application;

import static java.lang.Math.floor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.backend.entity.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    Context context;
    ArrayList<Restaurant> arrayList;

    public MyAdapter(Context context, ArrayList<Restaurant> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.card_restaurant,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        Restaurant restaurant = arrayList.get(position);

        holder.textView_restaurant_name1.setText(restaurant.getName());
        holder.textView_restaurant_address1.setText(restaurant.getAddress());
        holder.textView_ratings.setText("Ratings: " + floor(restaurant.getRatings()) );
        holder.textView_restaurant_time1.setText(( String.format( "%.2f", restaurant.getTravellingTime()) + "mins"));
        holder.crowdLevel.setText("Crowd Level: " + restaurant.getCrowdLevel());
        holder.ratingBar.setRating((float) floor(restaurant.getRatings()));
        //holder.ratingBar.setNumStars(5);
        //holder.ratingBar.setRating(5);



        Picasso.get()
                .load(restaurant.getImage())
                .fit()
                .into(holder.imageView_restaurant1);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DisplayRestaurant.class);
                intent.putExtra("restaurant_url", restaurant.getImage());
                intent.putExtra("restaurant_name", restaurant.getName());
                intent.putExtra("restaurant_address", restaurant.getAddress());
                intent.putExtra("restaurant_latitude", restaurant.getLatitude());
                intent.putExtra("restaurant_longitude", restaurant.getLongitude());
                intent.putExtra("restaurant_opening_hours_time", restaurant.isOpenNow());
                intent.putExtra("restaurant_crowd_level_value", restaurant.getCrowdLevel() );
                intent.putExtra("ratings", restaurant.getRatings() );
                intent.putExtra("restaurant_travelling_time", String.format( "%.2f", restaurant.getTravellingTime()));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (arrayList==null) {return 0;}
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView,textView_restaurant_name1,textView_restaurant_address1,textView_restaurant_time1, crowdLevel, textView_ratings;
        private ImageView imageView_bg1,imageView_restaurant1;
        String restaurant_url;
        CardView cardView;
        RatingBar ratingBar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_restaurant_name1 = itemView.findViewById(R.id.textView_restaurant_name1);
            textView_restaurant_address1 = itemView.findViewById(R.id.textView_restaurant_address1);
            textView_restaurant_time1 = itemView.findViewById(R.id.textView_restaurant_time1);
            imageView_bg1 = itemView.findViewById(R.id.imageView_bg1);
            imageView_restaurant1 = itemView.findViewById(R.id.imageView_restaurant1);
            cardView = itemView.findViewById(R.id.cardView);
            crowdLevel = itemView.findViewById(R.id.textView_crowd_level);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            textView_ratings = itemView.findViewById(R.id.textView_rating);
        }

    }

    public void setArrayList(ArrayList<Restaurant> restaurantArrayList) {this.arrayList = restaurantArrayList;}
}
