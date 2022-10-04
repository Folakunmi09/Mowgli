package com.example.mowgli.models;

import org.parceler.Parcel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

@Parcel
public class MovieShowing {
    String showingId = "";
    Movie movie;
    //TODO: Figure out using Date format for showtime & showDate
    String showDate;
    String showTime;
    int maxCapacity;
    int availableTickets;
    int purchasedTickets;

    public MovieShowing(){}

    public MovieShowing(Movie movie, String showDate, String showTime, int maxCapacity) {
        this.movie = movie;
        this.showDate = showDate;
        this.showTime = showTime;
        this.maxCapacity = maxCapacity;
        this.availableTickets = maxCapacity;
        purchasedTickets = 0;
        setShowingId();
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setShowingId(String id) {
        setShowingId();
    }

    public void setShowingId(){
        String id = String.valueOf(hashCode());

        if (showDate != null) {
            String[] showDateArr = showDate.split("-");
            for (String str:showDateArr){
                id += str;
            }
        }

        if (showTime != null) {
            String[] showTimeArr = showTime.split(":");
            id += showTimeArr[0] + showTimeArr[1].substring(0, 2);
            this.showingId = id;
        }

    }

    public String getShowingId() {
        return showingId;
    }


    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getAvailableTickets(){
        return availableTickets;
    }

    public int getPurchasedTickets(){
        return purchasedTickets;
    }
    public void updateTicketCount(int tickets) {
        this.purchasedTickets += tickets;
        this.availableTickets -= tickets;
    }

    public void setShowTime(String showTime){
        this.showTime = showTime;
    }

    public String getShowTime(){
        return showTime;
    }

    public void setShowDate(String showDate){
        this.showDate = showDate;
    }

    public String getShowDate(){
        return showDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieShowing)) return false;
        MovieShowing that = (MovieShowing) o;
        return getMaxCapacity() == that.getMaxCapacity() && getAvailableTickets() == that.getAvailableTickets() && getPurchasedTickets() == that.getPurchasedTickets() && Objects.equals(getShowingId(), that.getShowingId()) && Objects.equals(getMovie(), that.getMovie()) && Objects.equals(getShowDate(), that.getShowDate()) && Objects.equals(getShowTime(), that.getShowTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMovie(), getShowDate(), getShowTime(), getMaxCapacity());
    }

    @Override
    public String toString() {
        return "MovieShowing{" +
                "showingId='" + showingId + '\'' +
                ", maxCapacity='" + maxCapacity + '\'' +
                ", availableTickets='" + availableTickets + '\'' +
                ", purchasedTickets='" + purchasedTickets + '\'' +
                '}';
    }
}
