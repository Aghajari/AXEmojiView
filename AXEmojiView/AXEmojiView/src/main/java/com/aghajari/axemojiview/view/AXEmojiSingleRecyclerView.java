package com.aghajari.axemojiview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.aghajari.axemojiview.utils.Utils;

public class AXEmojiSingleRecyclerView extends RecyclerView {

    public AXEmojiSingleRecyclerView(@NonNull Context context) {
        super(context);
        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(Utils.getGridCount(context), StaggeredGridLayoutManager.VERTICAL);
        this.setLayoutManager(lm);
    }

}
