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
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import com.aghajari.emojiview.listener.PopupListener;
import com.aghajari.emojiview.search.AXEmojiSearchView;
import com.aghajari.emojiview.utils.Utils;


public class AXEmojiPopupLayout extends FrameLayout implements AXPopupInterface {


    AXEmojiPopupView popupView;
    private View keyboard;
    protected boolean changeHeightWithKeyboard = true;
    protected KeyboardHeightProvider heightProvider = null;

    public AXEmojiPopupLayout(Context context) {
        this(context, null);
    }

    public AXEmojiPopupLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AXEmojiPopupLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Utils.forceLTR(this);
        initKeyboardHeightProvider();
    }

    public void initPopupView(AXEmojiBase content) {
        if (keyboard == null) {
            keyboard = new View(getContext());
            this.addView(keyboard,new FrameLayout.LayoutParams(-1,0));
        }
        content.setPopupInterface(this);
        popupView = new AXEmojiPopupView(this, content);
        popupView.setFocusableInTouchMode(true);
        popupView.setFocusable(true);
        popupView.requestFocus();

        initKeyboardHeightProvider();
    }

    protected void initKeyboardHeightProvider() {
        if (heightProvider == null) heightProvider = new KeyboardHeightProvider(this);
        if (popupView != null) {
            popupView.post(() -> heightProvider.start());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (popupView!=null && popupView.listener!=null)
            popupView.listener.onViewHeightChanged(h);
    }

    public boolean onBackPressed() {
        if (popupView == null) return false;
        return popupView.onBackPressed();
    }

    @Override
    public void reload() {
        if (popupView!=null) popupView.reload();
    }

    public boolean isKeyboardOpen() {
        return (popupView != null && popupView.isKeyboardOpen);
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
        return popupView != null && popupView.isShowing;
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
        popupView.hideSearchView(true);
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

    public void setMaxHeight(int maxHeight) {
        if (popupView!=null) popupView.setMaxHeight(maxHeight);
    }

    public int getMaxHeight() {
        return popupView!=null ? popupView.getMaxHeight() : -1;
    }

    public void setMinHeight(int minHeight) {
        if (popupView!=null) popupView.setMinHeight(minHeight);
    }

    public int getMinHeight() {
        return popupView!=null ? popupView.getMinHeight() : -1;
    }

    public void setPopupAnimationEnabled(boolean enabled){
        if (popupView!=null) popupView.animationEnabled = enabled;
    }

    public boolean isPopupAnimationEnabled(){
        return popupView == null || popupView.animationEnabled;
    }

    public void setPopupAnimationDuration(long duration){
        if (popupView!=null) popupView.animationDuration = duration;
    }

    public long getPopupAnimationDuration(){
        return popupView!=null ? popupView.animationDuration : 250;
    }

    public AXEmojiSearchView getSearchView() {
        return popupView.getSearchView();
    }

    public void setSearchView(AXEmojiSearchView searchView) {
        popupView.setSearchView(searchView);
    }

    public void hideSearchView(){
        popupView.hideSearchView(true);
    }

    public void showSearchView(){
        popupView.showSearchView();
    }

    public boolean isShowingSearchView(){
        return popupView!=null && popupView.isShowingSearchView();
    }

    public void setSearchViewAnimationEnabled(boolean enabled){
        popupView.searchViewAnimationEnabled = enabled;
    }

    public boolean isSearchViewAnimationEnabled(){
        return popupView == null || popupView.searchViewAnimationEnabled;
    }

    public void setSearchViewAnimationDuration(long duration){
        if (popupView!=null) popupView.searchViewAnimationDuration = duration;
    }

    public long getSearchViewAnimationDuration(){
        return popupView!=null ? popupView.searchViewAnimationDuration : 250;
    }

    /**
     * The keyboard height provider, this class uses a PopupWindow
     * to calculate the window height when the floating keyboard is opened and closed.
     */
    protected static class KeyboardHeightProvider extends PopupWindow {

        /**
         * The view that is used to calculate the keyboard height
         */
        private final View popupView;

        /**
         * The parent view
         */
        private final View parentView;

        private final AXEmojiPopupLayout layout;

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

            popupView.getViewTreeObserver().addOnGlobalLayoutListener(this::handleOnGlobalLayout);
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
            if (layout.popupView == null || layout.popupView.getVisibility() == GONE) return;
            final int keyboardHeight = Utils.getInputMethodHeight(popupView.getContext(), layout.popupView.rootView);
            layout.popupView.updateKeyboardState(keyboardHeight);
            if (layout.changeHeightWithKeyboard) {
                layout.keyboard.getLayoutParams().height = keyboardHeight;
                layout.requestLayout();
            }
        }
    }
}
