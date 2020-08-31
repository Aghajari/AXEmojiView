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


package com.aghajari.emojiview.variant;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.R;
import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.emoji.EmojiData;
import com.aghajari.emojiview.listener.OnEmojiActions;
import com.aghajari.emojiview.utils.Utils;
import com.aghajari.emojiview.view.AXEmojiImageView;

import java.lang.reflect.Field;

public class AXTouchEmojiVariantPopup extends AXEmojiVariantPopup {

    @NonNull
    final View rootView;
    @Nullable
    private PopupWindow popupWindow;

    @Nullable
    final OnEmojiActions listener;
    @Nullable
    AXEmojiImageView rootImageView;
    EmojiColorPickerView pickerView;
    int popupWidth, popupHeight;
    boolean fromRecent;

    public AXTouchEmojiVariantPopup(@NonNull final View rootView, @Nullable final OnEmojiActions listener) {
        super(rootView, listener);
        this.rootView = rootView;
        this.listener = listener;
        init();
    }

    private void init() {
        mEmojiSize = Utils.dp(rootView.getContext(), 34);

        pickerView = new EmojiColorPickerView(rootView.getContext());
        popupWindow = new EmojiPopupWindow(pickerView, popupWidth = Utils.dp(rootView.getContext(), (34 * 6 + 10 + 4 * 5) + 2), popupHeight = Utils.dp(rootView.getContext(), 58));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setClippingEnabled(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
        popupWindow.getContentView().setFocusableInTouchMode(true);
        popupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_UP && popupWindow != null && popupWindow.isShowing()) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    boolean isShowing;

    public boolean isShowing() {
        return isShowing;
    }

    int mEmojiSize;

    public void show(@NonNull final AXEmojiImageView clickedImage, @NonNull final Emoji emoji, boolean fromRecent) {
        dismiss();
        this.fromRecent = fromRecent;
        if (popupWindow == null || pickerView == null) init();

        isShowing = true;
        rootImageView = clickedImage;

        emojiTouchedX = emojiLastX;
        emojiTouchedY = emojiLastY;

        String color = EmojiData.getEmojiColor(emoji.getUnicode());

        if (color != null) {
            switch (color) {
                case "\uD83C\uDFFB":
                    pickerView.setSelection(1);
                    break;
                case "\uD83C\uDFFC":
                    pickerView.setSelection(2);
                    break;
                case "\uD83C\uDFFD":
                    pickerView.setSelection(3);
                    break;
                case "\uD83C\uDFFE":
                    pickerView.setSelection(4);
                    break;
                case "\uD83C\uDFFF":
                    pickerView.setSelection(5);
                    break;
            }
        } else {
            pickerView.setSelection(0);
        }
        final int[] location = new int[2];
        rootImageView.getLocationOnScreen(location);

        popupWindow.setFocusable(true);

        final Point loc;

        if (AXEmojiManager.isUsingPopupWindow()) {
            loc = new Point(location[0] - popupWidth / 2 + clickedImage.getWidth() / 2, location[1] - popupHeight + (rootImageView.getMeasuredHeight() - mEmojiSize) / 2);

            pickerView.setEmoji(emoji.getBase(), -100);
            popupWindow.showAtLocation(rootView, Gravity.NO_GRAVITY, loc.x, loc.y);

        } else {
            loc = null;
            int x = mEmojiSize * pickerView.getSelection() + Utils.dp(rootImageView.getContext(), 4 * pickerView.getSelection() - 1);
            if (location[0] - x < Utils.dp(rootImageView.getContext(), 5)) {
                x += (location[0] - x) - Utils.dp(rootImageView.getContext(), 5);
            } else if (location[0] - x + popupWidth > rootImageView.getContext().getResources().getDisplayMetrics().widthPixels - Utils.dp(rootImageView.getContext(), 5)) {
                x += (location[0] - x + popupWidth) - (rootImageView.getContext().getResources().getDisplayMetrics().widthPixels - Utils.dp(rootImageView.getContext(), 5));
            }
            int xOffset = -x;
            int yOffset = rootImageView.getTop() < 0 ? rootImageView.getTop() : 0;

            pickerView.setEmoji(emoji.getBase(), Utils.dp(rootImageView.getContext(), 22) - xOffset + (int) Utils.dpf2(rootImageView.getContext(), 0.5f));

            popupWindow.showAsDropDown(rootImageView, xOffset, -rootImageView.getMeasuredHeight() - popupHeight + (rootImageView.getMeasuredHeight() - mEmojiSize) / 2 - yOffset);
        }

        clickedImage.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
        clickedImage.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, location[0], location[1], 0));

