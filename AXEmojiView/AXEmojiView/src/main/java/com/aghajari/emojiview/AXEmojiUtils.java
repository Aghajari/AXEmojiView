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

package com.aghajari.emojiview;

import android.content.Context;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.utils.EmojiRange;
import com.aghajari.emojiview.utils.Utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AXEmojiUtils {
    private static final Pattern SPACE_REMOVAL = Pattern.compile("[\\s]");

    /**
     * returns true when the string contains only emojis. Note that whitespace will be filtered out.
     */
    public static boolean isOnlyEmojis(@Nullable final CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            final String inputWithoutSpaces = SPACE_REMOVAL.matcher(text).replaceAll(Matcher.quoteReplacement(""));

            return AXEmojiManager.getInstance()
                    .getEmojiRepetitivePattern()
                    .matcher(inputWithoutSpaces)
                    .matches();
        }

        return false;
    }

    /**
     * returns the emojis that were found in the given text
     */
    @NonNull
    public static List<EmojiRange> getEmojis(@Nullable final CharSequence text) {
        return AXEmojiManager.getInstance().findAllEmojis(text);
    }

    /**
     * returns the number of all emojis that were found in the given text
     */
    public static int getEmojisCount(@Nullable final CharSequence text) {
        return getEmojis(text).size();
    }

    public static SpannableStringBuilder replaceWithEmojis(Context context, Paint.FontMetrics fontMetrics, final CharSequence rawText, float emojiSize) {
        final CharSequence text = rawText == null ? "" : rawText;
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        if (AXEmojiManager.isInstalled()) {
            AXEmojiManager.getInstance().replaceWithImages(context, spannableStringBuilder, emojiSize, fontMetrics);
        }
        return spannableStringBuilder;
    }

    public static SpannableStringBuilder replaceWithEmojis(Context context, final CharSequence rawText, float emojiSize) {
        final CharSequence text = rawText == null ? "" : rawText;
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        if (AXEmojiManager.isInstalled()) {
            AXEmojiManager.getInstance().replaceWithImages(context, spannableStringBuilder, emojiSize, null);
        }
        return spannableStringBuilder;
    }


    public static SpannableStringBuilder replaceWithEmojisPxSize(Context context, final CharSequence rawText, float emojiSize) {
        final CharSequence text = rawText == null ? "" : rawText;
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        if (AXEmojiManager.isInstalled()) {
            AXEmojiManager.getInstance().replaceWithImages(context, spannableStringBuilder, Utils.dpToPx(context, emojiSize),null);
        }
        return spannableStringBuilder;
    }

    public static SpannableStringBuilder replaceWithEmojis(View view, Paint.FontMetrics fontMetrics, final CharSequence rawText, float emojiSize) {
        final CharSequence text = rawText == null ? "" : rawText;
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        if (AXEmojiManager.isInstalled()) {
            AXEmojiManager.getInstance().replaceWithImages(view.getContext(),view, spannableStringBuilder, emojiSize, fontMetrics);
        }
        return spannableStringBuilder;
    }

    public static SpannableStringBuilder replaceWithEmojis(View view, final CharSequence rawText, float emojiSize) {
        final CharSequence text = rawText == null ? "" : rawText;
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        if (AXEmojiManager.isInstalled()) {
            AXEmojiManager.getInstance().replaceWithImages(view.getContext(),view, spannableStringBuilder, emojiSize, null);
        }
        return spannableStringBuilder;
    }


    public static SpannableStringBuilder replaceWithEmojisPxSize(View view, final CharSequence rawText, float emojiSize) {
        final CharSequence text = rawText == null ? "" : rawText;
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        if (AXEmojiManager.isInstalled()) {
            AXEmojiManager.getInstance().replaceWithImages(view.getContext(),view, spannableStringBuilder, Utils.dpToPx(view.getContext(), emojiSize),null);
        }
        return spannableStringBuilder;
    }

    public static String getEmojiUnicode(@NonNull final int[] codePoints) {
        return new Emoji(codePoints, -1).getUnicode();
    }

    public static String getEmojiUnicode(@NonNull final int codePoint) {
        return new Emoji(codePoint, -1).getUnicode();
    }

    public static void backspace(@NonNull final EditText editText) {
        final KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }

    public static void input(@NonNull final EditText editText, @Nullable final Emoji emoji) {
        if (emoji != null) {
            AXEmojiManager.getInstance().getEditTextInputListener().input(editText, emoji);
        }
    }

    public static Emoji getEmojiFromUnicode(String unicode) {
        return AXEmojiManager.getInstance().emojiMap.get(unicode);
    }
}
