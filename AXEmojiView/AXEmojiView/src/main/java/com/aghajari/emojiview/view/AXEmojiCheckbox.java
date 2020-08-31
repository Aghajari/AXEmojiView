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


package com.aghajari.emojiview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

import androidx.annotation.CallSuper;
import androidx.annotation.DimenRes;
import androidx.annotation.Px;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.R;
import com.aghajari.emojiview.utils.Utils;

public class AXEmojiCheckbox extends AppCompatCheckBox {
    private float emojiSize;

    public AXEmojiCheckbox(final Context context) {
        this(context, null);
    }

    public AXEmojiCheckbox(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;

        if (attrs == null) {
            emojiSize = defaultEmojiSize;
        } else {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AXEmojiCheckbox);

            try {
                emojiSize = a.getDimension(R.styleable.AXEmojiCheckbox_emojiSize, defaultEmojiSize);
            } finally {
                a.recycle();
            }
        }
    }

    public AXEmojiCheckbox(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;

        if (attrs == null) {
            emojiSize = defaultEmojiSize;
        } else {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AXEmojiCheckbox);

            try {
                emojiSize = a.getDimension(R.styleable.AXEmojiCheckbox_emojiSize, defaultEmojiSize);
            } finally {
                a.recycle();
            }
        }
    }

    @Override
    @CallSuper
    public void setText(final CharSequence rawText, final BufferType type) {
        if (isInEditMode() || !AXEmojiManager.isInstalled()) {
            super.setText(rawText, type);
            return;
        }

        final CharSequence text = rawText == null ? "" : rawText;
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        //final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;
        if (AXEmojiManager.isInstalled())
            AXEmojiManager.getInstance().replaceWithImages(getContext(), this, spannableStringBuilder,
                    emojiSize>0 ? emojiSize : Utils.getDefaultEmojiSize(fontMetrics), fontMetrics);
        super.setText(spannableStringBuilder, type);
    }

    public float getEmojiSize() {
        return emojiSize;
    }

    public final void setEmojiSize(@Px final int pixels) {
        setEmojiSize(pixels, true);
    }

    public final void setEmojiSize(@Px final int pixels, final boolean shouldInvalidate) {
        emojiSize = pixels;

        if (shouldInvalidate) {
            setText(getText().toString());
        }
    }

    public final void setEmojiSizeRes(@DimenRes final int res) {
        setEmojiSizeRes(res, true);
    }

    public final void setEmojiSizeRes(@DimenRes final int res, final boolean shouldInvalidate) {
        setEmojiSize(getResources().getDimensionPixelSize(res), shouldInvalidate);
    }
}