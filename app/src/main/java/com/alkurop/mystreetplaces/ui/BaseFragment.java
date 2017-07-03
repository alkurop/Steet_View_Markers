package com.alkurop.mystreetplaces.ui;

import android.support.v4.app.Fragment;
import com.alkurop.mystreetplaces.MyStreetPlacesApp;
import com.alkurop.mystreetplaces.di.components.FragmentComponent;
import com.alkurop.mystreetplaces.di.modules.FragmentModule;


/** Base class for application fragments. */
public abstract class BaseFragment extends Fragment {

  private FragmentComponent component;

  /** Returns a component instance capable to inject this fragment scoped objects. */
  protected FragmentComponent component() {
    if (component == null) {
      MyStreetPlacesApp app = (MyStreetPlacesApp) getActivity().getApplicationContext();
      component = app.component.fragmentComponent(new FragmentModule(this));
    }
    return component;
  }

}
