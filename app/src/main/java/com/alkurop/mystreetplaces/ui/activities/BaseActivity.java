package com.alkurop.mystreetplaces.ui.activities;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.alkurop.mystreetplaces.MyStreetPlacesApp;
import com.alkurop.mystreetplaces.R;
import com.alkurop.mystreetplaces.di.components.ActivityComponent;
import com.alkurop.mystreetplaces.di.modules.ActivityModule;
import com.stanfy.enroscar.views.GUIUtils;

/**
 * Base class for app activities.
 * Handles common behaviour, and makes base for injections.
 */
public abstract class BaseActivity extends AppCompatActivity {

  private ActivityComponent component;

  private Toolbar toolbar;

  /** Returns a component instance capable to inject this activity scoped objects. */
  protected ActivityComponent component() {
    if (component == null) {
      MyStreetPlacesApp app = (MyStreetPlacesApp) getApplicationContext();
      component = app.component.activityComponent(new ActivityModule(this));
    }
    return component;
  }

  /**
   * Set activity root view and set up tool bar.
   * This method will crash if toolbar is not found in the layout.
   */
  protected void setupRootView(@LayoutRes int layoutId) {
    setContentView(layoutId);
    toolbar = GUIUtils.find(this, R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
    }
  }

  /** Return a tool bar that was set up with {@link #setupRootView(int)}. */
  protected Toolbar getToolbar() {
    if (toolbar == null) {
      throw new IllegalStateException("Tool bar is not initialized. Have you called setupRootView?");
    }
    return toolbar;
  }
}
