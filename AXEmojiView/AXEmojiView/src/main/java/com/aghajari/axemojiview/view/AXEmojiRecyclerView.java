package com.aghajari.axemojiview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aghajari.axemojiview.utils.Utils;

public class AXEmojiRecyclerView extends RecyclerView {

    public AXEmojiRecyclerView(@NonNull Context context) {
        super(context);
        GridLayoutManager lm = new GridLayoutManager(context, Utils.getGridCount(context));
        this.setLayoutManager(lm);
    }

}
