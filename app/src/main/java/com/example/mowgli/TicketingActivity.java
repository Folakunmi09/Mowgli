package com.example.mowgli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mowgli.models.Movie;
import com.example.mowgli.models.MovieShowing;
import com.example.mowgli.models.Reservation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.parceler.Parcels;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TicketingActivity extends AppCompatActivity {
    private static final String TAG = "Ticketing Activity";

    private Button btnCompleteBooking;
    private Movie movie;
    private TextView tvTicketCount, tvTitle;
    private ImageView ivPoster, ivIncrementTicket, ivDecrementTicket;
    int ticketCount;
    MovieShowing movieShowing;
    DatabaseReference movieShowingDatabase, reservationDatabase;
    FirebaseUser currentUser;
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
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        movieShowingDatabase = FirebaseDatabase.getInstance().getReference("MovieShowings");
        reservationDatabase = FirebaseDatabase.getInstance().getReference("Reservations");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movie = Parcels.unwrap(extras.getParcelable("movieObject"));
            tvTitle.setText(movie.getTitle());
//            LocalDate today = LocalDate.now();
//            Date showDate = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
//            Date showTime = Date.from(Instant.from(LocalTime.NOON));
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            movieShowing = new MovieShowing(movie, LocalDate.now().plusDays(1).format(dateFormatter),
                    "12:00 PM", 10);
            Glide.with(this).load(movie.getPosterPath()).into(ivPoster);
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
                if (ticketCount <= movieShowing.getMaxCapacity()) {
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
                movieShowingDatabase.child(movieShowing.getShowingId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            MovieShowing mShowing = task.getResult().getValue(MovieShowing.class);
                            if (mShowing != null) {
                                if (ticketCount > mShowing.getAvailableTickets()) {
                                    Toast.makeText(TicketingActivity.this, "Selected number of tickets unable. Failed to get tickets.", Toast.LENGTH_SHORT).show();
                                    tvTicketCount.setText("0");
                                }
                                else{
                                    mShowing.updateTicketCount(ticketCount);
                                    movieShowingDatabase.child(mShowing.getShowingId()).setValue(mShowing);
                                }
                            }
                            else{
                                Log.d(TAG, String.valueOf(movieShowing));
                                movieShowingDatabase.child(movieShowing.getShowingId()).setValue(movieShowing);
                            }
                        }
                        else{
                            Log.d(TAG, "Failed to get movieShowing from database");
                        }
                    }
                });

                // todo: update reservation collection with info and user database with reservation id
                // toDO: reservation info to store in barcode - user email, movie title, no.of tickets, seats, show date, time
                // TODO: reservation info to store in database - useremail, showing id, no. of tickets, seats, reservation id
                // TODO: Showing should have ...
                // Use generateBarcode function to get Barcode.
                // For multiple seats, do comma seperated string as seat value.
                // for single seat, take note to not have the comma
                String reservationInfo = ticketCount + " tickets for " +
                        movieShowing.getMovie().getTitle() + " at " + movieShowing.getShowTime() +
                        " " +  movieShowing.getShowDate() + " for " +
                        currentUser.getEmail();


                Reservation reservation =
                        new Reservation(
                                currentUser.getEmail(),
                                movieShowing,
                                ticketCount,
                                reservationInfo);
                reservationDatabase.orderByChild("userEmail")
                        .equalTo(currentUser.getEmail())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                reservationDatabase.child(reservation.getReservationId()).setValue(reservation);
                                Log.d(TAG, "Reservation added to database");
                                Toast.makeText(TicketingActivity.this, "Reservation successful. Check bookings to view.", Toast.LENGTH_LONG).show();
                                //todo: send user confirmation email
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "Failed to add reservation to database", error.toException());
                                Toast.makeText(TicketingActivity.this, "Failed to book reservation!", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }


}