package com.aghajari.axemojiview.listener;

public interface PopupListener {

    void onDismiss();
    void onShow();
    void onKeyboardOpened(int height);
    void onKeyboardClosed();
}
