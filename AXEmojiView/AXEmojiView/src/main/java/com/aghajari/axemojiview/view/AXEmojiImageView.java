package com.aghajari.axemojiview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.emoji.Emoji;

public final class AXEmojiImageView extends AppCompatImageView {
  private static final int VARIANT_INDICATOR_PART_AMOUNT = 6;
  private static final int VARIANT_INDICATOR_PART = 5;

  public Emoji currentEmoji;

  private final Paint variantIndicatorPaint = new Paint();
  private final Path variantIndicatorPath = new Path();

  private final Point variantIndicatorTop = new Point();
  private final Point variantIndicatorBottomRight = new Point();
  private final Point variantIndicatorBottomLeft = new Point();

  private ImageLoadingTask imageLoadingTask;

  private boolean hasVariants;
  private boolean asyncLoad = AXEmojiManager.getInstance().isAsyncLoad();

  public boolean showVariants = AXEmojiManager.getEmojiViewTheme().isVariantDividerEnabled();

  public AXEmojiImageView(final Context context) {
    super(context);
    init();
  }

    public AXEmojiImageView(Context context, AttributeSet attrs) {
      super(context, attrs);
      init();
    }

    public AXEmojiImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
    variantIndicatorPaint.setColor(AXEmojiManager.getEmojiViewTheme().getVariantDividerColor());
    variantIndicatorPaint.setStyle(Paint.Style.FILL);
    variantIndicatorPaint.setAntiAlias(true);
  }

  @Override public void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    final int measuredWidth = getMeasuredWidth();
    //noinspection SuspiciousNameCombination
    setMeasuredDimension(measuredWidth, measuredWidth);
  }

  @Override protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

      variantIndicatorTop.x = w;
      variantIndicatorTop.y = h / VARIANT_INDICATOR_PART_AMOUNT * VARIANT_INDICATOR_PART;
      variantIndicatorBottomRight.x = w;
      variantIndicatorBottomRight.y = h;
      variantIndicatorBottomLeft.x = w / VARIANT_INDICATOR_PART_AMOUNT * VARIANT_INDICATOR_PART;
      variantIndicatorBottomLeft.y = h;

      variantIndicatorPath.rewind();
      variantIndicatorPath.moveTo(variantIndicatorTop.x, variantIndicatorTop.y);
      variantIndicatorPath.lineTo(variantIndicatorBottomRight.x, variantIndicatorBottomRight.y);
      variantIndicatorPath.lineTo(variantIndicatorBottomLeft.x, variantIndicatorBottomLeft.y);
      variantIndicatorPath.close();
  }

  @Override protected void onDraw(final Canvas canvas) {
    super.onDraw(canvas);
    if (showVariants) {
      if (hasVariants && getDrawable() != null) {
        canvas.drawPath(variantIndicatorPath, variantIndicatorPaint);
      }
    }
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();

    if (imageLoadingTask != null) {
      imageLoadingTask.cancel(true);
      imageLoadingTask = null;
    }
  }

  public void setEmoji(@NonNull final Emoji emoji) {
    if (!emoji.equals(currentEmoji)) {
      setImageDrawable(null);

      currentEmoji = emoji;
      hasVariants = emoji.getBase().hasVariants();

      if (imageLoadingTask != null) {
        imageLoadingTask.cancel(true);
      }

      if (AXEmojiManager.getInstance().getEmojiLoader()!=null) {
        AXEmojiManager.getInstance().getEmojiLoader().loadEmoji(this,emoji);
      }else{
        if (asyncLoad) {
          imageLoadingTask = new ImageLoadingTask(this);
          imageLoadingTask.execute(emoji);
        } else {
          this.setImageDrawable(emoji.getDrawable(getContext()));
        }
      }
    }
  }

  /**
   * Updates the emoji image directly. This should be called only for updating the variant
   * displayed (of the same base emoji), since it does not run asynchronously and does not update
   * the internal listeners.
   *
   * @param emoji The new emoji variant to show.
   */
  public void updateEmoji(@NonNull final Emoji emoji) {
    if (!emoji.equals(currentEmoji)) {
      currentEmoji = emoji;
      if (AXEmojiManager.getInstance().getEmojiLoader()!=null) {
        AXEmojiManager.getInstance().getEmojiLoader().loadEmoji(this,emoji);
      }else {
        setImageDrawable(emoji.getDrawable(this.getContext()));
      }
    }
  }
}
