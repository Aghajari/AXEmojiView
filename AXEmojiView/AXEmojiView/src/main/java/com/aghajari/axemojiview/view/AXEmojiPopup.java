package com.aghajari.axemojiview.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.autofill.AutofillManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.listener.PopupListener;
import com.aghajari.axemojiview.utils.EmojiResultReceiver;
import com.aghajari.axemojiview.utils.Utils;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;

public final class AXEmojiPopup implements EmojiResultReceiver.Receiver {
    static final int MIN_KEYBOARD_HEIGHT = 50;

    final View rootView;
    final Activity context;


    final public PopupWindow popupWindow;
    AXEmojiBase content;
    final EditText editText;

    public boolean isPendingOpen;
    public boolean isKeyboardOpen;

    int originalImeOptions = -1;

    final EmojiResultReceiver emojiResultReceiver = new EmojiResultReceiver(new Handler(Looper.getMainLooper()));

    final ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override @SuppressWarnings("PMD.CyclomaticComplexity") public void onGlobalLayout() {
            updateKeyboardState();
        }
    };

    PopupListener listener= null;

    public void setPopupListener(PopupListener listener){
        this.listener = listener;
    }

  public AXEmojiPopup(final AXEmojiBase content) {
        this.context = Utils.asActivity(content.getContext());
        this.rootView = content.getEditText().getRootView();
        this.editText = content.getEditText();
        this.content = content;

        popupWindow = new PopupWindow(context);

        popupWindow.setContentView(content);
      popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
      popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null)); // To avoid borders and overdraw.
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {
                if (listener!=null) listener.onDismiss();
            }
        });

        if (content instanceof AXEmojiPager){
            if (!((AXEmojiPager) content).isShowing) ((AXEmojiPager) content).show();
        }

      content.setBackgroundColor(AXEmojiManager.getEmojiViewTheme().getBackgroundColor());

      rootView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public PopupWindow getPopupWindow(){
        return popupWindow;
    }

    void updateKeyboardState() {
        final int keyboardHeight = Utils.getInputMethodHeight(context, rootView);

        if (keyboardHeight > Utils.dpToPx(context, MIN_KEYBOARD_HEIGHT)) {
            updateKeyboardStateOpened(keyboardHeight);
        } else {
            updateKeyboardStateClosed();
        }
    }

    private void updateKeyboardStateOpened(final int keyboardHeight) {
        if (popupWindow.getHeight() != keyboardHeight) {
            popupWindow.setHeight(keyboardHeight);
        }

        final Rect rect = Utils.windowVisibleDisplayFrame(context);

        final int properWidth = Utils.getOrientation(context) == Configuration.ORIENTATION_PORTRAIT ? rect.right : Utils.getScreenWidth(context);
        if (popupWindow.getWidth() != properWidth) {
            popupWindow.setWidth(properWidth);
        }

        if (!isKeyboardOpen) {
            isKeyboardOpen = true;
            if (!isKeyboardOpen &&listener!=null) listener.onKeyboardOpened(keyboardHeight);
        }

        if (isPendingOpen) {
            showAtBottom();
        }
    }

    private void updateKeyboardStateClosed() {
        isKeyboardOpen = false;

        if (!isKeyboardOpen &&listener!=null) listener.onKeyboardClosed();

        if (isShowing()) {
            dismiss();
        }
    }

    public void toggle() {
        if (!popupWindow.isShowing()) {
            content.refresh();

            if (Utils.shouldOverrideRegularCondition(context, editText) && originalImeOptions == -1) {
                originalImeOptions = editText.getImeOptions();
            }
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            showAtBottomPending();
        } else {
            dismiss();
        }

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
            emojiResultReceiver.setReceiver(this);
            inputMethodManager.showSoftInput(editText, InputMethodManager.RESULT_UNCHANGED_SHOWN, emojiResultReceiver);
        }
    }

    public boolean isShowing() {
        return popupWindow.isShowing();
    }

    public void dismiss() {
      popupWindow.dismiss();
        content.dismiss();


        emojiResultReceiver.setReceiver(null);

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

    void showAtBottom() {
        isPendingOpen = false;
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);

        if (listener != null) listener.onShow();
    }

    @Override
    public void onReceiveResult(final int resultCode, final Bundle data) {
        if (resultCode == 0 || resultCode == 1) {
            showAtBottom();
        }
    }

}
