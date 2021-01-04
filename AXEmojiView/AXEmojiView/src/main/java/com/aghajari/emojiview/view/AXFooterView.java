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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.AXEmojiUtils;
import com.aghajari.emojiview.R;
import com.aghajari.emojiview.adapters.AXFooterIconsAdapter;
import com.aghajari.emojiview.utils.Utils;

class AXFooterView extends AXEmojiLayout {
    public AXFooterView(Context context, AXEmojiPager pager, int Left) {
        super(context);

        this.Left = Left;
        this.pager = pager;
        init();
    }

    int Left;
    AXEmojiPager pager;
    RecyclerView icons;
    AppCompatImageView backSpace;
    AppCompatImageView leftIcon;


    private boolean backspacePressed;
    private boolean backspaceOnce;
    boolean backspaceEnabled = true;

    private void init() {
        int iconSize = Utils.dpToPx(getContext(), 24);
        backSpace = new AppCompatImageView(getContext()) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                if (!backspaceEnabled) return super.onTouchEvent(event);

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    backspacePressed = true;
                    backspaceOnce = false;
                    postBackspaceRunnable(350);
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
                    backspacePressed = false;
                    if (!backspaceOnce) {
                        AXEmojiUtils.backspace(editText);
                        backSpace.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);

                    }
                }
                super.onTouchEvent(event);
                return true;
            }
        };

        this.addView(backSpace, new LayoutParams(
                getContext().getResources().getDisplayMetrics().widthPixels - Utils.dpToPx(getContext(), 38), Utils.dpToPx(getContext(), 10), iconSize, iconSize));

        Drawable back = ContextCompat.getDrawable(getContext(), R.drawable.emoji_backspace);
        DrawableCompat.setTint(DrawableCompat.wrap(back), AXEmojiManager.getEmojiViewTheme().getFooterItemColor());
        backSpace.setImageDrawable(back);
        Utils.setClickEffect(backSpace, true);

        backSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (pager.getEditText()!=null) AXEmojiUtils.backspace(pager.getEditText());
                if (pager.listener != null) pager.listener.onClick(view, false);
            }
        });

        if (Left != -1) {
            leftIcon = new AppCompatImageView(getContext());
            this.addView(leftIcon, new LayoutParams(Utils.dpToPx(getContext(), 8), Utils.dpToPx(getContext(), 10), iconSize, iconSize));

            Drawable leftIconDr = AppCompatResources.getDrawable(getContext(), Left);
            DrawableCompat.setTint(DrawableCompat.wrap(leftIconDr), AXEmojiManager.getEmojiViewTheme().getFooterItemColor());
            leftIcon.setImageDrawable(leftIconDr);
            Utils.setClickEffect(leftIcon, true);

            leftIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pager.listener != null) pager.listener.onClick(view, true);
                }
            });
        }

        icons = new RecyclerView(getContext());
        this.addView(icons, new AXEmojiLayout.LayoutParams(
                Utils.dpToPx(getContext(), 44), 0, getContext().getResources().getDisplayMetrics().widthPixels - Utils.dpToPx(getContext(), 88), -1));

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        icons.setLayoutManager(lm);
        Utils.forceLTR(icons);

        icons.setItemAnimator(null);

        icons.setAdapter(new AXFooterIconsAdapter(pager));

        icons.setOverScrollMode(View.OVER_SCROLL_NEVER);
        if (icons.getLayoutParams().width > pager.getPagesCount() * Utils.dpToPx(getContext(), 40)) {
            icons.getLayoutParams().width = pager.getPagesCount() * Utils.dpToPx(getContext(), 40);
            ((LayoutParams) icons.getLayoutParams()).leftMargin = 0;
            ((LayoutParams) icons.getLayoutParams()).gravity = Gravity.CENTER_HORIZONTAL;
            this.requestLayout();
        }

        this.setBackgroundColor(AXEmojiManager.getEmojiViewTheme().getFooterBackgroundColor());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setElevation(Utils.dpToPx(getContext(), 2));
        }

        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void setPageIndex(int index) {
        icons.getAdapter().notifyDataSetChanged();
    }

    private void postBackspaceRunnable(final int time) {
        backSpace.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!backspaceEnabled || !backspacePressed) return;

                AXEmojiUtils.backspace(editText);
                backSpace.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);

                backspaceOnce = true;
                postBackspaceRunnable(Math.max(50, time - 100));
            }
        }, time);
    }
}
