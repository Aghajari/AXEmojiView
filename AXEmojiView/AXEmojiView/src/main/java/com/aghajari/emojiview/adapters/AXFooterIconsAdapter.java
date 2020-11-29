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


package com.aghajari.emojiview.adapters;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.utils.Utils;
import com.aghajari.emojiview.view.AXEmojiLayout;
import com.aghajari.emojiview.view.AXEmojiPager;

public class AXFooterIconsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    AXEmojiPager pager;

    public AXFooterIconsAdapter(AXEmojiPager pager) {
        this.pager = pager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int iconSize = Utils.dpToPx(viewGroup.getContext(), 24);
        AXEmojiLayout layout = new AXEmojiLayout(viewGroup.getContext());
        AppCompatImageView icon = new AppCompatImageView(viewGroup.getContext());
        layout.addView(icon, new AXEmojiLayout.LayoutParams(Utils.dpToPx(viewGroup.getContext(), 8), Utils.dpToPx(viewGroup.getContext(), 10), iconSize, iconSize));
        layout.setLayoutParams(new ViewGroup.LayoutParams(Utils.dpToPx(viewGroup.getContext(), 40), Utils.dpToPx(viewGroup.getContext(), 44)));
        return new RecyclerView.ViewHolder(layout) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        boolean selected = pager.getViewPager().getCurrentItem() == i;
        AXEmojiLayout layout = (AXEmojiLayout) viewHolder.itemView;
        AppCompatImageView icon = (AppCompatImageView) layout.getChildAt(0);

        if (pager.getPageBinder(i) != null) {
            pager.getPageBinder(i).onBindFooterItem(icon, i, selected);
        } else {
            Drawable dr = ContextCompat.getDrawable(icon.getContext().getApplicationContext(), pager.getPageIcon(i));
            if (selected) {
                DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getEmojiViewTheme().getFooterSelectedItemColor());
            } else {
                DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getEmojiViewTheme().getFooterItemColor());
            }
            icon.setImageDrawable(dr);
        }
        Utils.setClickEffect(icon, true);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pager.getViewPager().getCurrentItem() != i) {
                    pager.setPageIndex(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pager.getPagesCount();
    }
}
