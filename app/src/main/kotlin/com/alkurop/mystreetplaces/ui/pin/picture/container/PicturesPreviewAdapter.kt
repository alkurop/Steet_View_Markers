package com.alkurop.mystreetplaces.ui.pin.picture.container

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.alkurop.mystreetplaces.ui.pin.picture.view.PreviewPictureFragment

class PicturesPreviewAdapter(fm: FragmentManager, val items: List<PictureWrapper>) : FragmentPagerAdapter(fm) {

    val fragments: List<Fragment>

    init {
        fragments = items.map {
            val fragment = PreviewPictureFragment()
            val args = Bundle()
            args.putParcelable(PreviewPictureFragment.PICTURE_WRAPPER_KEY, it)
            fragment.arguments = args
            fragment
        }
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getItemPosition(`object`: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }
}