package com.aghajari.emojiview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;

import androidx.annotation.CallSuper;
import androidx.annotation.DimenRes;
import androidx.annotation.Px;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import android.util.AttributeSet;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.AXEmojiUtils;
import com.aghajari.emojiview.R;
import com.aghajari.emojiview.emoji.Emoji;

/** Reference implementation for an EmojiAutoCompleteTextView with emoji support. */
public class AXEmojiMultiAutoCompleteTextView extends AppCompatMultiAutoCompleteTextView {
  private float emojiSize;

  public AXEmojiMultiAutoCompleteTextView(final Context context) {
    this(context, null);
  }

  public AXEmojiMultiAutoCompleteTextView(final Context context, final AttributeSet attrs) {
    super(context, attrs);


    final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
    final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;

    if (attrs == null) {
      emojiSize = defaultEmojiSize;
    } else {
      final TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.AXEmojiMultiAutoCompleteTextView);

      try {
        emojiSize = a.getDimension(R.styleable.AXEmojiMultiAutoCompleteTextView_emojiSize_MEDT, defaultEmojiSize);
      } finally {
        a.recycle();
      }
    }

    setText(getText());
  }

  @Override @CallSuper
  protected void onTextChanged(final CharSequence text, final int start, final int lengthBefore, final int lengthAfter) {
    final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
    final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;
    AXEmojiManager.getInstance().replaceWithImages(getContext(), getText(), emojiSize, fontMetrics,defaultEmojiSize);
  }

  @CallSuper public void backspace() {
    AXEmojiUtils.backspace(this);
  }

  @CallSuper public void input(final Emoji emoji) {
    AXEmojiUtils.input(this, emoji);
  }

  /** returns the emoji size */
  public final float getEmojiSize() {
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
