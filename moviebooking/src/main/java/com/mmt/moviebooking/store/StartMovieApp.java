package com.mmt.moviebooking.store;

import com.mmt.moviebooking.model.Category;
import com.mmt.moviebooking.model.Genre;
import com.mmt.moviebooking.model.Movie;
import com.mmt.moviebooking.model.cast.Actor;
import com.mmt.moviebooking.model.cast.CastMember;
import com.mmt.moviebooking.model.cast.Director;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jitheshrajan on 11/17/18.
 */
public class StartMovieApp {

    public static void main(String[] args) {

        // adding genres to the app!

        GenreStore.INSTANCE.registerGenre(new Genre("ACTION"));
        GenreStore.INSTANCE.registerGenre(new Genre("THRILLER"));
        GenreStore.INSTANCE.registerGenre(new Genre("DRAMA"));
        GenreStore.INSTANCE.registerGenre(new Genre("COMEDY"));


        // adding categories to the app!

        CategoryStore.INSTANCE.registerCategory(new Category("REGIONAL CINEMA"));
        CategoryStore.INSTANCE.registerCategory(new Category("HOLLYWOOD"));
        CategoryStore.INSTANCE.registerCategory(new Category("BOLLYWOOD"));

        // adding movies to main store!

        List<CastMember> castMemberList = Arrays.asList(
                new Actor("Tom Cruilse"),
                new Actor("Simon Pegg"),
                new Director("Stephen Speilberg"));

        Movie.MovieBuilder movieBuilder = new Movie.MovieBuilder();
        movieBuilder.setName("Mission impossible");
        movieBuilder.setReleaseDate(System.currentTimeMillis() - 100 * 60 * 60 * 24); // movie released 100 days ago
        movieBuilder.setCast(castMemberList);
        Movie newMovie = movieBuilder.build();

        Movie.MovieUpdater.addCategory(newMovie, CategoryStore.INSTANCE.getCategoryName("HOLLYWOOD"));
        Movie.MovieUpdater.addGenre(newMovie, GenreStore.INSTANCE.getGenre("ACTION"));

        MovieStore.addMovie(newMovie);

        castMemberList = Arrays.asList(
                        new Actor("Leonardo Decaprio"),
                        new Actor("Tom Hardy"),
                        new Director("Christopher Nolan"));

        movieBuilder = new Movie.MovieBuilder();
        movieBuilder.setName("Inception");
        movieBuilder.setReleaseDate(System.currentTimeMillis() - 50 * 60 * 60 * 24); // movie released 100 days ago
        movieBuilder.setCast(castMemberList);
        newMovie = movieBuilder.build();

        Movie.MovieUpdater.addCategory(newMovie, CategoryStore.INSTANCE.getCategoryName("HOLLYWOOD"));
        Movie.MovieUpdater.addGenre(newMovie, GenreStore.INSTANCE.getGenre("THRILLER"));

        MovieStore.addMovie(newMovie);

        castMemberList = Arrays.asList(
                new Actor("Christian Bale"),
                new Actor("Morgan Freeman"),
                new Director("Christopher Nolan"));


        movieBuilder = new Movie.MovieBuilder();
        movieBuilder.setName("The Dark Knight");
        movieBuilder.setReleaseDate(System.currentTimeMillis() - 100 * 60 * 60 * 24); // movie released 100 days ago
        movieBuilder.setCast(castMemberList);
        newMovie = movieBuilder.build();

        Movie.MovieUpdater.addCategory(newMovie, CategoryStore.INSTANCE.getCategoryName("HOLLYWOOD"));
        Movie.MovieUpdater.addGenre(newMovie, GenreStore.INSTANCE.getGenre("ACTION"));

        MovieStore.addMovie(newMovie);

        // now to search!

        MovieStore.getInstance().getSearchResultListCastName("Mission").stream().forEach(movie -> System.out.println(movie.toString()));



    }
}
