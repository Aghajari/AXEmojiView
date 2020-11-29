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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.R;
import com.aghajari.emojiview.sticker.RecentSticker;
import com.aghajari.emojiview.sticker.StickerProvider;
import com.aghajari.emojiview.utils.Utils;
import com.aghajari.emojiview.view.AXEmojiLayout;


public class AXCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    AXEmojiLayout pager;
    StickerProvider provider;
    RecentSticker recentSticker;
    boolean recent;

    public AXCategoryAdapter(AXEmojiLayout pager, StickerProvider provider, RecentSticker RecentStickerManager) {
        recent = !RecentStickerManager.isEmpty() && provider.isRecentEnabled();
        this.recentSticker = RecentStickerManager;
        this.pager = pager;
        this.provider = provider;
    }

    public void update() {
        recent = !recentSticker.isEmpty() && provider.isRecentEnabled();;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int iconSize = Utils.dpToPx(viewGroup.getContext(), 24);
        AXEmojiLayout layout = new AXEmojiLayout(viewGroup.getContext());
        View icon;
        if (i == 10) {
            icon = new AppCompatImageView(viewGroup.getContext());
        } else {
            icon = AXEmojiManager.getInstance().getStickerViewCreatorListener().onCreateCategoryView(viewGroup.getContext());
        }
        layout.addView(icon, new AXEmojiLayout.LayoutParams(Utils.dpToPx(viewGroup.getContext(), 7), Utils.dpToPx(viewGroup.getContext(), 7), iconSize, iconSize));
        layout.setLayoutParams(new ViewGroup.LayoutParams(Utils.dpToPx(viewGroup.getContext(), 38), Utils.dpToPx(viewGroup.getContext(), 38)));

        View selection = new View(viewGroup.getContext());
        layout.addView(selection, new AXEmojiLayout.LayoutParams(
                0, Utils.dpToPx(viewGroup.getContext(), 36), Utils.dpToPx(viewGroup.getContext(), 38), Utils.dpToPx(viewGroup.getContext(), 2)));
        selection.setBackgroundColor(AXEmojiManager.getStickerViewTheme().getSelectionColor());
        selection.setVisibility(View.GONE);
        return new RecyclerView.ViewHolder(layout) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        boolean selected = pager.getPageIndex() == i;
        AXEmojiLayout layout = (AXEmojiLayout) viewHolder.itemView;
        View icon = layout.getChildAt(0);
        View selection = (View) layout.getChildAt(1);

        if (selected) selection.setVisibility(View.VISIBLE);
        else selection.setVisibility(View.GONE);

        if (recent && i == 0) {
            Drawable dr0 = AppCompatResources.getDrawable(layout.getContext(), R.drawable.emoji_recent);
            Drawable dr = dr0.getConstantState().newDrawable();
            if (selected) {
                DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getStickerViewTheme().getSelectedColor());
            } else {
                DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getStickerViewTheme().getDefaultColor());
            }
            ((AppCompatImageView) icon).setImageDrawable(dr);
        } else {
            int i2 = i;
            if (recent) i2--;
            provider.getLoader().onLoadStickerCategory(((ImageView) icon), provider.getCategories()[i2], selected);
        }

        Utils.setClickEffect(icon, true);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setPageIndex(i);
            }
        };
        icon.setOnClickListener(listener);
        layout.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        if (recent) return provider.getCategories().length + 1;
        return provider.getCategories().length;
    }

    @Override
    public int getItemViewType(int position) {
        if (recent && position == 0) return 10;
        return -1;
    }
}
