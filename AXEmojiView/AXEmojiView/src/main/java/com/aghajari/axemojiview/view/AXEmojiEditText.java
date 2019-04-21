package com.aghajari.axemojiview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.annotation.CallSuper;
import android.support.annotation.DimenRes;
import android.support.annotation.Px;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.AXEmojiUtils;
import com.aghajari.axemojiview.R;
import com.aghajari.axemojiview.emoji.Emoji;

public class AXEmojiEditText extends AppCompatEditText {
    private float emojiSize;

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
                emojiSize = a.getDimension(R.styleable.AXEmojiEditText_emojiSize_EDT, defaultEmojiSize);
            } finally {
                a.recycle();
            }
        }

        setText(getText());
    }

    @Override @CallSuper protected void onTextChanged(final CharSequence text, final int start, final int lengthBefore, final int lengthAfter) {
        final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;

        if (AXEmojiManager.isInstalled())
        AXEmojiManager.getInstance().replaceWithImages(getContext(), getText(), emojiSize, defaultEmojiSize);
    }
    @CallSuper public void backspace() {
        AXEmojiUtils.backspace(this);
    }

    @CallSuper public void input(final Emoji emoji) {
        AXEmojiUtils.input(this, emoji);
    }

    onInputEmojiListener listener;
    public void setOnInputEmojiListener(onInputEmojiListener listener){
        this.listener = listener;
    }
    public void removeOnInputEmojiListener(){
        this.listener = null;
    }
    public interface onInputEmojiListener{
        void onInput (AXEmojiEditText editText,Emoji emoji);
    }

    public  float getEmojiSize() {
        return emojiSize;
    }
    /** sets the emoji size in pixels and automatically invalidates the text and renders it with the new size */
    public final void setEmojiSize(@Px final int pixels) {
        setEmojiSize(pixels, true);
    }

    /** sets the emoji size in pixels and automatically invalidates the text and renders it with the new size when {@code shouldInvalidate} is true */
    public final void setEmojiSize(@Px final int pixels, final boolean shouldInvalidate) {
        emojiSize = pixels;

        if (shouldInvalidate) {
            setText(getText());
        }
    }

    /** sets the emoji size in pixels with the provided resource and automatically invalidates the text and renders it with the new size */
    public final void setEmojiSizeRes(@DimenRes final int res) {
        setEmojiSizeRes(res, true);
    }

    /** sets the emoji size in pixels with the provided resource and invalidates the text and renders it with the new size when {@code shouldInvalidate} is true */
    public final void setEmojiSizeRes(@DimenRes final int res, final boolean shouldInvalidate) {
        setEmojiSize(getResources().getDimensionPixelSize(res), shouldInvalidate);
    }
}
