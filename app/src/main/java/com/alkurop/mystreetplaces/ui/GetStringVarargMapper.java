package com.alkurop.mystreetplaces.ui;


import android.content.Context;
import android.support.annotation.StringRes;
import java.util.List;

public class GetStringVarargMapper {
  private final Context mContext;

  public GetStringVarargMapper(Context context) {
    this.mContext = context;
  }

  public String getString(@StringRes int stringId, List<Object> args) {
    Object[] array = args.toArray(new Object[args.size()]);
    return mContext.getString(stringId, array);
  }
}
