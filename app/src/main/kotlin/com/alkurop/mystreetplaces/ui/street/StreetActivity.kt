package com.alkurop.mystreetplaces.ui.street

import android.os.Bundle
import android.view.MenuItem
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.activities.BaseActivity
import com.alkurop.mystreetplaces.ui.base.BaseMvpActivity
import com.google.android.gms.maps.model.LatLng

class StreetActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
        setupRootView(R.layout.activity_street)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.street_view)
        val focusLocation = intent
                .getParcelableExtra<Bundle>(BaseMvpActivity.ARGS_KEY)
                .getParcelable<LatLng>(StreetFragment.FOCUS_LOCATION_KEY)
        addStreetFragment(focusLocation)
    }

    private fun addStreetFragment(focusLocation:LatLng) {
        val fragment = StreetFragment.getNewInstance(focusLocation)
        supportFragmentManager.beginTransaction()
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