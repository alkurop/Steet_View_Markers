package com.alkurop.mystreetplaces.ui.activities;

import android.support.annotation.LayoutRes;
import android.view.View;

public interface ActivityRoot {

  void setRootView(View view);

  void setRootView(@LayoutRes int layoutId);

}
