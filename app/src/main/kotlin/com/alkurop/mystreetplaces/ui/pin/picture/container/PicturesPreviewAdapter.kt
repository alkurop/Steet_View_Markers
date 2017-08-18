package com.alkurop.mystreetplaces.ui.pin.picture.container

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.alkurop.mystreetplaces.ui.pin.picture.view.PreviewPictureFragment

class PicturesPreviewAdapter(fm: FragmentManager, val items: MutableList<PictureWrapper>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment  {
        val pictureWrapper = items[position]
        val fragment = PreviewPictureFragment()
        val args = Bundle()
        args.putParcelable(PreviewPictureFragment.PICTURE_WRAPPER_KEY, pictureWrapper)
        fragment.arguments = args
        return fragment
    }

    override fun getCount(): Int = items.size

    override fun getItemPosition(`object`: Any?): Int = PagerAdapter.POSITION_NONE

}