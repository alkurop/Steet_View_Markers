package com.github.alkurop.streetviewmarker;

import com.google.android.gms.maps.model.LatLng;

public class UpdatePosition {
  public LatLng center;
  public long radius;

  public UpdatePosition(LatLng center, long radius) {
    this.center = center;
    this.radius = radius;
  }

  @Override
  public String toString() {
    return "UpdatePosition{" +
        "center=" + center +
        ", radius=" + radius +
        '}';
  }
}
