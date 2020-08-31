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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.emoji.EmojiCategory;
import com.aghajari.emojiview.listener.OnEmojiActions;
import com.aghajari.emojiview.shared.RecentEmoji;
import com.aghajari.emojiview.shared.VariantEmoji;
import com.aghajari.emojiview.utils.Utils;
import com.aghajari.emojiview.view.AXEmojiImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AXSingleEmojiPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    EmojiCategory[] categories;
    RecentEmoji recentEmoji;
    VariantEmoji variantEmoji;
    OnEmojiActions events;
    public List<Integer> titlesPosition = new ArrayList<Integer>();
    List<Emoji> emojis = new ArrayList<Emoji>();

    public int getLastEmojiCategoryCount() {
        return categories[categories.length - 1].getEmojis().length;
    }

    public int getFirstTitlePosition() {
        return titlesPosition.get(0);
    }

    public AXSingleEmojiPageAdapter(EmojiCategory[] categories, OnEmojiActions events, RecentEmoji recentEmoji, VariantEmoji variantEmoji) {
        this.categories = categories;
        this.recentEmoji = recentEmoji;
        this.variantEmoji = variantEmoji;
        this.events = events;
        calItemsCount();
    }

    public void refresh() {
        titlesPosition.clear();
        emojis.clear();
        calItemsCount();
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 1) {
            FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
            StaggeredGridLayoutManager.LayoutParams lm = new StaggeredGridLayoutManager.LayoutParams(-1, Utils.dpToPx(viewGroup.getContext(), 28));
            lm.setFullSpan(true);
            frameLayout.setLayoutParams(lm);

            TextView tv = new TextView(viewGroup.getContext());
            frameLayout.addView(tv, new FrameLayout.LayoutParams(-1, -1));
            tv.setTextColor(AXEmojiManager.getTheme().getTitleColor());
            tv.setTypeface(AXEmojiManager.getTheme().getTitleTypeface());
            tv.setTextSize(16);
            tv.setPadding(Utils.dpToPx(viewGroup.getContext(), 16), Utils.dpToPx(viewGroup.getContext(), 4),
                    Utils.dpToPx(viewGroup.getContext(), 16), Utils.dpToPx(viewGroup.getContext(), 4));

            return new TitleHolder(frameLayout, tv);
        } else if (i == 2) {
            FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
            StaggeredGridLayoutManager.LayoutParams lm = new StaggeredGridLayoutManager.LayoutParams(-1, Utils.dpToPx(viewGroup.getContext(), 38));
            lm.setFullSpan(true);
            frameLayout.setLayoutParams(lm);
            return new SpaceHolder(frameLayout);
        } else {
            FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
            AXEmojiImageView emojiView = new AXEmojiImageView(viewGroup.getContext());
            int cw = Utils.getColumnWidth(viewGroup.getContext());
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(cw, cw));
            frameLayout.addView(emojiView);

            int dp6 = Utils.dpToPx(viewGroup.getContext(), 6);
            emojiView.setPadding(dp6, dp6, dp6, dp6);

            return new EmojiHolder(frameLayout, emojiView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof TitleHolder) {
            EmojiCategory category = categories[titlesPosition.indexOf(i)];
            ((TextView) ((FrameLayout) viewHolder.itemView).getChildAt(0)).setText(category.getTitle());
        } else if (viewHolder instanceof EmojiHolder) {
            FrameLayout frameLayout = (FrameLayout) viewHolder.itemView;
            final AXEmojiImageView emojiView = (AXEmojiImageView) frameLayout.getChildAt(0);

            Emoji emoji = emojis.get(i);
            if (emoji == null) return;
            emojiView.setEmoji(variantEmoji.getVariant(emoji));
            //ImageLoadingTask currentTask = new ImageLoadingTask(emojiView);
            //currentTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, emoji, null, null);

            boolean fromRecent = false;
            if (i < recentEmoji.getRecentEmojis().size()) fromRecent = true;
            emojiView.setOnEmojiActions(events, fromRecent);
        }
    }

    @Override
    public int getItemCount() {
        return emojis.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 2;
        if (titlesPosition.contains(position)) return 1;
        return 0;
    }

    int calItemsCount() {
        emojis.add(null);
        int number = 0;
        Emoji[] recents = new Emoji[recentEmoji.getRecentEmojis().size()];
        recents = recentEmoji.getRecentEmojis().toArray(recents);
        number = number + recents.length;
        emojis.addAll(Arrays.asList(recents));
        for (int i = 0; i < categories.length; i++) {
            number++;
            titlesPosition.add(number);
            emojis.add(null);
            number = number + categories[i].getEmojis().length;
            emojis.addAll(Arrays.asList(categories[i].getEmojis()));
        }
        return emojis.size();
    }

    public class TitleHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public TitleHolder(@NonNull View itemView, TextView tv) {
            super(itemView);
            this.tv = tv;
        }
    }

    public class SpaceHolder extends RecyclerView.ViewHolder {
        public SpaceHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class EmojiHolder extends RecyclerView.ViewHolder {
        AXEmojiImageView imageView;

        public EmojiHolder(@NonNull View itemView, AXEmojiImageView imageView) {
            super(itemView);
            this.imageView = imageView;
        }
    }
}
