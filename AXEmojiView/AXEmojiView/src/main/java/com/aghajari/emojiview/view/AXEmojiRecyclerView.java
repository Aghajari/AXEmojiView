package com.aghajari.emojiview.view;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aghajari.emojiview.listener.FindVariantListener;
import com.aghajari.emojiview.shared.VariantEmoji;
import com.aghajari.emojiview.utils.Utils;

public class AXEmojiRecyclerView extends RecyclerView {
    FindVariantListener variantListener;

    public AXEmojiRecyclerView(@NonNull Context context, FindVariantListener variantListener) {
        super(context);
        this.variantListener =variantListener;
        GridLayoutManager lm = new GridLayoutManager(context, Utils.getGridCount(context));
        this.setLayoutManager(lm);

        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(variantListener.findVariant()!=null && variantListener.findVariant().onTouch(event,this)) return true;
        return super.dispatchTouchEvent(event);
    }
}
