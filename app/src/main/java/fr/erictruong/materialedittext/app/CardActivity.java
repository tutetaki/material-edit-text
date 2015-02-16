package fr.erictruong.materialedittext.app;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;

public class CardActivity extends RecyclerActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int width;
        Display display = getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            width = display.getWidth();
        }

        int maxWdth = getResources().getDimensionPixelSize(R.dimen.card_max_width_material);
        if (width > maxWdth) {
            getRecyclerView().getLayoutParams().width = maxWdth;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setLayoutManager(new LinearLayoutManager(this));
        getRecyclerView().addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildPosition(view) < 1)
                    return;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    outRect.top = getResources().getDimensionPixelSize(R.dimen.margin_small);
                } else {
                    outRect.top = getResources().getDimensionPixelSize(R.dimen.margin_xxsmall);
                }
            }
        });
    }
}
