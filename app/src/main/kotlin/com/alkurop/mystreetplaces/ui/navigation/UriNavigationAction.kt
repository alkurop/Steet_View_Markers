package com.alkurop.mystreetplaces.ui.navigation

import android.content.Intent
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by alkurop on 4/14/17.
 */
data class UriNavigationAction(val intent: Intent) : NavigationAction, Parcelable {
  companion object {
    @JvmField val CREATOR: Parcelable.Creator<UriNavigationAction> = object : Parcelable.Creator<UriNavigationAction> {
      override fun createFromParcel(source: Parcel): UriNavigationAction = UriNavigationAction(source)
      override fun newArray(size: Int): Array<UriNavigationAction?> = arrayOfNulls(size)
    }
  }

  constructor(source: Parcel) : this(source.readParcelable<Intent>(Intent::class.java.classLoader))

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel?, flags: Int) {
    dest?.writeParcelable(intent, 0)
  }
}
