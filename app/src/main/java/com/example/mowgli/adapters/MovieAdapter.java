package com.example.mowgli.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mowgli.DetailActivity;

import com.example.mowgli.R;
import com.example.mowgli.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    Context context;
    List<Movie> movies;


    public MovieAdapter(Context context, List<Movie> movies){
        this.context = context;
        this.movies = movies;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false );
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        //Get the movie at the passed position
        Movie movie = movies.get(position);
        //Bind the movie data into the View Holder
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout itemContainer;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            itemContainer = itemView.findViewById(R.id.itemContainer);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;

            // Set it to the backdrop url if it's in landscape
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
            }else {
                //Set imageUrl to posterPath if image is in portrait orientation.
                imageUrl = movie.getPosterPath();
            }



            Glide.with(context).load(imageUrl).into(ivPoster);
            //Making use of Glide cause there's no way to directly load remote
            // images from android studio yet

            //Whenever the user clicks on a movie title, we want to show them more details about the movie
            //1.  we need to create a way to know when the click on a movie title first
            //- Register click listener on the whole row
            itemContainer.setOnClickListener(new View.OnClickListener()  {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show();


                    //Intent- First parameter is the context; second is the class of the activity to launch
                    Intent i = new Intent(context, DetailActivity.class);
//                  i.putExtra("title", movie.getTitle());
                    i.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(i); //Brings up second activity
                }
            });
        }
    }
}
