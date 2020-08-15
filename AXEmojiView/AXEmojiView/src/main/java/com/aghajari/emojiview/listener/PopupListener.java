package com.aghajari.emojiview.listener;

public interface PopupListener {
    void onDismiss();
    void onShow();
    void onKeyboardOpened(int height);
    void onKeyboardClosed();
}
