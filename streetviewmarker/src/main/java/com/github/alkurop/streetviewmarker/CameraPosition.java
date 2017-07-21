package com.github.alkurop.streetviewmarker;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

public class CameraPosition {
    public final LatLng center;
    public final StreetViewPanoramaCamera camera;

    public CameraPosition (LatLng center, StreetViewPanoramaCamera camera) {
        this.center = center;
        this.camera = camera;
    }

    @Override
    public String toString () {
        return "UpdatePosition{" +
                "center=" + center +
                ", camera=" + camera +
                '}';
    }
}
