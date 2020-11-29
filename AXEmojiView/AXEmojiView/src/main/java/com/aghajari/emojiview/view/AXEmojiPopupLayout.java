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
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.aghajari.emojiview.listener.PopupListener;
import com.aghajari.emojiview.utils.Utils;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class AXEmojiPopupLayout extends FrameLayout implements AXPopupInterface {


    AXEmojiPopupView popupView;

    protected KeyboardHeightProvider heightProvider = null;

    public AXEmojiPopupLayout(Context context) {
        this(context, null);
    }

    public AXEmojiPopupLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AXEmojiPopupLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initKeyboardHeightProvider();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //this.setOrientation(LinearLayout.VERTICAL);
    }

    public void initPopupView(AXEmojiBase content) {
        popupView = new AXEmojiPopupView(this, content);
        popupView.setFocusableInTouchMode(true);
        popupView.setFocusable(true);
        popupView.requestFocus();

        initKeyboardHeightProvider();
    }

    protected void initKeyboardHeightProvider() {
        if (heightProvider == null) heightProvider = new KeyboardHeightProvider(this);
        if (popupView != null) {
            popupView.post(new Runnable() {
                @Override
                public void run() {
                    heightProvider.start();
                }
            });
        }
    }

    public boolean onBackPressed() {
        if (popupView != null && popupView.isShowing) {
            popupView.dismiss();
            return true;
        }
        return false;
    }

    public boolean isKeyboardOpen() {
        return ((popupView != null && popupView.isKeyboardOpen) ? true : false);
    }

    public void removePopupView() {
        if (popupView != null) popupView.remove();
    }

    public void hidePopupView() {
        if (popupView != null) popupView.onlyHide();
    }

    public int getPopupHeight() {
        return ((popupView != null) ? popupView.getPopupHeight() : 0);
    }


    @Override
    public void toggle() {
        if (popupView != null) popupView.toggle();
    }

    @Override
    public void show() {
        if (popupView != null) popupView.show();
    }

    @Override
    public void dismiss() {
        if (popupView != null) popupView.dismiss();
    }

    @Override
    public boolean isShowing() {
        if (popupView != null) return popupView.isShowing();
        return false;
    }

    public void setPopupListener(PopupListener listener) {
        if (popupView != null) popupView.setPopupListener(listener);
    }

    public void hideAndOpenKeyboard() {
        popupView.dismiss();
        popupView.editText.setFocusable(true);
        popupView.editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) popupView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(popupView.editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void openKeyboard() {
        popupView.editText.setFocusable(true);
        popupView.editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) popupView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(popupView.editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void updateKeyboardStateOpened(int height) {
        if (popupView != null) popupView.updateKeyboardStateOpened(height);
    }

    public void updateKeyboardStateClosed() {
        if (popupView != null) popupView.updateKeyboardStateClosed();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (heightProvider != null) {
            heightProvider.stickOnStart();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (heightProvider != null) heightProvider.close();
    }

    /**
     * The keyboard height provider, this class uses a PopupWindow
     * to calculate the window height when the floating keyboard is opened and closed.
     */
    class KeyboardHeightProvider extends PopupWindow {

        /**
         * The view that is used to calculate the keyboard height
         */
        private View popupView;

        /**
         * The parent view
         */
        private View parentView;

        private AXEmojiPopupLayout layout;

        /**
         * Construct a new KeyboardHeightProvider
         */
        public KeyboardHeightProvider(AXEmojiPopupLayout popupLayout) {
            super(popupLayout.getContext());

            this.popupView = new View(popupLayout.getContext());
            this.layout = popupLayout;
            setContentView(popupView);

            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

            parentView = Utils.asActivity(popupLayout.getContext()).findViewById(android.R.id.content);

            setWidth(0);
            setHeight(WindowManager.LayoutParams.MATCH_PARENT);

            popupView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    if (popupView != null) {
                        handleOnGlobalLayout();
                    }
                }
            });
        }

        /**
         * Start the KeyboardHeightProvider, this must be called after the onResume of the Activity.
         * PopupWindows are not allowed to be registered before the onResume has finished
         * of the Activity.
         */
        public void start() {
            if (!isShowing() && parentView.getWindowToken() != null) {
                setBackgroundDrawable(new ColorDrawable(0));
                showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
            }
        }

        public void stickOnStart() {
            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (!isShowing() && parentView.getWindowToken() != null) {
                        setBackgroundDrawable(new ColorDrawable(0));
                        showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
                        return;
                    }
                    if (!isShowing()) {
                        handler.post(this);
                    }
                }
            });
        }

        /**
         * Close the keyboard height provider,
         * this provider will not be used anymore.
         */
        public void close() {
            dismiss();
        }

        /**
         * Popup window itself is as big as the window of the Activity.
         * The keyboard can then be calculated by extracting the popup view bottom
         * from the activity window height.
         */
        private void handleOnGlobalLayout() {
            if (layout.popupView == null && layout.popupView.getVisibility() != GONE) return;
            final int keyboardHeight = Utils.getInputMethodHeight(popupView.getContext(), layout.popupView.rootView);
            layout.popupView.updateKeyboardState(keyboardHeight);
        }
    }
}
