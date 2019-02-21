package com.zencom.weathertest.pojo;

public class City {

    private String name;
    private double lat;
    private double lng;

    public City(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    private City(Builder builder) {
        this.name = builder.name;
        this.lat = builder.lat;
        this.lng = builder.lng;
    }

    public static Builder newCity() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


    public static final class Builder {
        private String name;
        private double lat;
        private double lng;

        private Builder() {
        }

        public City build() {
            return new City(this);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder lat(double lat) {
            this.lat = lat;
            return this;
        }

        public Builder lng(double lng) {
            this.lng = lng;
            return this;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
