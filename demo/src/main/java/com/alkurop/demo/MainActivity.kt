package com.alkurop.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.marker_view

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        marker_view.onCreate(savedInstanceState)

        val focusLocation = LatLng(50.431849329572735, 30.515935972507187)
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
    }

    override fun onStop() {
        super.onStop()
        marker_view.onStop()
    }

    override fun onStart() {
        super.onStart()
        marker_view.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        marker_view.onDestroy()
    }
}
