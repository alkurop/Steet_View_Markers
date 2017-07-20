package com.github.alkurop.streetviewmarker;

/**
 * Created by alkurop on 31.05.16.
 */
public class /**/MarkerGeoData {
    public Place place;
    public double distance;
    public double azimuth;

    public MarkerGeoData (Place place, double distance, double azimuth) {
        this.distance = distance;
        this.azimuth = azimuth;
        this.place = place;
    }

    public String getId () {
        return place.getId();
    }

    @Override public String toString () {
        return "MarkerGeoData{" +
                  "distance=" + distance +
                  ", azimuth=" + azimuth +
                  '}';
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarkerGeoData that = (MarkerGeoData) o;

        return place.equals(that.place);

    }

    @Override
    public int hashCode() {
        return place.hashCode();
    }
}
