package com.alkurop.mystreetplaces.ui.search;

import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class DividerDecorator extends RecyclerView.ItemDecoration {

    private final int mPaddingRes;
    private int mPadding;

    DividerDecorator(@DimenRes int paddingRes) {
        this.mPaddingRes = paddingRes;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (mPadding == 0) {
            mPadding = view.getContext().getResources().getDimensionPixelSize(mPaddingRes);
        }
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = mPadding;
        }
    }
}
