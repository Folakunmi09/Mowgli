package com.example.mowgli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mowgli.models.MovieShowing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TicketingActivity extends AppCompatActivity {
    private static final String TAG = "Ticketing Activity";

    private Button btnCompleteBooking;
    private TextView tvTicketCount, tvTitle;
    private ImageView ivPoster, ivIncrementTicket, ivDecrementTicket;
    int ticketCount;
    MovieShowing movieShowing;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing);

        btnCompleteBooking = findViewById(R.id.btnCompleteBooking);
        tvTitle = findViewById(R.id.tvTitle);
        ivPoster = findViewById(R.id.ivPoster);
        tvTicketCount = findViewById(R.id.tvTicketCount);
        ivIncrementTicket = findViewById(R.id.ivIncrementTicket);
        ivDecrementTicket = findViewById(R.id.ivDecrementTicket);

        ticketCount = Integer.parseInt(tvTicketCount.getText().toString());
        databaseReference = FirebaseDatabase.getInstance().getReference("MovieShowings");
        movieShowing = new MovieShowing(10);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvTitle.setText(extras.getString("movieTitle"));
            movieShowing.setMovieTitle(extras.getString("movieTitle"));
            Glide.with(this).load(extras.getString("posterPath")).into(ivPoster);
        }
        else{
            Log.d(TAG, "No movie title or posterPath passed to class.");
            Toast.makeText(this, "Failed to load movie title and image.", Toast.LENGTH_SHORT).show();
        }

        ivDecrementTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ticketCount > 0) {
                    ticketCount--;
                    tvTicketCount.setText(String.valueOf(ticketCount));
                }
            }
        });

        ivIncrementTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: set upper bound to per purchase limit?
                if (ticketCount < movieShowing.getMaxCapacity()) {
                    ticketCount++;
                    tvTicketCount.setText(String.valueOf(ticketCount));

                }
            }
        });

        // confirm availability and purchase ticket
        btnCompleteBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieShowing.updateTicketCount(ticketCount);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        MovieShowing mShowing = snapshot.child(movieShowing.getMovieTitle()).getValue(MovieShowing.class);

                        if (mShowing != null) {
                            if (ticketCount > mShowing.getAvailableTickets()) {
                                Toast.makeText(TicketingActivity.this, "Selected number of tickets unable. Failed to get tickets.", Toast.LENGTH_SHORT).show();
                                tvTicketCount.setText("0");
                            }
                            else{
                                mShowing.updateTicketCount(ticketCount);
                                databaseReference.child(mShowing.getMovieTitle()).setValue(movieShowing);
                            }
                        }
                        else{
                            Log.d(TAG, String.valueOf(movieShowing));
                            databaseReference.child(movieShowing.getMovieTitle()).setValue(movieShowing);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "Failed to update movieShowing");

                    }
                });

            }
        });
    }
}