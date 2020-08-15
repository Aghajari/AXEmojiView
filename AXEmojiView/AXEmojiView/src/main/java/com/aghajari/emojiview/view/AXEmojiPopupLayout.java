package com.aghajari.emojiview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import com.aghajari.emojiview.listener.PopupListener;

public class AXEmojiPopupLayout extends FrameLayout implements AXPopupInterface {


    public AXEmojiPopupLayout(Context context) {
        super(context);
    }

    public AXEmojiPopupLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AXEmojiPopupLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //this.setOrientation(LinearLayout.VERTICAL);
    }

    AXEmojiPopupView popupView;

    public void initPopupView(AXEmojiBase content){
        popupView = new AXEmojiPopupView(this,content);
        popupView.setFocusableInTouchMode(true);
        popupView.setFocusable(true);
        popupView.requestFocus();
    }

    public boolean onBackPressed(){
        if (popupView!=null && popupView.isShowing){
            popupView.dismiss();
            return true;
        }
        return false;
    }

    public boolean isKeyboardOpen() {
        return ((popupView!=null && popupView.isKeyboardOpen) ? true: false);
    }

    public void removePopupView() {
        if (popupView!=null) popupView.remove();
    }

    public void hidePopupView() {
        if (popupView!=null) popupView.onlyHide();
    }


    public int getPopupHeight() {
        return ((popupView!=null)?popupView.getPopupHeight():0);
    }


    @Override
    public void toggle() {
        if (popupView!=null) popupView.toggle();
    }

    @Override
    public void show() {
        if (popupView!=null) popupView.show();
    }

    @Override
    public void dismiss() {
        if (popupView!=null) popupView.dismiss();
    }

    @Override
    public boolean isShowing() {
        if (popupView!=null) return popupView.isShowing();
        return false;
    }

    public void setPopupListener(PopupListener listener){
        if (popupView!=null) popupView.setPopupListener(listener);
    }

    public void hideAndOpenKeyboard(){
        popupView.dismiss();
        popupView.editText.setFocusable(true);
        popupView.editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) popupView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(popupView.editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void openKeyboard(){
        popupView.editText.setFocusable(true);
        popupView.editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) popupView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(popupView.editText, InputMethodManager.SHOW_IMPLICIT);
    }
}
