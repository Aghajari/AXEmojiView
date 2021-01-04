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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import android.util.AttributeSet;
import android.view.View;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.listener.OnEmojiActions;
import com.aghajari.emojiview.utils.Utils;

public final class AXEmojiImageView extends AppCompatImageView {
    private static final int VARIANT_INDICATOR_PART_AMOUNT = 6;
    private static final int VARIANT_INDICATOR_PART = 5;

    Emoji currentEmoji;

    private final Paint variantIndicatorPaint = new Paint();
    private final Path variantIndicatorPath = new Path();

    private final Point variantIndicatorTop = new Point();
    private final Point variantIndicatorBottomRight = new Point();
    private final Point variantIndicatorBottomLeft = new Point();

    private ImageLoadingTask imageLoadingTask;

    private boolean hasVariants;
    private boolean asyncLoad = AXEmojiManager.isAsyncLoadEnabled();

    boolean showVariants = AXEmojiManager.getEmojiViewTheme().isVariantDividerEnabled();

    OnEmojiActions actions;
    boolean fromRecent;

    public void setOnEmojiActions(OnEmojiActions actions, boolean fromRecent) {
        this.actions = actions;
        this.fromRecent = fromRecent;
    }

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

    private void init() {
        variantIndicatorPaint.setColor(AXEmojiManager.getEmojiViewTheme().getVariantDividerColor());
        variantIndicatorPaint.setStyle(Paint.Style.FILL);
        variantIndicatorPaint.setAntiAlias(true);

        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sendEmoji(currentEmoji, false);
            }
        });

        setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (actions != null) return actions.onLongClick(view, currentEmoji, fromRecent, false);
                return false;
            }
        });

        if (AXEmojiManager.isRippleEnabled()) Utils.setClickEffect(this, false);
    }


    @Override
    public void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int measuredWidth = getMeasuredWidth();
        //noinspection SuspiciousNameCombination
        setMeasuredDimension(measuredWidth, measuredWidth);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
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

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (showVariants) {
            if (hasVariants && getDrawable() != null) {
                canvas.drawPath(variantIndicatorPath, variantIndicatorPaint);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (imageLoadingTask != null) {
            imageLoadingTask.cancel(true);
            imageLoadingTask = null;
        }
    }

    public void setEmojiAsync(final Emoji emoji) {
        if (!emoji.equals(currentEmoji)) {
            setImageDrawable(null);

            currentEmoji = emoji;
            hasVariants = emoji.getBase().hasVariants();

            if (imageLoadingTask != null) {
                imageLoadingTask.cancel(true);
            }
            imageLoadingTask = new ImageLoadingTask(this);
            imageLoadingTask.execute(emoji);

            if (emoji.isLoading()) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (emoji.isLoading()) {
                            postDelayed(this, 10);
                            return;
                        }
                        invalidate();
                    }
                }, 10);
            }
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

            if (AXEmojiManager.getEmojiLoader() != null) {
                AXEmojiManager.getEmojiLoader().loadEmoji(this, emoji);
            } else {
                if (asyncLoad) {
                    imageLoadingTask = new ImageLoadingTask(this);
                    imageLoadingTask.execute(emoji);
                } else {
                    this.setImageDrawable(emoji.getDrawable(this));
                }
            }

            if (emoji.isLoading()) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (emoji.isLoading()) {
                            postDelayed(this, 10);
                            return;
                        }
                        invalidate();
                    }
                }, 10);
            }

        }
    }

    /**
     * Updates the emoji image directly. This should be called only for updating the variant
     * displayed (of the same base emoji)
     *
     * @param emoji The new emoji variant to show.
     */
    public void updateEmoji(@NonNull final Emoji emoji) {
        if (!emoji.equals(currentEmoji)) {
            currentEmoji = emoji;
            if (AXEmojiManager.getEmojiLoader() != null) {
                AXEmojiManager.getEmojiLoader().loadEmoji(this, emoji);
            } else {
                setImageDrawable(emoji.getDrawable(this));
            }
        }
    }

    public Emoji getEmoji() {
        return currentEmoji;
    }

    private void sendEmoji(Emoji emoji, boolean variants) {
        if (actions != null) actions.onClick(this, emoji, fromRecent, variants);
    }

    public boolean isShowingVariants() {
        return showVariants;
    }

    public boolean isFromRecent() {
        return fromRecent;
    }

    public void setShowVariants(boolean showVariants) {
        this.showVariants = showVariants;
    }
}
