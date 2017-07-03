package com.alkurop.mystreetplaces.ui

import android.view.View
import com.stanfy.enroscar.views.StateHelper.STATE_EMPTY
import com.stanfy.enroscar.views.StateView


fun StateView?.renderLoading(isLoading: Boolean?) {
  if (isLoading != null && this != null) {
    if (isLoading.not()) {
      if (this.state != STATE_EMPTY)
        this.setNormalState()
    } else {
      this.setLoadingState()
    }
  }
}

fun View?.renderVisibility(visibility: Int?) {
  when (visibility) {
    View.GONE,
    View.VISIBLE,
    View.INVISIBLE -> {
      this?.visibility = visibility
    }
    else -> {
    }
  }
}
