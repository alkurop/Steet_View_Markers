package com.github.alkurop.streetviewmarker;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by alkurop on 2/3/17.
 */

public interface Place extends Parcelable {

  @NonNull String getId();

  @NonNull LatLng getLocation();

  @Nullable String getMarkerPath();

  @DrawableRes int getDrawableRes();

  @Nullable Bitmap getBitmap();
}
