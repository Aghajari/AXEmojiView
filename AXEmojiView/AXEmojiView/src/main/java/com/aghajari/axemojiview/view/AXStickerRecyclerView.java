package com.aghajari.axemojiview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aghajari.axemojiview.utils.Utils;

public class AXStickerRecyclerView extends RecyclerView {

    public AXStickerRecyclerView(@NonNull Context context) {
        super(context);
        GridLayoutManager lm = new GridLayoutManager(context, Utils.getStickerGridCount(context));
        this.setLayoutManager(lm);
    }

}
