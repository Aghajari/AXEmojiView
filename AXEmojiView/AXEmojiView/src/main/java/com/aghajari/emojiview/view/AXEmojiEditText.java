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

import androidx.annotation.CallSuper;
import androidx.annotation.DimenRes;
import androidx.annotation.Px;
import androidx.appcompat.widget.AppCompatEditText;

import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.AXEmojiUtils;
import com.aghajari.emojiview.R;
import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.utils.Utils;

public class AXEmojiEditText extends AppCompatEditText {
    private float emojiSize;
    AXPopupInterface popupInterface;

    public AXEmojiEditText(final Context context) {
        this(context, null);
    }

    public AXEmojiEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;

        if (attrs == null) {
            emojiSize = defaultEmojiSize;
        } else {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AXEmojiEditText);

            try {
                emojiSize = a.getDimension(R.styleable.AXEmojiEditText_emojiSize, defaultEmojiSize);
            } finally {
                a.recycle();
            }
        }

        setText(getText());
    }

    @Override
    @CallSuper
    protected void onTextChanged(final CharSequence text, final int start, final int lengthBefore, final int lengthAfter) {
        final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();

        if (AXEmojiManager.isInstalled())
            AXEmojiManager.getInstance().replaceWithImages(getContext(), this, getText(),
                    emojiSize>0 ? emojiSize : Utils.getDefaultEmojiSize(fontMetrics), fontMetrics);
    }

    @CallSuper
    public void backspace() {
        AXEmojiUtils.backspace(this);
    }

    @CallSuper
    public void input(final Emoji emoji) {
        AXEmojiUtils.input(this, emoji);
        if (listener != null) listener.onInput(this, emoji);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        try {
            if (popupInterface != null && keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && hasFocus()) {
                InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (popupInterface.isShowing()) {
                    mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
                    popupInterface.onBackPressed();
                    return true;
                }
            }
        }catch (Exception ignore){}

        return super.onKeyPreIme(keyCode,event);
    }

    OnInputEmojiListener listener;

    public void setOnInputEmojiListener(OnInputEmojiListener listener) {
        this.listener = listener;
    }

    public void removeOnInputEmojiListener() {
        this.listener = null;
    }

    public interface OnInputEmojiListener {
        void onInput(AXEmojiEditText editText, Emoji emoji);
    }

    public float getEmojiSize() {
        return emojiSize;
    }

    /**
     * sets the emoji size in pixels and automatically invalidates the text and renders it with the new size
     */
    public final void setEmojiSize(@Px final int pixels) {
        setEmojiSize(pixels, true);
    }

    /**
     * sets the emoji size in pixels and automatically invalidates the text and renders it with the new size when {@code shouldInvalidate} is true
     */
    public final void setEmojiSize(@Px final int pixels, final boolean shouldInvalidate) {
        emojiSize = pixels;

        if (shouldInvalidate) {
            if (getText()!=null) {
                setText(getText().toString());
            }
        }
    }

    /**
     * sets the emoji size in pixels with the provided resource and automatically invalidates the text and renders it with the new size
     */
    public final void setEmojiSizeRes(@DimenRes final int res) {
        setEmojiSizeRes(res, true);
    }

    /**
     * sets the emoji size in pixels with the provided resource and invalidates the text and renders it with the new size when {@code shouldInvalidate} is true
     */
    public final void setEmojiSizeRes(@DimenRes final int res, final boolean shouldInvalidate) {
        setEmojiSize(getResources().getDimensionPixelSize(res), shouldInvalidate);
    }
}
