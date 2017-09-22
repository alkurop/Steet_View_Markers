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

val focusLocation = arguments.getParcelable<LatLng>(FOCUS_LOCATION_KEY)

marker_view.focusToLocation(focusLocation)

marker_view.onMarkerClickListener = {
            presenter.onMarkerClicked(it)
        }

marker_view.onMarkerLongClickListener = {
            presenter.onMarkerClicked(it)
        }
marker_view.onStreetLoadedSuccess = { loadedSuccess ->
            if (!loadedSuccess) {
                presenter.errorLoadingStreetView()
            }
        }
marker_view.onCameraUpdateListener = {
            presenter.onCameraUpdate(it)
        }

```



To view an example see 
https://github.com/alkurop/Steet_View_Markers/blob/master/app/src/main/kotlin/com/alkurop/mystreetplaces/ui/street/StreetFragment.kt

https://play.google.com/store/apps/details?id=com.alkurop.mystreetplaces

![](https://github.com/alkurop/gif-repo/raw/master/demo.gif)
