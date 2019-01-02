package com.mmt.moviebooking.model;

import java.util.HashMap;

/**
 * Created by jitheshrajan on 11/17/18.
 */
public class CinemaHall {

    private String id;
    private String name;
    private long seats;
    private Location location;
    private HashMap<String, Integer> seatingArrangement;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getSeats() {
        return seats;
    }

    public Location getLocation() {
        return location;
    }

    public HashMap<String, Integer> getSeatingArrangement() {
        return seatingArrangement;
    }

    public CinemaHall(String id, String name, long seats, Location location, HashMap<String, Integer> seatingArrangement) {
        this.id = id;
        this.name = name;
        this.seats = seats;
        this.location = location;
        this.seatingArrangement = seatingArrangement;
    }

    class CinemaHallBuilder{
        private String name;
        private long seats;
        private Location location;
        private HashMap<String, Integer> seatingArrangement;

        public CinemaHallBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public CinemaHallBuilder setSeats(long seats) {
            this.seats = seats;
            return this;
        }

        public CinemaHallBuilder setLocation(Location location) {
            this.location = location;
            return this;
        }

        public CinemaHallBuilder setSeatingArrangement(HashMap<String, Integer> seatingArrangement) {
            this.seatingArrangement = seatingArrangement;
            return this;
        }


        public CinemaHall build(){
            // todo insert new id from singleton here!

            return new CinemaHall(
                    System.currentTimeMillis()+"",
                    this.name,
                    this.seats,
                    this.location,
                    this.seatingArrangement);
        }
    }






}
