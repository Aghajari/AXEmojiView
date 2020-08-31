/*
 * Copyright (C) 2020 - Amir Hossein Aghajari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package com.aghajari.emojiview.view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.view.View;

import com.aghajari.emojiview.utils.Utils;

/**
 * @hide
 */
public class AXStickerRecyclerView extends RecyclerView {

    public AXStickerRecyclerView(@NonNull Context context) {
        super(context);
        final int spanCount = Utils.getStickerGridCount(context);
        GridLayoutManager lm = new GridLayoutManager(context, spanCount);
        this.setLayoutManager(lm);

        final int spacing;
        spacing = (context.getResources().getDisplayMetrics().widthPixels - (Utils.getStickerColumnWidth(context) * spanCount)) / (spanCount + 2);
        if (spacing > 0) {
            this.addItemDecoration(new ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull State state) {
                    outRect.left = spacing;
                }
            });
        }

        setOverScrollMode(OVER_SCROLL_NEVER);
    }

}
