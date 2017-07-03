package com.alkurop.mystreetplaces.ui.activities;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.view.View;
import com.alkurop.mystreetplaces.di.annotations.PerActivity;
import javax.inject.Inject;

/** Basic pure implementation of ActivityRoot. */
final class DefaultActivityRoot implements ActivityRoot {

  private final Activity activity;

  @Inject
  DefaultActivityRoot(@PerActivity Activity activity) {
    this.activity = activity;
  }

  @Override
  public void setRootView(View view) {
    activity.setContentView(view);
  }

  @Override
  public void setRootView(@LayoutRes int layoutId) {
    activity.setContentView(layoutId);
  }

}
