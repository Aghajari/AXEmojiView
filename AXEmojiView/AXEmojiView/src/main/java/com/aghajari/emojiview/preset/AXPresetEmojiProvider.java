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


package com.aghajari.emojiview.preset;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.emoji.EmojiCategory;
import com.aghajari.emojiview.emoji.EmojiProvider;

public abstract class AXPresetEmojiProvider extends AXPresetEmojiReplacer implements EmojiProvider {

    public static EmojiCategory[] emojiCategories = null;

    public AXPresetEmojiProvider(Context context, int[] icons) {
        AXPresetEmojiLoader.init(context, getEmojiData());

        if (emojiCategories == null) {
            int len = getEmojiData().getCategoriesLength();
            emojiCategories = new EmojiCategory[len];
            for (int c = 0; c < len; c++) {
                emojiCategories[c] = createCategory(c, icons[c]);
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

    protected EmojiCategory createCategory(int i, int icon) {
        return new AXPresetEmojiCategory(i, icon, getEmojiData());
    }

}
