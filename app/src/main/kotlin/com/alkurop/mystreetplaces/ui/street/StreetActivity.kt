package com.alkurop.mystreetplaces.ui.street

import android.os.Bundle
import android.view.MenuItem
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.activities.BaseActivity

class StreetActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
        setupRootView(R.layout.activity_street)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.street_view)
        addStreetFragment()

    }

    private fun addStreetFragment() {
        val fragment = StreetFragment()
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