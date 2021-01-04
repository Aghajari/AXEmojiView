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


package com.aghajari.emojiview.emoji.iosprovider;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aghajari.emojiview.R;
import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.emoji.EmojiCategory;
import com.aghajari.emojiview.emoji.EmojiData;
import com.aghajari.emojiview.emoji.EmojiProvider;

public final class AXIOSEmojiProvider extends AXIOSEmojiReplacer implements EmojiProvider {

    public static EmojiCategory[] emojiCategories = null;

    public AXIOSEmojiProvider(Context context) {
        AXIOSEmojiLoader.init(context);

        if (emojiCategories == null) {
            int[] icons = new int[]{
                    R.drawable.emoji_ios_category_people,
                    R.drawable.emoji_ios_category_nature,
                    R.drawable.emoji_ios_category_food,
                    R.drawable.emoji_ios_category_activity,
                    R.drawable.emoji_ios_category_travel,
                    R.drawable.emoji_ios_category_objects,
                    R.drawable.emoji_ios_category_symbols,
                    R.drawable.emoji_ios_category_flags
            };

            emojiCategories = new EmojiCategory[EmojiData.titles.length];
            for (int c = 0; c < EmojiData.titles.length; c++) {
                emojiCategories[c] = new AXIOSEmojiCategory(c, icons[c]);
            }
        }
    }

    public AXIOSEmojiProvider(Context context, int[] icons) {
        AXIOSEmojiLoader.init(context);

        if (emojiCategories == null) {
            emojiCategories = new EmojiCategory[EmojiData.titles.length];
            for (int c = 0; c < EmojiData.titles.length; c++) {
                emojiCategories[c] = new AXIOSEmojiCategory(c, icons[c]);
            }
        }
    }

    @Override
    @NonNull
    public EmojiCategory[] getCategories() {
        return emojiCategories;
    }

    @Override
    public void destroy() {

    }

    public static class AXIOSEmojiCategory implements EmojiCategory {
        Emoji[] DATA;
        String title;
        int icon;

        public AXIOSEmojiCategory(int i, int icon) {
            DATA = new Emoji[EmojiData.releaseData[i].length];
            for (int j = 0; j < EmojiData.releaseData[i].length; j++) {
                DATA[j] = new AXIOSEmoji(EmojiData.releaseData[i][j]);
            }
            title = EmojiData.titles[i];
            this.icon = icon;
        }

        @NonNull
        @Override
        public Emoji[] getEmojis() {
            return DATA;
        }

        @Override
        public int getIcon() {
            return icon;
        }

        @Override
        public CharSequence getTitle() {
            return title;
        }
    }
}
