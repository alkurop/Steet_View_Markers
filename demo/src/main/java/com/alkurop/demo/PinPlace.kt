package com.alkurop.demo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.github.alkurop.streetviewmarker.Place
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.view_street_marker.view.distanceTv
import kotlinx.android.synthetic.main.view_street_marker.view.textView

data class PinPlace(
        val pinId: String,
        val pinLocation: LatLng,
        val title: String,
        val description: String? = null
) : Place,
    Parcelable {


    override fun getId(): String = pinId

    override fun getLocation(): LatLng = pinLocation

    override fun getMarkerPath(): String? = null

    override fun getDrawableRes(): Int = 0

    @SuppressLint("SetTextI18n")
    override fun getBitmap(context: Context, distanceMeters: Int): Bitmap? {
        val li = LayoutInflater.from(context)
        val view = FrameLayout(context)
        val childView = li.inflate(R.layout.view_street_marker, view, true)

        childView.textView.text = title
        childView.distanceTv.text = "${distanceMeters}m"


        //Provide it with a layout params. It should necessarily be wrapping the
        //content as we not really going to have a parent for it.
        view.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )

        //Pre-measure the childView so that height and width don't remain null.
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        //Assign a size and position to the childView and all of its descendants
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        //Create the bitmap
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        //Create a canvas with the specified bitmap to draw into
        val c = Canvas(bitmap)

        //Render this childView (and all of its children) to the given Canvas
        view.draw(c)
        return bitmap
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PinPlace> = object : Parcelable.Creator<PinPlace> {
            override fun createFromParcel(source: Parcel): PinPlace = PinPlace(source)
            override fun newArray(size: Int): Array<PinPlace?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
        source.readString(),
        source.readParcelable<LatLng>(LatLng::class.java.classLoader),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(pinId)
        dest.writeParcelable(pinLocation, 0)
        dest.writeString(title)
        dest.writeString(description)
    }


}
