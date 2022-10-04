package com.example.mowgli;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mowgli.models.Reservation;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.parceler.Parcels;

public class ReservationDetailActivity extends AppCompatActivity {
    private static final String TAG = "ReservationDetailActivity";
    private ImageView ivPoster, ivBarcode;
    private TextView tvShowingDate, tvShowingTime, tvOrderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);

        ivPoster = findViewById(R.id.ivPoster);
        ivBarcode = findViewById(R.id.ivBarcode);
        tvShowingDate = findViewById(R.id.tvShowingDate);
        tvShowingTime = findViewById(R.id.tvShowingTime);
        tvOrderNumber = findViewById(R.id.tvOrderNumber);

        Reservation reservation = Parcels.unwrap(getIntent().getParcelableExtra("reservation"));
        Glide.with(this).load(reservation.getMovieShowing().getMovie().getPosterPath()).into(ivPoster);
        tvShowingDate.setText(reservation.getMovieShowing().getShowDate());
        tvShowingTime.setText(reservation.getMovieShowing().getShowTime());
        tvOrderNumber.setText(reservation.getReservationId());

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            String info = reservation.getReservationInfo();
            if (info.length() > 80) {
                info = info.substring(0, 80);
            }
            BitMatrix bitMatrix = multiFormatWriter.encode(info, BarcodeFormat.CODE_128,
                    600, 300);
            Bitmap bitmap = Bitmap.createBitmap(600, 300, Bitmap.Config.RGB_565);
            for (int i = 0; i < 600; i++){
                for (int j = 0; j < 300; j++){
                    bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                }
            }
            ivBarcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to generate Barcode");
        }


    }
}