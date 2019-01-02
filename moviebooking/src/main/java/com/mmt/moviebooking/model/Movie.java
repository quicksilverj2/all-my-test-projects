package com.mmt.moviebooking.model;

import com.mmt.moviebooking.model.cast.CastMember;
import com.mmt.moviebooking.store.CastStore;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jitheshrajan on 11/17/18.
 */
@ToString
public class Movie implements Comparable<Movie> {

    private String id;
    private String name;
    private Long releaseDate;
    private List<CastMember> cast;


    private long runningTime;
    private Category category;
    private Genre genre;

    public List<UserRating> userRatings;
    public BigDecimal consolidatedRating;

    public BigDecimal getConsolidatedRating() {

        // todo calculate here

        return consolidatedRating;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getReleaseDate() {
        return releaseDate;
    }

    public List<CastMember> getCast() {
        return cast;
    }

    public Movie(String id, String name, Long releaseDate, List<CastMember> cast) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.cast = cast;
    }

    @Override
    public int compareTo(Movie that) {
        // since we want to store the rating in reverse!

        if(this.consolidatedRating == null || that.consolidatedRating == null){
            return 0;
        }

        return this.consolidatedRating.compareTo(that.consolidatedRating) * -1;
    }

    public static class MovieBuilder{
        private String name;
        private Long releaseDate;
        private List<CastMember> cast;


        public MovieBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public MovieBuilder setReleaseDate(Long releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public MovieBuilder setCast(List<CastMember> cast) {

            for(CastMember castMember : cast){
                CastStore.INSTANCE.registerCastMember(castMember) ;
            }

            this.cast = cast;
            return this;
        }

        public Movie build(){

            // todo insert new id from singleton here!

            return new Movie(System.currentTimeMillis()+"",
                    this.name,
                    this.releaseDate,
                    this.cast);
        }
    }

    public static class MovieUpdater{

        public static Movie addGenre(Movie movie, Genre genre){

            if(movie != null){
                movie.genre = genre;
            }

            return movie;
        }

        public static Movie addCategory(Movie movie, Category category){

            if(movie != null){
                movie.category = category;
            }

            return movie;
        }

        public static Movie addRunningTime(Movie movie, long runningTime){

            if(movie != null){
                movie.runningTime = runningTime;
            }

            return movie;
        }

    }
}
