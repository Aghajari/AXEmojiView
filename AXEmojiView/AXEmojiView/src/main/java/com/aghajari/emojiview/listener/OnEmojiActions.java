package com.aghajari.emojiview.listener;

import android.view.View;

import com.aghajari.emojiview.emoji.Emoji;

public interface OnEmojiActions {
    void onClick(View view, Emoji emoji, boolean fromRecent, boolean fromVariant);
    void onLongClick(View view, Emoji emoji, boolean fromRecent, boolean fromVariant);
}
