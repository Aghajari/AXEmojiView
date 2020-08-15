package com.aghajari.emojiview.view;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.aghajari.emojiview.listener.FindVariantListener;
import com.aghajari.emojiview.utils.Utils;

public class AXEmojiSingleRecyclerView extends RecyclerView {
    FindVariantListener variantListener;

    public AXEmojiSingleRecyclerView(@NonNull Context context, FindVariantListener variantListener) {
        super(context);
        this.variantListener =variantListener;
        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(Utils.getGridCount(context), StaggeredGridLayoutManager.VERTICAL);
        this.setLayoutManager(lm);

        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    boolean skipTouch = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(variantListener.findVariant()!=null && variantListener.findVariant().onTouch(event,this)) return true;
        return super.dispatchTouchEvent(event);
    }
}
