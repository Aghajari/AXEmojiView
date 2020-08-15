package com.aghajari.sample.emojiview.customs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aghajari.emojiview.view.AXEmojiEditText;
import com.aghajari.emojiview.view.AXEmojiPopupLayout;

// Based on : https://stackoverflow.com/a/28719420
public class CustomEditText extends AXEmojiEditText {
    public CustomEditText(@NonNull Context context) {
        super(context);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private AXEmojiPopupLayout layout;

    public void setEmojiLayout(AXEmojiPopupLayout layout) {
        this.layout = layout;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (layout.isShowing()) {
                mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
                layout.dismiss();
                return true;
            }
        }
        return super.onKeyPreIme(keyCode,event);
    }

}
