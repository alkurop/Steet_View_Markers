package com.alkurop.mystreetplaces.ui.pin.activity

import android.os.Bundle
import android.view.MenuItem
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseActivity
import com.alkurop.mystreetplaces.ui.base.BaseMvpActivity
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinFragment
import com.google.android.gms.maps.model.LatLng


class DropPinActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
        setupRootView(R.layout.activity_container)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.add_place)
        val location = intent
                .getParcelableExtra<Bundle>(BaseMvpActivity.ARGS_KEY)
                .getParcelable<LatLng>(DropPinFragment.LOCATION_KEY)
        addDropPinFragment(location)
    }


    fun addDropPinFragment(location: LatLng) {
        val fragment = DropPinFragment.getNewInstance(location)
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