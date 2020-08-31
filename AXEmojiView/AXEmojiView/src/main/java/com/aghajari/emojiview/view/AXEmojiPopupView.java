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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.autofill.AutofillManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.listener.PopupListener;
import com.aghajari.emojiview.utils.Utils;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.O;

class AXEmojiPopupView extends FrameLayout implements AXPopupInterface {
    static final int MIN_KEYBOARD_HEIGHT = 50;

    final View rootView;
    final Activity context;

    AXEmojiBase content;
    final EditText editText;

    boolean isPendingOpen;
    boolean isShowing = false;
    boolean isKeyboardOpen;

    int keyboardHeight = 0;
    int originalImeOptions = -1;

    public boolean isKeyboardOpen() {
        return isKeyboardOpen;
    }

    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            final int keyboardHeight = Utils.getInputMethodHeight(context, rootView);
            if (keyboardHeight > Utils.dpToPx(context, MIN_KEYBOARD_HEIGHT)) {
                updateKeyboardStateOpened(keyboardHeight);
                if (listener != null) listener.onKeyboardOpened(keyboardHeight);
            } else {
                updateKeyboardStateClosed();
                if (listener != null) listener.onKeyboardClosed();
            }
        }
    };


    PopupListener listener = null;

    public void setPopupListener(PopupListener listener) {
        this.listener = listener;
    }

    ViewGroup ap;
    FrameLayout.LayoutParams lp;

    AXEmojiPopupView(ViewGroup view, final AXEmojiBase content) {
        super(content.getContext());
        popupWindowHeight = Utils.getKeyboardHeight(content.getContext(), 0);
        lp = new FrameLayout.LayoutParams(-1, popupWindowHeight);
        lp.gravity = Gravity.BOTTOM;

        this.ap = view;
        this.context = Utils.asActivity(content.getContext());
        this.rootView = content.getEditText().getRootView();
        this.editText = content.getEditText();
        this.content = content;

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);

        this.addView(content, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        if (content instanceof AXEmojiPager) {
            if (!((AXEmojiPager) content).isShowing) ((AXEmojiPager) content).show();
        }

        content.setBackgroundColor(AXEmojiManager.getEmojiViewTheme().getBackgroundColor());
        this.invalidate();
    }

    private void showAtBottomPending() {
        isPendingOpen = true;
        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (Utils.shouldOverrideRegularCondition(context, editText)) {
            editText.setImeOptions(editText.getImeOptions() | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
            if (inputMethodManager != null) {
                inputMethodManager.restartInput(editText);
            }
        }

        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(editText, InputMethodManager.RESULT_UNCHANGED_SHOWN, new ResultReceiver(new Handler(Looper.getMainLooper())) {
                @Override
                public void onReceiveResult(final int resultCode, final Bundle data) {
                }
            });
        }
    }

    void updateKeyboardStateOpened(final int keyboardHeight) {
        isKeyboardOpen = true;
        if (this.keyboardHeight <= 0 || needToOpen) {
            this.keyboardHeight = keyboardHeight;
            popupWindowHeight = keyboardHeight;
            Utils.updateKeyboardHeight(context, keyboardHeight);
        }
        if (listener != null) listener.onKeyboardOpened(keyboardHeight);

        lp.height = popupWindowHeight;

        if (needToOpen) {
            this.setLayoutParams(lp);
            needToOpen = false;
            Utils.hideKeyboard(ap);

            /**if (isKeyboardOpen){
             //disable anim
             show();
             }else{
             show();
             }*/
            show();

        } else {
            //disable anim
            //dismiss();
        }
    }

    void updateKeyboardStateClosed() {
        isKeyboardOpen = false;

        if (listener != null) {
            listener.onKeyboardClosed();
        }

        /**if (isShowing()) {
         dismiss();
         }*/
    }

    int popupWindowHeight = 0;
    boolean needToOpen = false;

    public void toggle() {
        AXEmojiManager.setUsingPopupWindow(false);
        //if (isAnimRunning) return;
        if (!isShowing) {
            if (this.getParent() != null) {
                ap.removeView(this);
            }
            // this is needed because something might have cleared the insets listener
            if (popupWindowHeight > 0) {
                needToOpen = false;
                lp.height = popupWindowHeight;
                ap.addView(this, lp);
                show();
            } else {
                needToOpen = true;
                ap.addView(this, lp);

                if (Utils.shouldOverrideRegularCondition(context, editText) && originalImeOptions == -1) {
                    originalImeOptions = editText.getImeOptions();
                }

                editText.setFocusableInTouchMode(true);
                editText.requestFocus();

                showAtBottomPending();
            }
        } else {
            dismiss();
        }
    }

    public void show() {
        AXEmojiManager.setUsingPopupWindow(false);
        //if (isAnimRunning) return;
        if (popupWindowHeight == 0) {
            needToOpen = true;
            if (this.getParent() != null) {
                ap.removeView(this);
            }
            ap.addView(this, lp);

            if (Utils.shouldOverrideRegularCondition(context, editText) && originalImeOptions == -1) {
                originalImeOptions = editText.getImeOptions();
            }

            editText.setFocusableInTouchMode(true);
            editText.requestFocus();

            showAtBottomPending();
            return;
        }
        if (getParent() == null) {
            needToOpen = false;
            lp.height = popupWindowHeight;
            ap.addView(this, lp);
            content.refresh();
        }
        isShowing = true;

        Utils.hideKeyboard(ap);
        showAtBottom();
    }


    public boolean isShowing() {
        return isShowing;
    }

    public void dismiss() {
        //if (isAnimRunning) return;
        anim(false);

        isShowing = false;
        content.dismiss();
        if (listener != null) listener.onDismiss();

        if (originalImeOptions != -1) {
            editText.setImeOptions(originalImeOptions);
            final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputMethodManager != null) {
                inputMethodManager.restartInput(editText);
            }

            if (SDK_INT >= O) {
                final AutofillManager autofillManager = context.getSystemService(AutofillManager.class);
                if (autofillManager != null) {
                    autofillManager.cancel();
                }
            }
        }
    }

    boolean ignoreCall = false;

    public void onlyHide() {
        ignoreCall = true;
        anim(false);

        isShowing = false;
        content.dismiss();
        if (listener != null) listener.onDismiss();
    }

    void showAtBottom() {
        isPendingOpen = false;

        anim(true);
        if (listener != null) listener.onShow();
    }


    void anim(final boolean e) {
        if (e) {
            lp.height = popupWindowHeight;
            this.setLayoutParams(lp);
        } else {
            remove();
        }
    }


    public int getPopupHeight() {
        return lp.height;
    }

    public void remove() {
        content.dismiss();
        //this.isAnimRunning = false;
        this.isShowing = false;
        lp.height = 0;
        if (this.getParent() != null) ap.removeView(this);
        if (listener != null) listener.onDismiss();
    }


}
