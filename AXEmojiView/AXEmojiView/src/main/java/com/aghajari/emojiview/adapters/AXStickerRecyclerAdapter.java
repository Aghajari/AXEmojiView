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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.listener.OnStickerActions;
import com.aghajari.emojiview.sticker.Sticker;
import com.aghajari.emojiview.sticker.StickerCategory;
import com.aghajari.emojiview.sticker.StickerLoader;
import com.aghajari.emojiview.utils.Utils;

@SuppressWarnings("rawtypes")
public class AXStickerRecyclerAdapter extends RecyclerView.Adapter<AXStickerRecyclerAdapter.ViewHolder> {
    Sticker[] stickers;
    int count;
    OnStickerActions events;
    StickerLoader loader;
    boolean isEmptyLoading = false;
    View empty;
    StickerCategory category;

    public AXStickerRecyclerAdapter(StickerCategory category, Sticker[] stickers, OnStickerActions events, StickerLoader loader, View empty) {
        this.stickers = stickers;
        this.category = category;
        this.count = stickers.length;
        this.events = events;
        this.loader = loader;
        this.empty = empty;
        if (empty != null) {
            isEmptyLoading = true;
        } else {
            isEmptyLoading = false;
        }
    }

    RecyclerView rv = null;

    @Override
    public void onAttachedToRecyclerView(RecyclerView r) {
        super.onAttachedToRecyclerView(r);
        rv = r;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new ViewHolder(empty);
        } else {
            FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
            View emojiView = AXEmojiManager.getInstance().getStickerViewCreatorListener().onCreateStickerView(viewGroup.getContext(), category, false);
            int cw = Utils.getStickerColumnWidth(viewGroup.getContext());
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(cw, cw));
            frameLayout.addView(emojiView);

            int dp6 = Utils.dpToPx(viewGroup.getContext(), 6);
            emojiView.setPadding(dp6, dp6, dp6, dp6);

            View ripple = new View(viewGroup.getContext());
            frameLayout.addView(ripple, new ViewGroup.MarginLayoutParams(cw, cw));

            return new ViewHolder(frameLayout);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType() == 1) {
            if (rv != null && rv.getLayoutParams() != null) {
                if (viewHolder.itemView.getLayoutParams() == null) {
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(rv.getMeasuredWidth(), rv.getLayoutParams().height);
                    lp.gravity = Gravity.CENTER;
                    viewHolder.itemView.setLayoutParams(lp);
                } else {
                    viewHolder.itemView.getLayoutParams().width = rv.getMeasuredWidth();
                    viewHolder.itemView.getLayoutParams().height = rv.getLayoutParams().height;
                }
            }
        } else {
            FrameLayout frameLayout = (FrameLayout) viewHolder.itemView;
            final View stickerView = frameLayout.getChildAt(0);
            View ripple = (View) frameLayout.getChildAt(1);

            final Sticker sticker = stickers[i];
            loader.onLoadSticker(stickerView, sticker);

            Utils.setClickEffect(ripple, false);

            ripple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (events != null) events.onClick(stickerView, sticker, false);
                }
            });
            ripple.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (events != null) return events.onLongClick(stickerView, sticker, false);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (count == 0 && isEmptyLoading) return 1;
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && stickers.length == 0 && isEmptyLoading) return 1;
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
