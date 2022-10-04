package com.example.mowgli.models;

import android.graphics.Bitmap;

import org.parceler.Parcel;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

@Parcel
public class Reservation {

    String reservationId;
    String reservationEmail;
    MovieShowing movieShowing;
    int numOfTickets;
    String reservationInfo;

    public Reservation(){
    }

    public Reservation(String reservationEmail, MovieShowing movieShowing, int numOfTickets, String reservationInfo) {
        this.reservationEmail = reservationEmail;
        this.movieShowing = movieShowing;
        this.numOfTickets = numOfTickets;
        this.reservationInfo = reservationInfo;
        setReservationId();
    }

    public void setReservationId(String reservationId) {
        setReservationId();
    }

    public void setReservationId() {
        this.reservationId = String.valueOf(hashCode());
        if (reservationId.startsWith("-")) {
            reservationId = reservationId.substring(1);
        }
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationEmail(String reservationEmail) {
        this.reservationEmail = reservationEmail;
    }

    public String getReservationEmail() {
        return reservationEmail;
    }

    public void setMovieShowing(MovieShowing movieShowing) {
        this.movieShowing = movieShowing;
    }

    public MovieShowing getMovieShowing() {
        return movieShowing;
    }

    public void setNumOfTickets(int numOfTickets) {
        this.numOfTickets = numOfTickets;
    }

    public int getNumOfTickets() {
        return numOfTickets;
    }

    public String getReservationInfo() {
        return reservationInfo;
    }

    public void setReservationInfo(String reservationInfo) {
        this.reservationInfo = reservationInfo;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", reservationEmail='" + reservationEmail + '\'' +
                ", movieShowing='" + movieShowing.toString() + '\'' +
                ", numOfTickets='" + numOfTickets + '\'' +
                ", reservationInfo='" + reservationInfo + '\'' +

                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return getNumOfTickets() == that.getNumOfTickets() && Objects.equals(getReservationEmail(), that.getReservationEmail()) && Objects.equals(getMovieShowing(), that.getMovieShowing()) && Objects.equals(getReservationInfo(), that.getReservationInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReservationEmail(), getMovieShowing(), getNumOfTickets(), getReservationInfo());
    }

}
