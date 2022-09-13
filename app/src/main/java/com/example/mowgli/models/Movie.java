package com.example.mowgli.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {

    int movieId;
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    double rating;

    // Empty constructor needed by the Parceler library
    public Movie(){

    }

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");//Path to movie's poster
        title = jsonObject.getString("title"); //movie's title
        overview = jsonObject.getString("overview");//movie's description
        rating = jsonObject.getDouble("vote_average");
        movieId = jsonObject.getInt("id");


    }

    public static List<Movie> fromJsonArray(JSONArray jsonArray) throws JSONException {
        //Method creates a list of movies from the given JSON array
        List<Movie> movies = new ArrayList<>();
        JSONObject m;
        Log.i("Movie, json", String.valueOf(jsonArray.length()));
        for (int i =0; i<jsonArray.length(); i++ ){
            Log.i("Movie, json", String.valueOf(i));
            m = jsonArray.getJSONObject(i);
            movies.add(new Movie(m));
        }
        return movies;
    }

    public String getPosterPath(){
        //if we simply return posterPath as is, it's going to be unusable. We won't be able to
        // actually get the poster from it because the current posterPath data is a shortened
        // (relative) form of what we actually need
//        return posterPath;

        return String.format("https://image.tmdb.org/t/p/w342%s", posterPath);

    }
//
    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s",backdropPath);
    }

    public String getTitle(){
        return title;
    }

    public String getOverview(){
        return overview;
    }

    public double getRating(){ return rating; }

    public int getMovieId(){ return movieId; }
}
