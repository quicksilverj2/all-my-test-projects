package com.mmt.moviebooking.model;

import java.math.BigDecimal;

/**
 * Created by jitheshrajan on 11/17/18.
 */
public class Location {

    private BigDecimal latitude;
    private BigDecimal longitude;

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {

        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public Location(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