        if (AXEmojiManager.isUsingPopupWindow()) {
            Utils.fixPopupLocation(popupWindow, loc);
            popupWindow.getContentView().post(new Runnable() {
                @Override
                public void run() {
                    if (popupWindow.getContentView().getMeasuredHeight() <= 0) {
                        popupWindow.getContentView().post(this);
                        return;
                    }
                    Point wLoc = Utils.locationOnScreen(popupWindow.getContentView());
                    pickerView.setEmoji(emoji.getBase(), (location[0] - wLoc.x) + (clickedImage.getWidth() / 2));
                }
            });
        }
    }

    public void dismiss() {
        isShowing = false;
        rootImageView = null;

        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    private class EmojiColorPickerView extends View {

        private Drawable backgroundDrawable;
        private Drawable arrowDrawable;
        private Emoji currentEmoji;
        private int arrowX;
        private int selection;
        private Paint rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private RectF rect = new RectF();
        private boolean loaded = false;

        public void setEmoji(Emoji emoji, int arrowPosition) {
            currentEmoji = emoji;
            arrowX = arrowPosition;
            rectPaint.setColor(0x2f000000);
            invalidate();
        }

        public Emoji getEmoji() {
            return currentEmoji;
        }

        public void setSelection(int position) {
            if (selection == position) {
                return;
            }
            selection = position;
            invalidate();
        }

        public int getSelection() {
            return selection;
        }

        public EmojiColorPickerView(Context context) {
            super(context);

            backgroundDrawable = getResources().getDrawable(R.drawable.stickers_back_all);
            arrowDrawable = getResources().getDrawable(R.drawable.stickers_back_arrow);
            setDrawableColor(backgroundDrawable, AXEmojiManager.getEmojiViewTheme().getVariantPopupBackgroundColor());
            setDrawableColor(arrowDrawable, AXEmojiManager.getEmojiViewTheme().getVariantPopupBackgroundColor());
        }

        void setDrawableColor(Drawable drawable, int color) {
            if (drawable == null) return;
            drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            loaded = true;
            backgroundDrawable.setBounds(0, 0, getMeasuredWidth(), Utils.dp(getContext(), 54));
            backgroundDrawable.draw(canvas);

            arrowDrawable.setBounds(arrowX - Utils.dp(getContext(), 9), Utils.dp(getContext(), 49.5f), arrowX + Utils.dp(getContext(), 9), Utils.dp(getContext(), 47.5f + 8));
            arrowDrawable.draw(canvas);

            if (currentEmoji != null) {
                for (int a = 0; a < 6; a++) {
                    int x = mEmojiSize * a + Utils.dp(getContext(), 5 + 4 * a);
                    int y = Utils.dp(getContext(), 9);
                    if (selection == a) {
                        rect.set(x, y - (int) Utils.dpf2(getContext(), 3.5f), x + mEmojiSize, y + mEmojiSize + Utils.dp(getContext(), 3));
                        canvas.drawRoundRect(rect, Utils.dp(getContext(), 4), Utils.dp(getContext(), 4), rectPaint);
                    }

                    Emoji sEmoji;
                    if (a == 0) {
                        sEmoji = currentEmoji.getBase();
                    } else {
                        sEmoji = currentEmoji.getVariants().get(a - 1);
                    }
                    Drawable drawable = sEmoji.getDrawable(getContext());

                    if (drawable != null) {
                        drawable.setBounds(x, y, x + mEmojiSize, y + mEmojiSize);
                        drawable.draw(canvas);
                    }

                    if (loaded) {
                        if (sEmoji.isLoading()) loaded = false;
                    }
                }
            }

            if (!loaded) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                }, 10);
            }
        }
    }

    private static final Field superListenerField;

    static {
        Field f = null;
        try {
            f = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) {
            /* ignored */
        }
        superListenerField = f;
    }

    private static final ViewTreeObserver.OnScrollChangedListener NOP = new ViewTreeObserver.OnScrollChangedListener() {
        @Override
        public void onScrollChanged() {
            /* do nothing */
        }
    };


    private class EmojiPopupWindow extends PopupWindow {

        private ViewTreeObserver.OnScrollChangedListener mSuperScrollListener;
        private ViewTreeObserver mViewTreeObserver;

        public EmojiPopupWindow() {
            super();
            init();
        }

        public EmojiPopupWindow(Context context) {
            super(context);
            init();
        }

        public EmojiPopupWindow(int width, int height) {
            super(width, height);
            init();
        }

        public EmojiPopupWindow(View contentView) {
            super(contentView);
            init();
        }

        public EmojiPopupWindow(View contentView, int width, int height, boolean focusable) {
            super(contentView, width, height, focusable);
            init();
        }

        public EmojiPopupWindow(View contentView, int width, int height) {
            super(contentView, width, height);
            init();
        }

        private void init() {
            if (superListenerField != null) {
                try {
                    mSuperScrollListener = (ViewTreeObserver.OnScrollChangedListener) superListenerField.get(this);
                    superListenerField.set(this, NOP);
                } catch (Exception e) {
                    mSuperScrollListener = null;
                }
            }
        }

        private void unregisterListener() {
            if (mSuperScrollListener != null && mViewTreeObserver != null) {
                if (mViewTreeObserver.isAlive()) {
                    mViewTreeObserver.removeOnScrollChangedListener(mSuperScrollListener);
                }
                mViewTreeObserver = null;
            }
        }

        private void registerListener(View anchor) {
            if (mSuperScrollListener != null) {
                ViewTreeObserver vto = (anchor.getWindowToken() != null) ? anchor.getViewTreeObserver() : null;
                if (vto != mViewTreeObserver) {
                    if (mViewTreeObserver != null && mViewTreeObserver.isAlive()) {
                        mViewTreeObserver.removeOnScrollChangedListener(mSuperScrollListener);
                    }
                    if ((mViewTreeObserver = vto) != null) {
                        vto.addOnScrollChangedListener(mSuperScrollListener);
                    }
                }
            }
        }

        @Override
        public void showAsDropDown(View anchor, int xoff, int yoff) {
            try {
                super.showAsDropDown(anchor, xoff, yoff);
                registerListener(anchor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void update(View anchor, int xoff, int yoff, int width, int height) {
            super.update(anchor, xoff, yoff, width, height);
            registerListener(anchor);
        }

        @Override
        public void update(View anchor, int width, int height) {
            super.update(anchor, width, height);
            registerListener(anchor);
        }

        @Override
        public void showAtLocation(View parent, int gravity, int x, int y) {
            super.showAtLocation(parent, gravity, x, y);
            unregisterListener();
        }

        @Override
        public void dismiss() {
            setFocusable(false);
            try {
                super.dismiss();
            } catch (Exception ignore) {

            }
            unregisterListener();
        }
    }

    float emojiTouchedX, emojiTouchedY;
    float emojiLastX, emojiLastY;

    private int location[] = new int[2];

    @Override
    public boolean onTouch(MotionEvent event, RecyclerView recyclerView) {
        if (rootImageView != null) {
            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    Emoji emoji;
                    if (pickerView.getSelection() == 0) {
                        emoji = pickerView.getEmoji().getBase();
                    } else {
                        emoji = pickerView.getEmoji().getVariants().get(pickerView.getSelection() - 1);
                    }
                    rootImageView.updateEmoji(emoji);
                    if (listener != null) listener.onClick(rootImageView, emoji, fromRecent, true);
                }
                rootImageView = null;
                emojiTouchedX = -10000;
                emojiTouchedY = -10000;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                boolean ignore = false;
                if (emojiTouchedX != -10000) {
                    if (Math.abs(emojiTouchedX - event.getX()) > Utils.getPixelsInCM(rootImageView.getContext(), 0.2f, true) || Math.abs(emojiTouchedY - event.getY()) > Utils.getPixelsInCM(rootImageView.getContext(), 0.2f, false)) {
                        emojiTouchedX = -10000;
                        emojiTouchedY = -10000;
                    } else {
                        ignore = true;
                    }
                }
                if (!ignore) {
                    recyclerView.getLocationOnScreen(location);
                    float x = location[0] + event.getX();
                    pickerView.getLocationOnScreen(location);
                    x -= location[0] + Utils.dp(rootImageView.getContext(), 3);
                    int position = (int) (x / (mEmojiSize + Utils.dp(rootImageView.getContext(), 4)));
                    if (position < 0) {
                        position = 0;
                    } else if (position > 5) {
                        position = 5;
                    }
                    pickerView.setSelection(position);
                }
            }
            return true;
        }
        emojiLastX = event.getX();
        emojiLastY = event.getY();
        return super.onTouch(event, recyclerView);
    }
}
