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
import com.example.mowgli.ReservationDetailActivity;
import com.example.mowgli.models.Movie;
import com.example.mowgli.models.MovieShowing;
import com.example.mowgli.models.Reservation;

import org.parceler.Parcels;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {
    Context context;
    List<Reservation> reservations;


    public ReservationAdapter(Context context, List<Reservation> reservations){
        this.context = context;
        this.reservations = reservations;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ReservationAdapter", "onCreateViewHolder");
        View reservationView = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false );
        return new ViewHolder(reservationView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("ReservationAdapter", "onBindViewHolder " + position);
        Reservation reservation = reservations.get(position);
        holder.bind(reservation);
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout reservationContainer;
        TextView tvTitle, tvShowingDate, tvShowingTime, tvOrderNumber;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvShowingDate = itemView.findViewById(R.id.tvShowingDate);
            tvShowingTime = itemView.findViewById(R.id.tvShowingTime);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            reservationContainer = itemView.findViewById(R.id.reservationContainer);

        }

        public void bind(Reservation reservation) {
            MovieShowing movieShowing = reservation.getMovieShowing();

            if (movieShowing != null) {
                if (movieShowing.getMovie() != null) {
                    tvTitle.setText(movieShowing.getMovie().getTitle());
                    tvShowingDate.setText(movieShowing.getShowDate());
                    tvShowingTime.setText(movieShowing.getShowTime());

                    tvOrderNumber.setText(reservation.getReservationId());

                    reservationContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show();


                            //Intent- First parameter is the context; second is the class of the activity to launch
                            Intent i = new Intent(context, ReservationDetailActivity.class);
                            i.putExtra("reservation", Parcels.wrap(reservation));
                            context.startActivity(i); //Brings up second activity
                        }
                    });
                }

            }

        }
    }
}
