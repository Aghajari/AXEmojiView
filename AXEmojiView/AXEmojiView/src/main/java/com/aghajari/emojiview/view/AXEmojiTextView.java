package com.aghajari.emojiview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import androidx.annotation.CallSuper;
import androidx.annotation.DimenRes;
import androidx.annotation.Px;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.R;

public class AXEmojiTextView extends AppCompatTextView {
  private float emojiSize;

  public AXEmojiTextView(final Context context) {
    this(context, null);
  }

  public AXEmojiTextView(final Context context, final AttributeSet attrs) {
    super(context, attrs);

    final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
    final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;

    if (attrs == null) {
      emojiSize = defaultEmojiSize;
    } else {
      final TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.AXEmojiTextView);

      try {
        emojiSize = a.getDimension(R.styleable.AXEmojiTextView_emojiSize_TV, defaultEmojiSize);
      } finally {
        a.recycle();
      }
    }

    setText(getText());
  }

  @Override @CallSuper
  public void setText(final CharSequence rawText, final BufferType type) {
    if (AXEmojiManager.isInstalled()) {
      final CharSequence text = rawText == null ? "" : rawText;
      final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
      final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
      final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;
      AXEmojiManager.getInstance().replaceWithImages(getContext(), spannableStringBuilder, emojiSize, fontMetrics,defaultEmojiSize);
      super.setText(spannableStringBuilder, type);
    }else{
      super.setText(rawText,type);
    }
  }
  
  public void setText2(final CharSequence rawText) {
    if (AXEmojiManager.isInstalled()) {
      final CharSequence text = rawText == null ? "" : rawText;
      final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
      final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
      final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;
      AXEmojiManager.getInstance().replaceWithImages(getContext(), spannableStringBuilder, emojiSize, fontMetrics,defaultEmojiSize);
      super.setText(spannableStringBuilder);
    }else{
      super.setText(rawText);
    }

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
  
  public  float getEmojiSize() {
	  return emojiSize;
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
