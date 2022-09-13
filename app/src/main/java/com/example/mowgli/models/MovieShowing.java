package com.example.mowgli.models;

public class MovieShowing {

    String movieTitle;
    int maxCapacity;
    int availableTickets;
    int purchasedTickets;

    public MovieShowing(){}

    public MovieShowing(int maxCapacity) {
        purchasedTickets = 0;
        this.maxCapacity = maxCapacity;
        this.availableTickets = maxCapacity;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieTitle() {
        return movieTitle;
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


    @Override
    public String toString() {
        return "MovieShowing{" +
                "movieTitle='" + movieTitle + '\'' +
                ", maxCapacity='" + maxCapacity + '\'' +
                ", availableTickets='" + availableTickets + '\'' +
                ", purchasedTickets='" + purchasedTickets + '\'' +
                '}';
    }
}
