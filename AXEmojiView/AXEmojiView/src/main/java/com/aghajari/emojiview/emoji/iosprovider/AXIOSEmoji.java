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
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;

import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.emoji.EmojiData;


@SuppressWarnings("serial")
public class AXIOSEmoji extends Emoji {

    public AXIOSEmoji(final String code) {
        super(code, -1);

        boolean isVariants = EmojiData.isColoredEmoji(code);
        if (isVariants) {
            DispatchQueue run = new DispatchQueue("emoji");
            run.postRunnable(new Runnable() {
                @Override
                public void run() {
                    AXIOSEmoji[] variants = new AXIOSEmoji[5];
                    for (int i = 0; i < 5; i++) {
                        String color = "";
                        switch (i) {
                            case 0:
                                color = "\uD83C\uDFFB";
                                break;
                            case 1:
                                color = "\uD83C\uDFFC";
                                break;
                            case 2:
                                color = "\uD83C\uDFFD";
                                break;
                            case 3:
                                color = "\uD83C\uDFFE";
                                break;
                            case 4:
                                color = "\uD83C\uDFFF";
                                break;
                        }
                        variants[i] = new AXIOSEmoji(EmojiData.addColorToCode(code, color), -1, 0);
                    }
                    setVariants(variants);
                }
            });
        }
    }

    private AXIOSEmoji(String code, int resource, int count) {
        super(code, resource, new Emoji[count]);
    }

    @NonNull
    @Override
    public Drawable getDrawable(final View view) {
        return AXIOSEmojiLoader.getEmojiBigDrawable(getUnicode());
    }

    @NonNull
    @Override
    public Drawable getDrawable(final Context context) {
        return AXIOSEmojiLoader.getEmojiBigDrawable(getUnicode());
    }

    public Drawable getDrawable() {
        return AXIOSEmojiLoader.getEmojiBigDrawable(getUnicode());
    }

    public Drawable getDrawable(int size, boolean fullSize) {
        return AXIOSEmojiLoader.getEmojiDrawable(getUnicode(), size, fullSize);
    }

    @Override
    public Bitmap getEmojiBitmap() {
        return AXIOSEmojiLoader.getEmojiBitmap(getUnicode());
    }

    @Override
    public boolean isLoading() {
        return getEmojiBitmap() == null;
    }
}
