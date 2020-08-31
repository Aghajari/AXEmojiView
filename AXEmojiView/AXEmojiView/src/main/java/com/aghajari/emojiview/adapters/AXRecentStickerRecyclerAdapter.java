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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.listener.OnStickerActions;
import com.aghajari.emojiview.sticker.RecentSticker;
import com.aghajari.emojiview.sticker.Sticker;
import com.aghajari.emojiview.sticker.StickerLoader;
import com.aghajari.emojiview.utils.Utils;

public class AXRecentStickerRecyclerAdapter extends RecyclerView.Adapter<AXRecentStickerRecyclerAdapter.ViewHolder> {
    RecentSticker recent;
    OnStickerActions events;
    StickerLoader loader;

    public AXRecentStickerRecyclerAdapter(RecentSticker recent, OnStickerActions events, StickerLoader loader){
        this.recent = recent;
        this.events = events;
        this.loader = loader;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
        View emojiView = AXEmojiManager.getInstance().getStickerViewCreatorListener().onCreateStickerView(viewGroup.getContext(),null,true);
        int cw = Utils.getStickerColumnWidth(viewGroup.getContext());
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(cw,cw));
        frameLayout.addView(emojiView);

        int dp6=Utils.dpToPx(viewGroup.getContext(),6);
        emojiView.setPadding(dp6,dp6,dp6,dp6);

        View ripple = new View(viewGroup.getContext());
        frameLayout.addView(ripple,new ViewGroup.MarginLayoutParams(cw,cw));

        return new ViewHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        FrameLayout frameLayout = (FrameLayout) viewHolder.itemView;
        final AppCompatImageView stickerView = (AppCompatImageView) frameLayout.getChildAt(0);
        View ripple = (View) frameLayout.getChildAt(1);

        @SuppressWarnings("rawtypes")
		final Sticker sticker = (Sticker) recent.getRecentStickers().toArray()[i];
        loader.onLoadSticker(stickerView,sticker);

        Utils.setClickEffect(ripple,false);

        ripple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (events !=null) events.onClick(stickerView,sticker,true);
            }
        });
            ripple.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (events!=null) return events.onLongClick(stickerView,sticker,true);
                    return false;
                }
            });
    }

    @Override
    public int getItemCount() {
        return recent.getRecentStickers().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
