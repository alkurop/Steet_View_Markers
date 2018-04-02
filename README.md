# Street View Markers
[![](https://jitpack.io/v/alkurop/Steet_View_Markers.svg)](https://jitpack.io/#alkurop/Steet_View_Markers)


Add clickable markers to android street view.


1. Layout

    
``` 
<com.github.alkurop.streetviewmarker.StreetMarkerView
        android:id="@+id/marker_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
2. Code

```

val focusLocation = LatLng(50.431849329572735, 30.515935972507187)
    marker_view.focusToLocation(focusLocation)
    
marker_view.focusToLocation(focusLocation)

marker_view.onMarkerClickListener = {
    Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
}

marker_view.onMarkerLongClickListener = {
    Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
}
marker_view.onStreetLoadedSuccess = { loadedSuccess ->
    if (!loadedSuccess) {
        Toast.makeText(this, "No street found at this location", Toast.LENGTH_SHORT).show()
    }
}

marker_view.onCameraUpdateListener = { cameraPosition ->
    Log.d(this.javaClass.canonicalName, "Load new markres list for position $cameraPosition")
}

val markerLocation = LatLng(50.431781340278064, 30.51638161763549)
val marker = PinPlace("1", markerLocation, "title")

marker_view.setMarkers(hashSetOf(marker))

```



To view an example see 
https://github.com/alkurop/Steet_View_Markers/blob/master/app/src/main/kotlin/com/alkurop/mystreetplaces/ui/street/StreetFragment.kt

https://play.google.com/store/apps/details?id=com.alkurop.mystreetplaces

![](https://github.com/alkurop/gif-repo/raw/master/demo.gif)
