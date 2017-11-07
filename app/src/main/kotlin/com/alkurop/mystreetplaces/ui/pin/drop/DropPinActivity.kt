package com.alkurop.mystreetplaces.ui.pin.drop

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseActivity
import com.alkurop.mystreetplaces.ui.base.BaseMvpActivity
import com.google.android.gms.maps.model.LatLng

class DropPinActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
        setupRootView(R.layout.activity_container)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.add_place)
        val parcelableExtra = intent
                .getParcelableExtra<Bundle>(BaseMvpActivity.ARGS_KEY)
        val location = parcelableExtra
                .getParcelable<LatLng>(DropPinFragment.LOCATION_KEY)
        location?.let {
            val fragment = DropPinFragment.getNewInstance(location)
            addDropPinFragment(fragment)
        }

        val id = parcelableExtra
                .getString(DropPinFragment.ID_KEY)
        id?.let {
            val fragment = DropPinFragment.getNewInstance(id)
            addDropPinFragment(fragment)
        }
    }

    fun addDropPinFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}