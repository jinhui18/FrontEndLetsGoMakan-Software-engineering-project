package com.example.application;

import static java.lang.Math.floor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

        //testing only
        /*
        holder.textView_restaurant_name1.setText("Pizza Hut Express - NTU");
        holder.textView_restaurant_address1.setText("76 #01-04 Nanyang Drive Block N2, #1, Singapore 637331");
        holder.textView_ratings.setText("Ratings: " + floor(restaurant.getRatings()) );
        holder.textView_restaurant_time1.setText(( "5" + "mins"));
        holder.crowdLevel.setText("Crowd Level: " + "1");
        holder.ratingBar.setRating(3);
         */


        holder.textView_restaurant_name1.setText(restaurant.getName());
        holder.textView_restaurant_address1.setText(restaurant.getAddress());
        holder.textView_ratings.setText("Ratings: " + floor(restaurant.getRatings()) );
        holder.textView_restaurant_time1.setText(( String.format( "%.2f", restaurant.getTravellingTime()) + " mins away"));
        holder.crowdLevel.setText("Crowd Level: " + restaurant.getCrowdLevel());
        holder.ratingBar.setRating((float) floor(restaurant.getRatings()));

        if (restaurant.isOpenNow()){
            holder.textView_restaurant_open_closed.setText("Open/Closed: Open");
        }else{
            holder.textView_restaurant_open_closed.setText("Open/Closed: Closed");
            holder.textView_restaurant_open_closed.setTextColor(Color.RED);
        }


        if (restaurant.isTakeOut()){
            holder.textView_restaurant_takeout.setText("Take out: Available ");
        }
        else{
            holder.textView_restaurant_takeout.setText("Take out: Not available");
        }



        int price_level = restaurant.getPriceLevel();
        String price_level_text = "$";
        switch(price_level){
            case 0:
            case 1:
                price_level_text = "Price: $";
                break;
            case 2:
                price_level_text = "Price: $$";
                break;
            case 3:
                price_level_text = "Price: $$$";
                //
                break;
        }
        holder.textView_price_level.setText(price_level_text);



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
                //intent.putExtra("restaurant_website", restaurant.getWebsite());
                intent.putExtra("restaurant_price_level", restaurant.getPriceLevel());
                intent.putExtra("restaurant_takeout", restaurant.isTakeOut());
                intent.putExtra("restaurant_phone_number", restaurant.getPhoneNumber());
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

        private TextView textView,textView_restaurant_name1,textView_restaurant_address1,textView_restaurant_time1, crowdLevel, textView_ratings, textView_price_level;
        private TextView textView_restaurant_takeout, textView_restaurant_open_closed;
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
            textView_price_level = itemView.findViewById(R.id.textView_price_level);
            textView_restaurant_takeout = itemView.findViewById(R.id.textView_takeout);
            textView_restaurant_open_closed = itemView.findViewById(R.id.textView_open_closed);

        }

    }

    public void setArrayList(ArrayList<Restaurant> restaurantArrayList) {this.arrayList = restaurantArrayList;}
}
