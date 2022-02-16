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
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.emoji.EmojiData;


@SuppressWarnings("serial")
public class AXPresetEmoji extends Emoji {

    public AXPresetEmoji(final String code,final EmojiData emojiData) {
        super(code, -1);

        boolean isVariants = emojiData.isColoredEmoji(code);
        if (isVariants) {
            AXPresetEmojiLoader.globalQueue.postRunnable(() -> {
                String[] vars = emojiData.getEmojiVariants(code);
                AXPresetEmoji[] variants = new AXPresetEmoji[vars.length];
                for (int i = 0; i < vars.length; i++) {
                    variants[i] = new AXPresetEmoji(vars[i], -1, 0);
                }
                setVariants(variants);
            });
        }
    }

    private AXPresetEmoji(String code, int resource, int count) {
        super(code, resource, new Emoji[count]);
    }

    @Override
    public Drawable getDrawable(final View view) {
        return AXPresetEmojiLoader.getEmojiBigDrawable(getUnicode());
    }

    @Override
    public Drawable getDrawable(final Context context) {
        return AXPresetEmojiLoader.getEmojiBigDrawable(getUnicode());
    }

    public Drawable getDrawable() {
        return AXPresetEmojiLoader.getEmojiBigDrawable(getUnicode());
    }

    public Drawable getDrawable(int size, boolean fullSize) {
        return AXPresetEmojiLoader.getEmojiDrawable(getUnicode(), size, fullSize);
    }

    @Override
    public Bitmap getEmojiBitmap() {
        return AXPresetEmojiLoader.getEmojiBitmap(getUnicode());
    }

    @Override
    public boolean isLoading() {
        return getEmojiBitmap() == null;
    }
}
