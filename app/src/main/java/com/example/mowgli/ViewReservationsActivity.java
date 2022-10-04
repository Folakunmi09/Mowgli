package com.example.mowgli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.mowgli.adapters.ReservationAdapter;
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

import java.util.ArrayList;
import java.util.List;

public class ViewReservationsActivity extends AppCompatActivity {
    public static final String TAG = "ViewReservationsActivity";

    private RecyclerView rvReservations;
    private List<Reservation> reservations;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reservations);

        rvReservations = findViewById(R.id.rvReservations);
        reservations = new ArrayList<>();

        ReservationAdapter reservationAdapter = new ReservationAdapter(this, reservations);
        rvReservations.setAdapter(reservationAdapter);
        rvReservations.setLayoutManager(new LinearLayoutManager(this));

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Reservations");

        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().exists()) {
                    for (DataSnapshot reservationSnapshot : task.getResult().getChildren()) {
                        Reservation reservation = reservationSnapshot.getValue(Reservation.class);
                        if (reservation != null && reservation.getReservationEmail().equals(currentUser.getEmail())) {
                            reservations.add(reservation);
                        }
                    }
                    Log.d(TAG,reservations.toString());
                    reservationAdapter.notifyDataSetChanged();
                }
                else{
                    Log.d(TAG, "Returned empty reservation list for current user");
                }
            }
        });


    }
}